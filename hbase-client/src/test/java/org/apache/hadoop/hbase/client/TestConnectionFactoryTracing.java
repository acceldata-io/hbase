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
package org.apache.hadoop.hbase.client;

import static org.apache.hadoop.hbase.HConstants.CLIENT_CONNECTION_REGISTRY_IMPL_CONF_KEY;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.hadoop.hbase.HBaseClassTestRule;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.security.UserProvider;
import org.apache.hadoop.hbase.testclassification.ClientTests;
import org.apache.hadoop.hbase.testclassification.SmallTests;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import org.apache.hbase.thirdparty.com.google.common.io.Closeables;

@Category({ ClientTests.class, SmallTests.class })
public class TestConnectionFactoryTracing extends TestTracingBase {

  @ClassRule
  public static final HBaseClassTestRule CLASS_RULE =
    HBaseClassTestRule.forClass(TestConnectionFactoryTracing.class);

  private User currentUser;
  private Object connection;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    currentUser = UserProvider.instantiate(conf).getCurrent();
    conf.set(CLIENT_CONNECTION_REGISTRY_IMPL_CONF_KEY, RegistryForTracingTest.class.getName());
  }

  @After
  public void tearDown() throws IOException {
    Closeables.close((Closeable) connection, true);
  }

  @Test
  public void testConnectionTracing() throws IOException {
    connection = ConnectionFactory.createConnection(conf, currentUser);
    assertTrace(ConnectionFactory.class.getSimpleName(), "createConnection", null, null);
  }

  @Test
  public void testAsyncConnectionTracing()
    throws IOException, ExecutionException, InterruptedException {
    connection = ConnectionFactory.createAsyncConnection(conf, currentUser).get();
    assertTrace(ConnectionFactory.class.getSimpleName(), "createAsyncConnection", null, null);
  }

}
