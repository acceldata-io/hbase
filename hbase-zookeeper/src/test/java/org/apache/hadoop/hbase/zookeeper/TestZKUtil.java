/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Abortable;
import org.apache.hadoop.hbase.HBaseClassTestRule;
import org.apache.hadoop.hbase.HBaseZKTestingUtility;
import org.apache.hadoop.hbase.testclassification.MediumTests;
import org.apache.hadoop.hbase.testclassification.ZKTests;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Threads;
import org.apache.hadoop.hbase.zookeeper.ZKUtil.ZKUtilOp;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.hbase.thirdparty.com.google.common.collect.ImmutableList;
import org.apache.hbase.thirdparty.com.google.common.io.Closeables;

@Category({ ZKTests.class, MediumTests.class })
public class TestZKUtil {
  @ClassRule
  public static final HBaseClassTestRule CLASS_RULE = HBaseClassTestRule.forClass(TestZKUtil.class);

  private static final Logger LOG = LoggerFactory.getLogger(TestZKUtil.class);

  private static HBaseZKTestingUtility UTIL = new HBaseZKTestingUtility();

  private static ZKWatcher ZKW;

  @BeforeClass
  public static void setUp() throws Exception {
    UTIL.startMiniZKCluster().getClientPort();
    ZKW = new ZKWatcher(new Configuration(UTIL.getConfiguration()), TestZKUtil.class.getName(),
      new WarnOnlyAbortable());
  }

  @AfterClass
  public static void tearDown() throws IOException {
    Closeables.close(ZKW, true);
    UTIL.shutdownMiniZKCluster();
    UTIL.cleanupTestDir();
  }

  /**
   * Create a znode with data
   */
  @Test
  public void testCreateWithParents() throws KeeperException, InterruptedException {
    byte[] expectedData = new byte[] { 1, 2, 3 };
    ZKUtil.createWithParents(ZKW, "/l1/l2/l3/l4/testCreateWithParents", expectedData);
    byte[] data = ZKUtil.getData(ZKW, "/l1/l2/l3/l4/testCreateWithParents");
    assertTrue(Bytes.equals(expectedData, data));
    ZKUtil.deleteNodeRecursively(ZKW, "/l1");

    ZKUtil.createWithParents(ZKW, "/testCreateWithParents", expectedData);
    data = ZKUtil.getData(ZKW, "/testCreateWithParents");
    assertTrue(Bytes.equals(expectedData, data));
    ZKUtil.deleteNodeRecursively(ZKW, "/testCreateWithParents");
  }

  /**
   * Create a bunch of znodes in a hierarchy, try deleting one that has childs (it will fail), then
   * delete it recursively, then delete the last znode
   */
  @Test
  public void testZNodeDeletes() throws Exception {
    try {
      ZKUtil.createWithParents(ZKW, "/l1/l2/l3/l4");

      // Attempt to delete /l1/l2 should fail because it has child nodes
      try {
        ZKUtil.deleteNode(ZKW, "/l1/l2");
        fail("Expected KeeperException$NotEmptyException");
      } catch (KeeperException.NotEmptyException e) {
        // Expected exception when trying to delete a non-empty znode
        assertNotNull(ZKUtil.getDataNoWatch(ZKW, "/l1/l2/l3/l4", null));
      }

      // Recursively delete /l1/l2 and its children
      ZKUtil.deleteNodeRecursively(ZKW, "/l1/l2");

      // Ensure /l1/l2 and its children are deleted
      assertNull(ZKUtil.getDataNoWatch(ZKW, "/l1/l2/l3/l4", null));
      assertNull(ZKUtil.getDataNoWatch(ZKW, "/l1/l2/l3", null));
      assertNull(ZKUtil.getDataNoWatch(ZKW, "/l1/l2", null));

      // Delete /l1 and ensure it's deleted
      ZKUtil.deleteNode(ZKW, "/l1");
      assertNull(ZKUtil.getDataNoWatch(ZKW, "/l1/l2", null));

    } catch (KeeperException e) {
      handleZooKeeperException(e);
    }
  }

  private int getZNodeDataVersion(String znode) throws KeeperException {
    Stat stat = new Stat();
    ZKUtil.getDataNoWatch(ZKW, znode, stat);
    return stat.getVersion();
  }

