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
package org.apache.hadoop.hbase.master.snapshot;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseInterfaceAudience;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.master.MasterServices;
import org.apache.hadoop.hbase.master.cleaner.BaseHFileCleanerDelegate;
import org.apache.hadoop.hbase.snapshot.CorruptedSnapshotException;
import org.apache.hadoop.hbase.snapshot.SnapshotDescriptionUtils;
import org.apache.hadoop.hbase.snapshot.SnapshotReferenceUtil;
import org.apache.hadoop.hbase.util.CommonFSUtils;
import org.apache.yetus.audience.InterfaceAudience;
import org.apache.yetus.audience.InterfaceStability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a file cleaner that checks if a hfile is still used by snapshots of HBase
 * tables.
 */
@InterfaceAudience.LimitedPrivate(HBaseInterfaceAudience.CONFIG)
@InterfaceStability.Evolving
public class SnapshotHFileCleaner extends BaseHFileCleanerDelegate {
  private static final Logger LOG = LoggerFactory.getLogger(SnapshotHFileCleaner.class);

  /**
   * Conf key for the frequency to attempt to refresh the cache of hfiles currently used in
   * snapshots (ms)
   */
  public static final String HFILE_CACHE_REFRESH_PERIOD_CONF_KEY =
    "hbase.master.hfilecleaner.plugins.snapshot.period";

  /** Refresh cache, by default, every 5 minutes */
  private static final long DEFAULT_HFILE_CACHE_REFRESH_PERIOD = 300000;

  /** File cache for HFiles in the completed and currently running snapshots */
  private SnapshotFileCache cache;

  private MasterServices master;

  @Override
  public Iterable<FileStatus> getDeletableFiles(Iterable<FileStatus> files) {
    // The Iterable is lazy evaluated, so if we just pass this Iterable in, we will access the HFile
    // storage inside the snapshot lock, which could take a lot of time (for example, several
    // seconds), and block all other operations, especially other cleaners.
    // So here we convert it to List first, to force it evaluated before calling
    // getUnreferencedFiles, so we will not hold snapshot lock for a long time.
    List<FileStatus> filesList =
      StreamSupport.stream(files.spliterator(), false).collect(Collectors.toList());
    try {
      return cache.getUnreferencedFiles(filesList, master.getSnapshotManager());
    } catch (CorruptedSnapshotException cse) {
      LOG.debug("Corrupted in-progress snapshot file exception, ignored ", cse);
    } catch (IOException e) {
      LOG.error("Exception while checking if files were valid, keeping them just in case.", e);
    }
    return Collections.emptyList();
  }

  @Override
  public void init(Map<String, Object> params) {
    if (params.containsKey(HMaster.MASTER)) {
      this.master = (MasterServices) params.get(HMaster.MASTER);
    }
  }

  @Override
  protected boolean isFileDeletable(FileStatus fStat) {
    return false;
  }

  @Override
  public void setConf(final Configuration conf) {
    super.setConf(conf);
    try {
      long cacheRefreshPeriod =
        conf.getLong(HFILE_CACHE_REFRESH_PERIOD_CONF_KEY, DEFAULT_HFILE_CACHE_REFRESH_PERIOD);
      final FileSystem fs = CommonFSUtils.getCurrentFileSystem(conf);
      Path rootDir = CommonFSUtils.getRootDir(conf);
      Path workingDir = SnapshotDescriptionUtils.getWorkingSnapshotDir(rootDir, conf);
      FileSystem workingFs = workingDir.getFileSystem(conf);

      cache = new SnapshotFileCache(fs, rootDir, workingFs, workingDir, cacheRefreshPeriod,
        cacheRefreshPeriod, "snapshot-hfile-cleaner-cache-refresher",
        new SnapshotFileCache.SnapshotFileInspector() {
          @Override
          public Collection<String> filesUnderSnapshot(final FileSystem fs, final Path snapshotDir)
            throws IOException {
            return SnapshotReferenceUtil.getHFileNames(conf, fs, snapshotDir);
          }
        });
    } catch (IOException e) {
      LOG.error("Failed to create cleaner util", e);
    }
  }

  @Override
  public void stop(String why) {
    this.cache.stop(why);
  }

  @Override
  public boolean isStopped() {
    return this.cache.isStopped();
  }

  /**
   * Exposed for Testing!
   * @return the cache of all hfiles
   */
  public SnapshotFileCache getFileCacheForTesting() {
    return this.cache;
  }
}