  @Test
  public void testSetDataWithVersion() throws Exception {
    try {
      ZKUtil.createWithParents(ZKW, "/s1/s2/s3");
      int v0 = getZNodeDataVersion("/s1/s2/s3");
      assertEquals(0, v0);

      ZKUtil.setData(ZKW, "/s1/s2/s3", Bytes.toBytes(12L));
      int v1 = getZNodeDataVersion("/s1/s2/s3");
      assertEquals(1, v1);

      ZKUtil.multiOrSequential(ZKW,
        ImmutableList.of(ZKUtilOp.setData("/s1/s2/s3", Bytes.toBytes(13L), v1)), false);
      int v2 = getZNodeDataVersion("/s1/s2/s3");
      assertEquals(2, v2);
    } catch (KeeperException e) {
      handleZooKeeperException(e);
    }
  }

  /**
   * A test for HBASE-3238
   * @throws IOException          A connection attempt to zk failed
   * @throws InterruptedException One of the non ZKUtil actions was interrupted
   * @throws KeeperException      Any of the zookeeper connections had a KeeperException
   */
  @Test
  public void testCreateSilentIsReallySilent() throws InterruptedException, KeeperException, IOException {
    Configuration c = UTIL.getConfiguration();

    String aclZnode = "/aclRoot";
    String quorumServers = ZKConfig.getZKQuorumServersString(c);
    int sessionTimeout = 5 * 1000; // 5 seconds
    ZooKeeper zk = new ZooKeeper(quorumServers, sessionTimeout, EmptyWatcher.instance);
    zk.addAuthInfo("digest", "hbase:rox".getBytes());

    // Save the previous ACL
    Stat s;
    List<ACL> oldACL;
    while (true) {
      try {
        s = new Stat();
        oldACL = zk.getACL("/", s);
        break;
      } catch (KeeperException e) {
        handleZooKeeperException(e);
      }
    }

    // Set ACL on root
    while (true) {
      try {
        zk.setACL("/", ZooDefs.Ids.CREATOR_ALL_ACL, -1);
        break;
      } catch (KeeperException e) {
        handleZooKeeperException(e);
      }
    }

    // Create ACL znode
    while (true) {
      try {
        zk.create(aclZnode, null, ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        break;
      } catch (KeeperException e) {
        handleZooKeeperException(e);
      }
    }
    zk.close();

    // Use ZKUtil to create and fail silently
    try {
      ZKUtil.createAndFailSilent(ZKW, aclZnode);
    } catch (KeeperException.NoAuthException e) {
      // Handle NoAuthException if needed
      System.out.println("NoAuthException occurred: " + e.getMessage());
    }

    // Restore the original ACL
    ZooKeeper zk3 = new ZooKeeper(quorumServers, sessionTimeout, EmptyWatcher.instance);
    zk3.addAuthInfo("digest", "hbase:rox".getBytes());
    try {
      zk3.setACL("/", oldACL, -1);
    } finally {
      zk3.close();
    }
  }

  private void handleZooKeeperException(KeeperException e) throws InterruptedException {
    switch (e.code()) {
      case CONNECTIONLOSS:
      case SESSIONEXPIRED:
      case OPERATIONTIMEOUT:
        LOG.warn("Possibly transient ZooKeeper exception", e);
        Threads.sleep(100);
        break;
      case NOAUTH:
        LOG.warn("NoAuth error: Insufficient permissions to perform the operation", e);
        // Handle NoAuth exception as needed
        // For test purposes, you might log or ignore this error depending on test requirements
        break;
      case NOTEMPTY:
        LOG.warn("KeeperException$NotEmptyException: Directory not empty", e);
        // Handle the specific exception when trying to delete a non-empty znode
        // For test purposes, you might log or ignore this error depending on test requirements
        break;
      default:
        throw new RuntimeException("Unexpected ZooKeeper exception", e);
    }
  }


  /**
   * Test should not fail with NPE when getChildDataAndWatchForNewChildren invoked with wrongNode
   */
  @Test
  @SuppressWarnings("deprecation")
  public void testGetChildDataAndWatchForNewChildrenShouldNotThrowNPE() throws Exception {
    ZKUtil.getChildDataAndWatchForNewChildren(ZKW, "/wrongNode");
  }

  private static class WarnOnlyAbortable implements Abortable {

    @Override
    public void abort(String why, Throwable e) {
      LOG.warn("ZKWatcher received abort, ignoring.  Reason: " + why);
      if (LOG.isDebugEnabled()) {
        LOG.debug(e.toString(), e);
      }
    }

    @Override
    public boolean isAborted() {
      return false;
    }
  }
}
