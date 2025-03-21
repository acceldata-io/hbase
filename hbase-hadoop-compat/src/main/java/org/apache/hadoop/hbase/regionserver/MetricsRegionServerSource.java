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
package org.apache.hadoop.hbase.regionserver;

import org.apache.hadoop.hbase.metrics.BaseSource;
import org.apache.hadoop.hbase.metrics.JvmPauseMonitorSource;
import org.apache.yetus.audience.InterfaceAudience;

/**
 * Interface for classes that expose metrics about the regionserver.
 */
@InterfaceAudience.Private
public interface MetricsRegionServerSource extends BaseSource, JvmPauseMonitorSource {

  /**
   * The name of the metrics
   */
  String METRICS_NAME = "Server";

  /**
   * The name of the metrics context that metrics will be under.
   */
  String METRICS_CONTEXT = "regionserver";

  /**
   * Description
   */
  String METRICS_DESCRIPTION = "Metrics about HBase RegionServer";

  /**
   * The name of the metrics context that metrics will be under in jmx
   */
  String METRICS_JMX_CONTEXT = "RegionServer,sub=" + METRICS_NAME;

  /**
   * Update the Put time histogram
   * @param t time it took
   */
  void updatePut(long t);

  /**
   * Update the PutBatch time histogram if a batch contains a Put op
   */
  void updatePutBatch(long t);

  /**
   * Update the Delete time histogram
   * @param t time it took
   */
  void updateDelete(long t);

  /**
   * Update the Delete time histogram if a batch contains a delete op
   * @param t time it took
   */
  void updateDeleteBatch(long t);

  /**
   * Update checkAndDelete histogram
   * @param t time it took
   */
  void updateCheckAndDelete(long t);

  /**
   * Update checkAndPut histogram
   * @param t time it took
   */
  void updateCheckAndPut(long t);

  /**
   * Update checkAndMutate histogram
   * @param t time it took
   */
  void updateCheckAndMutate(long t);

  /**
   * Update the Get time histogram .
   * @param t time it took
   */
  void updateGet(long t);

  /**
   * Update the Increment time histogram.
   * @param t time it took
   */
  void updateIncrement(long t);

  /**
   * Update the Append time histogram.
   * @param t time it took
   */
  void updateAppend(long t);

  /**
   * Update the Replay time histogram.
   * @param t time it took
   */
  void updateReplay(long t);

  /**
   * Update the scan size.
   * @param scanSize size of the scan
   */
  void updateScanSize(long scanSize);

  /**
   * Update the scan time.
   */
  void updateScanTime(long t);

  /**
   * Increment the number of slow Puts that have happened.
   */
  void incrSlowPut();

  /**
   * Increment the number of slow Deletes that have happened.
   */
  void incrSlowDelete();

  /**
   * Increment the number of slow Gets that have happened.
   */
  void incrSlowGet();

  /**
   * Increment the number of slow Increments that have happened.
   */
  void incrSlowIncrement();

  /**
   * Increment the number of slow Appends that have happened.
   */
  void incrSlowAppend();

  /**
   * Update the split transaction time histogram
   * @param t time it took, in milliseconds
   */
  void updateSplitTime(long t);

  /**
   * Increment number of a requested splits
   */
  void incrSplitRequest();

  /**
   * Increment number of successful splits
   */
  void incrSplitSuccess();

  /**
   * Update the flush time histogram
   * @param t time it took, in milliseconds
   */
  void updateFlushTime(long t);

  /**
   * Update the flush memstore size histogram
   * @param bytes the number of bytes in the memstore
   */
  void updateFlushMemStoreSize(long bytes);

  /**
   * Update the flush output file size histogram
   * @param bytes the number of bytes in the output file
   */
  void updateFlushOutputSize(long bytes);

  /**
   * Update the compaction time histogram, both major and minor
   * @param isMajor whether compaction is a major compaction
   * @param t       time it took, in milliseconds
   */
  void updateCompactionTime(boolean isMajor, long t);

  /**
   * Update the compaction input number of files histogram
   * @param isMajor whether compaction is a major compaction
   * @param c       number of files
   */
  void updateCompactionInputFileCount(boolean isMajor, long c);

  /**
   * Update the compaction total input file size histogram
   * @param isMajor whether compaction is a major compaction
   * @param bytes   the number of bytes of the compaction input file
   */
  void updateCompactionInputSize(boolean isMajor, long bytes);

  /**
   * Update the compaction output number of files histogram
   * @param isMajor whether compaction is a major compaction
   * @param c       number of files
   */
  void updateCompactionOutputFileCount(boolean isMajor, long c);

  /**
   * Update the compaction total output file size
   * @param isMajor whether compaction is a major compaction
   * @param bytes   the number of bytes of the compaction input file
   */
  void updateCompactionOutputSize(boolean isMajor, long bytes);

  void incrScannerLeaseExpired();

  // Strings used for exporting to metrics system.
  String REGION_COUNT = "regionCount";
  String REGION_COUNT_DESC = "Number of regions";
  String STORE_COUNT = "storeCount";
  String STORE_COUNT_DESC = "Number of Stores";
  String WALFILE_COUNT = "hlogFileCount";
  String WALFILE_COUNT_DESC = "Number of WAL Files";
  String WALFILE_SIZE = "hlogFileSize";
  String WALFILE_SIZE_DESC = "Size of all WAL Files";
  String STOREFILE_COUNT = "storeFileCount";
  String STOREFILE_COUNT_DESC = "Number of Store Files";
  String STORE_REF_COUNT = "storeRefCount";
  String STORE_REF_COUNT_DESC = "Store reference count";
  String MAX_COMPACTED_STORE_FILE_REF_COUNT = "maxCompactedStoreFileRefCount";
  String MEMSTORE_SIZE = "memStoreSize";
  String MEMSTORE_SIZE_DESC = "Size of the memstore";
  String MEMSTORE_HEAP_SIZE = "memStoreHeapSize";
  String MEMSTORE_HEAP_SIZE_DESC = "On-heap Size of the memstore";
  String MEMSTORE_OFFHEAP_SIZE = "memStoreOffHeapSize";
  String MEMSTORE_OFFHEAP_SIZE_DESC = "Off-heap Size of the memstore";
  String STOREFILE_SIZE = "storeFileSize";
  String MAX_STORE_FILE_AGE = "maxStoreFileAge";
  String MIN_STORE_FILE_AGE = "minStoreFileAge";
  String AVG_STORE_FILE_AGE = "avgStoreFileAge";
  String NUM_REFERENCE_FILES = "numReferenceFiles";
  String MAX_STORE_FILE_AGE_DESC = "Max age of store files hosted on this RegionServer";
  String MIN_STORE_FILE_AGE_DESC = "Min age of store files hosted on this RegionServer";
  String AVG_STORE_FILE_AGE_DESC = "Average age of store files hosted on this RegionServer";
  String NUM_REFERENCE_FILES_DESC = "Number of reference file on this RegionServer";
  String STOREFILE_SIZE_DESC = "Size of storefiles being served.";
  String TOTAL_REQUEST_COUNT = "totalRequestCount";
  String TOTAL_REQUEST_COUNT_DESC =
    "Total number of requests this RegionServer has answered; increments the count once for "
      + "EVERY access whether an admin operation, a Scan, a Put or Put of 1M rows, or a Get "
      + "of a non-existent row";
  String TOTAL_ROW_ACTION_REQUEST_COUNT = "totalRowActionRequestCount";
  String TOTAL_ROW_ACTION_REQUEST_COUNT_DESC =
    "Total number of region requests this RegionServer has answered; counts by row-level "
      + "action at the RPC Server (Sums 'readRequestsCount' and 'writeRequestsCount'); counts"
      + "once per access whether a Put of 1M rows or a Get that returns 1M Results";
  String READ_REQUEST_COUNT = "readRequestCount";
  String FILTERED_READ_REQUEST_COUNT = "filteredReadRequestCount";
  String FILTERED_READ_REQUEST_COUNT_DESC =
    "Number of read requests this region server has answered.";
  String READ_REQUEST_COUNT_DESC =
    "Number of read requests with non-empty Results that this RegionServer has answered.";
  String READ_REQUEST_RATE_PER_SECOND = "readRequestRatePerSecond";
  String READ_REQUEST_RATE_DESC =
    "Rate of answering the read requests by this region server per second.";
  String WRITE_REQUEST_COUNT = "writeRequestCount";
  String WRITE_REQUEST_COUNT_DESC = "Number of mutation requests this RegionServer has answered.";
  String WRITE_REQUEST_RATE_PER_SECOND = "writeRequestRatePerSecond";
  String WRITE_REQUEST_RATE_DESC =
    "Rate of answering the mutation requests by this region server per second.";
  String CHECK_MUTATE_FAILED_COUNT = "checkMutateFailedCount";
  String CHECK_MUTATE_FAILED_COUNT_DESC =
    "Number of Check and Mutate calls that failed the checks.";
  String CHECK_MUTATE_PASSED_COUNT = "checkMutatePassedCount";
  String CHECK_MUTATE_PASSED_COUNT_DESC =
    "Number of Check and Mutate calls that passed the checks.";
  String STOREFILE_INDEX_SIZE = "storeFileIndexSize";
  String STOREFILE_INDEX_SIZE_DESC = "Size of indexes in storefiles on disk.";
  String STATIC_INDEX_SIZE = "staticIndexSize";
  String STATIC_INDEX_SIZE_DESC = "Uncompressed size of the static indexes.";
  String STATIC_BLOOM_SIZE = "staticBloomSize";
  String STATIC_BLOOM_SIZE_DESC = "Uncompressed size of the static bloom filters.";

  String BLOOM_FILTER_REQUESTS_COUNT = "bloomFilterRequestsCount";
  String BLOOM_FILTER_REQUESTS_COUNT_DESC = "Count of requests to bloom filters.";

  String BLOOM_FILTER_NEGATIVE_RESULTS_COUNT = "bloomFilterNegativeResultsCount";
  String BLOOM_FILTER_NEGATIVE_RESULTS_COUNT_DESC =
    "Count of bloom filter requests which returned a negative result.";

  String BLOOM_FILTER_ELIGIBLE_REQUESTS_COUNT = "bloomFilterEligibleRequestsCount";
  String BLOOM_FILTER_ELIGIBLE_REQUESTS_COUNT_DESC =
    "Count of requests which could have used bloom filters but didn't because they weren't configured or loaded";

  String NUMBER_OF_MUTATIONS_WITHOUT_WAL = "mutationsWithoutWALCount";
  String NUMBER_OF_MUTATIONS_WITHOUT_WAL_DESC =
    "Number of mutations that have been sent by clients with the write ahead logging turned off.";
  String DATA_SIZE_WITHOUT_WAL = "mutationsWithoutWALSize";
  String DATA_SIZE_WITHOUT_WAL_DESC =
    "Size of data that has been sent by clients with the write ahead logging turned off.";
  String PERCENT_FILES_LOCAL = "percentFilesLocal";
  String PERCENT_FILES_LOCAL_DESC =
    "The percent of HFiles that are stored on the local hdfs data node.";
  String PERCENT_FILES_LOCAL_SECONDARY_REGIONS = "percentFilesLocalSecondaryRegions";
  String PERCENT_FILES_LOCAL_SECONDARY_REGIONS_DESC =
    "The percent of HFiles used by secondary regions that are stored on the local hdfs data node.";
  String SPLIT_QUEUE_LENGTH = "splitQueueLength";
  String SPLIT_QUEUE_LENGTH_DESC = "Length of the queue for splits.";
  String COMPACTION_QUEUE_LENGTH = "compactionQueueLength";
  String LARGE_COMPACTION_QUEUE_LENGTH = "largeCompactionQueueLength";
  String SMALL_COMPACTION_QUEUE_LENGTH = "smallCompactionQueueLength";
  String COMPACTION_QUEUE_LENGTH_DESC = "Length of the queue for compactions.";
  String LARGE_COMPACTION_QUEUE_LENGTH_DESC = "Length of the queue for compactions with input size "
    + "larger than throttle threshold (2.5GB by default)";
  String SMALL_COMPACTION_QUEUE_LENGTH_DESC = "Length of the queue for compactions with input size "
    + "smaller than throttle threshold (2.5GB by default)";
  String FLUSH_QUEUE_LENGTH = "flushQueueLength";
  String FLUSH_QUEUE_LENGTH_DESC = "Length of the queue for region flushes";
  String BLOCK_CACHE_FREE_SIZE = "blockCacheFreeSize";
  String BLOCK_CACHE_FREE_DESC = "Size of the block cache that is not occupied.";
  String BLOCK_CACHE_COUNT = "blockCacheCount";
  String BLOCK_CACHE_COUNT_DESC = "Number of block in the block cache.";
  String BLOCK_CACHE_DATA_BLOCK_COUNT = "blockCacheDataBlockCount";
  String BLOCK_CACHE_DATA_BLOCK_COUNT_DESC = "Number of DATA block in the block cache.";
  String BLOCK_CACHE_SIZE = "blockCacheSize";
  String BLOCK_CACHE_SIZE_DESC = "Size of the block cache.";
  String BLOCK_CACHE_HIT_COUNT = "blockCacheHitCount";
  String BLOCK_CACHE_HIT_COUNT_DESC = "Count of the hit on the block cache.";
  String BLOCK_CACHE_PRIMARY_HIT_COUNT = "blockCacheHitCountPrimary";
  String BLOCK_CACHE_PRIMARY_HIT_COUNT_DESC = "Count of hit on primary replica in the block cache.";
  String BLOCK_CACHE_HIT_CACHING_COUNT = "blockCacheHitCachingCount";
  String BLOCK_CACHE_HIT_CACHING_COUNT_DESC =
    "Count of the hit on the block cache, for cacheable requests.";
  String BLOCK_CACHE_MISS_COUNT = "blockCacheMissCount";
  String BLOCK_COUNT_MISS_COUNT_DESC =
    "Number of requests for a block that missed the block cache.";
  String BLOCK_CACHE_PRIMARY_MISS_COUNT = "blockCacheMissCountPrimary";
  String BLOCK_COUNT_PRIMARY_MISS_COUNT_DESC =
    "Number of requests for a block of primary replica that missed the block cache.";
  String BLOCK_CACHE_MISS_CACHING_COUNT = "blockCacheMissCachingCount";
  String BLOCK_COUNT_MISS_CACHING_COUNT_DESC =
    "Number of requests for a block that missed the block cache, for cacheable requests.";
  String BLOCK_CACHE_EVICTION_COUNT = "blockCacheEvictionCount";
  String BLOCK_CACHE_EVICTION_COUNT_DESC =
    "Count of the number of blocks evicted from the block cache."
      + "(Not including blocks evicted because of HFile removal)";
  String BLOCK_CACHE_PRIMARY_EVICTION_COUNT = "blockCacheEvictionCountPrimary";
  String BLOCK_CACHE_PRIMARY_EVICTION_COUNT_DESC =
    "Count of the number of blocks evicted from primary replica in the block cache.";
  String BLOCK_CACHE_HIT_PERCENT = "blockCacheCountHitPercent";
  String BLOCK_CACHE_HIT_PERCENT_DESC = "Percent of block cache requests that are hits";
  String BLOCK_CACHE_EXPRESS_HIT_PERCENT = "blockCacheExpressHitPercent";
  String BLOCK_CACHE_EXPRESS_HIT_PERCENT_DESC =
    "The percent of the time that requests with the cache turned on hit the cache.";
  String BLOCK_CACHE_FAILED_INSERTION_COUNT = "blockCacheFailedInsertionCount";
  String BLOCK_CACHE_FAILED_INSERTION_COUNT_DESC =
    "Number of times that a block cache " + "insertion failed. Usually due to size restrictions.";
  String BLOCK_CACHE_DATA_MISS_COUNT = "blockCacheDataMissCount";
  String BLOCK_CACHE_ENCODED_DATA_MISS_COUNT = "blockCacheEncodedDataMissCount";
  String BLOCK_CACHE_LEAF_INDEX_MISS_COUNT = "blockCacheLeafIndexMissCount";
  String BLOCK_CACHE_BLOOM_CHUNK_MISS_COUNT = "blockCacheBloomChunkMissCount";
  String BLOCK_CACHE_META_MISS_COUNT = "blockCacheMetaMissCount";
  String BLOCK_CACHE_ROOT_INDEX_MISS_COUNT = "blockCacheRootIndexMissCount";
  String BLOCK_CACHE_INTERMEDIATE_INDEX_MISS_COUNT = "blockCacheIntermediateIndexMissCount";
  String BLOCK_CACHE_FILE_INFO_MISS_COUNT = "blockCacheFileInfoMissCount";
  String BLOCK_CACHE_GENERAL_BLOOM_META_MISS_COUNT = "blockCacheGeneralBloomMetaMissCount";
  String BLOCK_CACHE_DELETE_FAMILY_BLOOM_MISS_COUNT = "blockCacheDeleteFamilyBloomMissCount";
  String BLOCK_CACHE_TRAILER_MISS_COUNT = "blockCacheTrailerMissCount";
  String BLOCK_CACHE_DATA_HIT_COUNT = "blockCacheDataHitCount";
  String BLOCK_CACHE_ENCODED_DATA_HIT_COUNT = "blockCacheEncodedDataHitCount";
  String BLOCK_CACHE_LEAF_INDEX_HIT_COUNT = "blockCacheLeafIndexHitCount";
  String BLOCK_CACHE_BLOOM_CHUNK_HIT_COUNT = "blockCacheBloomChunkHitCount";
  String BLOCK_CACHE_META_HIT_COUNT = "blockCacheMetaHitCount";
  String BLOCK_CACHE_ROOT_INDEX_HIT_COUNT = "blockCacheRootIndexHitCount";
  String BLOCK_CACHE_INTERMEDIATE_INDEX_HIT_COUNT = "blockCacheIntermediateIndexHitCount";
  String BLOCK_CACHE_FILE_INFO_HIT_COUNT = "blockCacheFileInfoHitCount";
  String BLOCK_CACHE_GENERAL_BLOOM_META_HIT_COUNT = "blockCacheGeneralBloomMetaHitCount";
  String BLOCK_CACHE_DELETE_FAMILY_BLOOM_HIT_COUNT = "blockCacheDeleteFamilyBloomHitCount";
  String BLOCK_CACHE_TRAILER_HIT_COUNT = "blockCacheTrailerHitCount";
  String L1_CACHE_FREE_SIZE = "l1CacheFreeSize";
  String L1_CACHE_FREE_SIZE_DESC = "Amount of free bytes in the L1 cache";
  String L1_CACHE_SIZE = "l1CacheSize";
  String L1_CACHE_SIZE_DESC = "Size of the L1 cache in bytes";
  String L1_CACHE_COUNT = "l1CacheCount";
  String L1_CACHE_COUNT_DESC = "Count of blocks in the L1 cache";
  String L1_CACHE_EVICTION_COUNT = "l1CacheEvictionCount";
  String L1_CACHE_EVICTION_COUNT_DESC = "Count of blocks evicted from the L1 cache";

  String L1_CACHE_HIT_COUNT = "l1CacheHitCount";
  String L1_CACHE_HIT_COUNT_DESC = "L1 cache hit count.";
  String L1_CACHE_MISS_COUNT = "l1CacheMissCount";
  String L1_CACHE_MISS_COUNT_DESC = "L1 cache miss count.";
  String L1_CACHE_HIT_RATIO = "l1CacheHitRatio";
  String L1_CACHE_HIT_RATIO_DESC = "L1 cache hit ratio.";
  String L1_CACHE_MISS_RATIO = "l1CacheMissRatio";
  String L1_CACHE_MISS_RATIO_DESC = "L1 cache miss ratio.";
  String L2_CACHE_FREE_SIZE = "l2CacheFreeSize";
  String L2_CACHE_FREE_SIZE_DESC = "Amount of free bytes in the L2 cache";
  String L2_CACHE_SIZE = "l2CacheSize";
  String L2_CACHE_SIZE_DESC = "Size of the L2 cache in bytes";
  String L2_CACHE_COUNT = "l2CacheCount";
  String L2_CACHE_COUNT_DESC = "Count of blocks in the L2 cache";
  String L2_CACHE_EVICTION_COUNT = "l2CacheEvictionCount";
  String L2_CACHE_EVICTION_COUNT_DESC = "Count of blocks evicted from the L2 cache";
  String L2_CACHE_HIT_COUNT = "l2CacheHitCount";
  String L2_CACHE_HIT_COUNT_DESC = "L2 cache hit count.";
  String L2_CACHE_MISS_COUNT = "l2CacheMissCount";
  String L2_CACHE_MISS_COUNT_DESC = "L2 cache miss count.";
  String L2_CACHE_HIT_RATIO = "l2CacheHitRatio";
  String L2_CACHE_HIT_RATIO_DESC = "L2 cache hit ratio.";
  String L2_CACHE_MISS_RATIO = "l2CacheMissRatio";
  String L2_CACHE_MISS_RATIO_DESC = "L2 cache miss ratio.";
  String RS_START_TIME_NAME = "regionServerStartTime";
  String ZOOKEEPER_QUORUM_NAME = "zookeeperQuorum";
  String SERVER_NAME_NAME = "serverName";
  String CLUSTER_ID_NAME = "clusterId";
  String RS_START_TIME_DESC = "RegionServer Start Time";
  String ZOOKEEPER_QUORUM_DESC = "ZooKeeper Quorum";
  String SERVER_NAME_DESC = "Server Name";
  String CLUSTER_ID_DESC = "Cluster Id";
  String UPDATES_BLOCKED_TIME = "updatesBlockedTime";
  String UPDATES_BLOCKED_DESC =
    "Number of MS updates have been blocked so that the memstore can be flushed.";
  String DELETE_KEY = "delete";
  String CHECK_AND_DELETE_KEY = "checkAndDelete";
  String CHECK_AND_PUT_KEY = "checkAndPut";
  String CHECK_AND_MUTATE_KEY = "checkAndMutate";
  String DELETE_BATCH_KEY = "deleteBatch";
  String GET_SIZE_KEY = "getSize";
  String GET_KEY = "get";
  String INCREMENT_KEY = "increment";
  String PUT_KEY = "put";
  String PUT_BATCH_KEY = "putBatch";
  String APPEND_KEY = "append";
  String REPLAY_KEY = "replay";
  String SCAN_KEY = "scan";
  String SCAN_SIZE_KEY = "scanSize";
  String SCAN_TIME_KEY = "scanTime";

  String SLOW_PUT_KEY = "slowPutCount";
  String SLOW_GET_KEY = "slowGetCount";
  String SLOW_DELETE_KEY = "slowDeleteCount";
  String SLOW_INCREMENT_KEY = "slowIncrementCount";
  String SLOW_APPEND_KEY = "slowAppendCount";
  String SLOW_PUT_DESC = "The number of batches containing puts that took over 1000ms to complete";
  String SLOW_DELETE_DESC =
    "The number of batches containing delete(s) that took over 1000ms to complete";
  String SLOW_GET_DESC = "The number of Gets that took over 1000ms to complete";
  String SLOW_INCREMENT_DESC = "The number of Increments that took over 1000ms to complete";
  String SLOW_APPEND_DESC = "The number of Appends that took over 1000ms to complete";

  String FLUSHED_CELLS = "flushedCellsCount";
  String FLUSHED_CELLS_DESC = "The number of cells flushed to disk";
  String FLUSHED_CELLS_SIZE = "flushedCellsSize";
  String FLUSHED_CELLS_SIZE_DESC = "The total amount of data flushed to disk, in bytes";
  String COMPACTED_CELLS = "compactedCellsCount";
  String COMPACTED_CELLS_DESC = "The number of cells processed during minor compactions";
  String COMPACTED_CELLS_SIZE = "compactedCellsSize";
  String COMPACTED_CELLS_SIZE_DESC =
    "The total amount of data processed during minor compactions, in bytes";
  String MAJOR_COMPACTED_CELLS = "majorCompactedCellsCount";
  String MAJOR_COMPACTED_CELLS_DESC = "The number of cells processed during major compactions";
  String MAJOR_COMPACTED_CELLS_SIZE = "majorCompactedCellsSize";
  String MAJOR_COMPACTED_CELLS_SIZE_DESC =
    "The total amount of data processed during major compactions, in bytes";
  String CELLS_COUNT_COMPACTED_TO_MOB = "cellsCountCompactedToMob";
  String CELLS_COUNT_COMPACTED_TO_MOB_DESC = "The number of cells moved to mob during compaction";
  String CELLS_COUNT_COMPACTED_FROM_MOB = "cellsCountCompactedFromMob";
  String CELLS_COUNT_COMPACTED_FROM_MOB_DESC =
    "The number of cells moved from mob during compaction";
  String CELLS_SIZE_COMPACTED_TO_MOB = "cellsSizeCompactedToMob";
  String CELLS_SIZE_COMPACTED_TO_MOB_DESC =
    "The total amount of cells move to mob during compaction, in bytes";
  String CELLS_SIZE_COMPACTED_FROM_MOB = "cellsSizeCompactedFromMob";
  String CELLS_SIZE_COMPACTED_FROM_MOB_DESC =
    "The total amount of cells move from mob during compaction, in bytes";
  String MOB_FLUSH_COUNT = "mobFlushCount";
  String MOB_FLUSH_COUNT_DESC = "The number of the flushes in mob-enabled stores";
  String MOB_FLUSHED_CELLS_COUNT = "mobFlushedCellsCount";
  String MOB_FLUSHED_CELLS_COUNT_DESC = "The number of mob cells flushed to disk";
  String MOB_FLUSHED_CELLS_SIZE = "mobFlushedCellsSize";
  String MOB_FLUSHED_CELLS_SIZE_DESC = "The total amount of mob cells flushed to disk, in bytes";
  String MOB_SCAN_CELLS_COUNT = "mobScanCellsCount";
  String MOB_SCAN_CELLS_COUNT_DESC = "The number of scanned mob cells";
  String MOB_SCAN_CELLS_SIZE = "mobScanCellsSize";
  String MOB_SCAN_CELLS_SIZE_DESC = "The total amount of scanned mob cells, in bytes";
  String MOB_FILE_CACHE_ACCESS_COUNT = "mobFileCacheAccessCount";
  String MOB_FILE_CACHE_ACCESS_COUNT_DESC = "The count of accesses to the mob file cache";
  String MOB_FILE_CACHE_MISS_COUNT = "mobFileCacheMissCount";
  String MOB_FILE_CACHE_MISS_COUNT_DESC = "The count of misses to the mob file cache";
  String MOB_FILE_CACHE_HIT_PERCENT = "mobFileCacheHitPercent";
  String MOB_FILE_CACHE_HIT_PERCENT_DESC = "The hit percent to the mob file cache";
  String MOB_FILE_CACHE_EVICTED_COUNT = "mobFileCacheEvictedCount";
  String MOB_FILE_CACHE_EVICTED_COUNT_DESC = "The number of items evicted from the mob file cache";
  String MOB_FILE_CACHE_COUNT = "mobFileCacheCount";
  String MOB_FILE_CACHE_COUNT_DESC = "The count of cached mob files";

  String HEDGED_READS = "hedgedReads";
  String HEDGED_READS_DESC = "The number of times we started a hedged read";
  String HEDGED_READ_WINS = "hedgedReadWins";
  String HEDGED_READ_WINS_DESC =
    "The number of times we started a hedged read and a hedged read won";
  String HEDGED_READ_IN_CUR_THREAD = "hedgedReadOpsInCurThread";
  String HEDGED_READ_IN_CUR_THREAD_DESC = "The number of times we execute a hedged read"
    + " in current thread as a fallback for task rejection";

  String TOTAL_BYTES_READ = "totalBytesRead";
  String TOTAL_BYTES_READ_DESC = "The total number of bytes read from HDFS";
  String LOCAL_BYTES_READ = "localBytesRead";
  String LOCAL_BYTES_READ_DESC = "The number of bytes read from the local HDFS DataNode";
  String SHORTCIRCUIT_BYTES_READ = "shortCircuitBytesRead";
  String SHORTCIRCUIT_BYTES_READ_DESC = "The number of bytes read through HDFS short circuit read";
  String ZEROCOPY_BYTES_READ = "zeroCopyBytesRead";
  String ZEROCOPY_BYTES_READ_DESC = "The number of bytes read through HDFS zero copy";

  String BLOCKED_REQUESTS_COUNT = "blockedRequestCount";
  String BLOCKED_REQUESTS_COUNT_DESC = "The number of blocked requests because of memstore size is "
    + "larger than blockingMemStoreSize";

  String SPLIT_KEY = "splitTime";
  String SPLIT_REQUEST_KEY = "splitRequestCount";
  String SPLIT_REQUEST_DESC = "Number of splits requested";
  String SPLIT_SUCCESS_KEY = "splitSuccessCount";
  String SPLIT_SUCCESS_DESC = "Number of successfully executed splits";

  String FLUSH_TIME = "flushTime";
  String FLUSH_TIME_DESC = "Histogram for the time in millis for memstore flush";
  String FLUSH_MEMSTORE_SIZE = "flushMemstoreSize";
  String FLUSH_MEMSTORE_SIZE_DESC = "Histogram for number of bytes in the memstore for a flush";
  String FLUSH_OUTPUT_SIZE = "flushOutputSize";
  String FLUSH_OUTPUT_SIZE_DESC = "Histogram for number of bytes in the resulting file for a flush";
  String FLUSHED_OUTPUT_BYTES = "flushedOutputBytes";
  String FLUSHED_OUTPUT_BYTES_DESC = "Total number of bytes written from flush";
  String FLUSHED_MEMSTORE_BYTES = "flushedMemstoreBytes";
  String FLUSHED_MEMSTORE_BYTES_DESC = "Total number of bytes of cells in memstore from flush";

  String COMPACTION_TIME = "compactionTime";
  String COMPACTION_TIME_DESC =
    "Histogram for the time in millis for compaction, both major and minor";
  String COMPACTION_INPUT_FILE_COUNT = "compactionInputFileCount";
  String COMPACTION_INPUT_FILE_COUNT_DESC =
    "Histogram for the compaction input number of files, both major and minor";
  String COMPACTION_INPUT_SIZE = "compactionInputSize";
  String COMPACTION_INPUT_SIZE_DESC =
    "Histogram for the compaction total input file sizes, both major and minor";
  String COMPACTION_OUTPUT_FILE_COUNT = "compactionOutputFileCount";
  String COMPACTION_OUTPUT_FILE_COUNT_DESC =
    "Histogram for the compaction output number of files, both major and minor";
  String COMPACTION_OUTPUT_SIZE = "compactionOutputSize";
  String COMPACTION_OUTPUT_SIZE_DESC =
    "Histogram for the compaction total output file sizes, both major and minor";
  String COMPACTED_INPUT_BYTES = "compactedInputBytes";
  String COMPACTED_INPUT_BYTES_DESC =
    "Total number of bytes that is read for compaction, both major and minor";
  String COMPACTED_OUTPUT_BYTES = "compactedOutputBytes";
  String COMPACTED_OUTPUT_BYTES_DESC =
    "Total number of bytes that is output from compaction, both major and minor";

  String MAJOR_COMPACTION_TIME = "majorCompactionTime";
  String MAJOR_COMPACTION_TIME_DESC = "Histogram for the time in millis for compaction, major only";
  String MAJOR_COMPACTION_INPUT_FILE_COUNT = "majorCompactionInputFileCount";
  String MAJOR_COMPACTION_INPUT_FILE_COUNT_DESC =
    "Histogram for the compaction input number of files, major only";
  String MAJOR_COMPACTION_INPUT_SIZE = "majorCompactionInputSize";
  String MAJOR_COMPACTION_INPUT_SIZE_DESC =
    "Histogram for the compaction total input file sizes, major only";
  String MAJOR_COMPACTION_OUTPUT_FILE_COUNT = "majorCompactionOutputFileCount";
  String MAJOR_COMPACTION_OUTPUT_FILE_COUNT_DESC =
    "Histogram for the compaction output number of files, major only";
  String MAJOR_COMPACTION_OUTPUT_SIZE = "majorCompactionOutputSize";
  String MAJOR_COMPACTION_OUTPUT_SIZE_DESC =
    "Histogram for the compaction total output file sizes, major only";
  String MAJOR_COMPACTED_INPUT_BYTES = "majorCompactedInputBytes";
  String MAJOR_COMPACTED_INPUT_BYTES_DESC =
    "Total number of bytes that is read for compaction, major only";
  String MAJOR_COMPACTED_OUTPUT_BYTES = "majorCompactedOutputBytes";
  String MAJOR_COMPACTED_OUTPUT_BYTES_DESC =
    "Total number of bytes that is output from compaction, major only";

  String RPC_GET_REQUEST_COUNT = "rpcGetRequestCount";
  String RPC_GET_REQUEST_COUNT_DESC = "Number of rpc get requests this RegionServer has answered.";
  String RPC_SCAN_REQUEST_COUNT = "rpcScanRequestCount";
  String RPC_SCAN_REQUEST_COUNT_DESC =
    "Number of rpc scan requests this RegionServer has answered.";
  String RPC_FULL_SCAN_REQUEST_COUNT = "rpcFullScanRequestCount";
  String RPC_FULL_SCAN_REQUEST_COUNT_DESC =
    "Number of rpc scan requests that were possible full region scans.";
  String RPC_MULTI_REQUEST_COUNT = "rpcMultiRequestCount";
  String RPC_MULTI_REQUEST_COUNT_DESC =
    "Number of rpc multi requests this RegionServer has answered.";
  String RPC_MUTATE_REQUEST_COUNT = "rpcMutateRequestCount";
  String RPC_MUTATE_REQUEST_COUNT_DESC =
    "Number of rpc mutation requests this RegionServer has answered.";
  String MAX_STOREFILE_COUNT = "maxStoreFileCount";
  String MAX_STOREFILE_COUNT_DESC = "Max store file count across all regions";
  String AVERAGE_REGION_SIZE = "averageRegionSize";
  String AVERAGE_REGION_SIZE_DESC =
    "Average region size over the RegionServer including memstore and storefile sizes.";

  /** Metrics for {@link org.apache.hadoop.hbase.io.ByteBuffAllocator} **/
  String BYTE_BUFF_ALLOCATOR_HEAP_ALLOCATION_BYTES = "ByteBuffAllocatorHeapAllocationBytes";
  String BYTE_BUFF_ALLOCATOR_HEAP_ALLOCATION_BYTES_DESC =
    "Bytes of heap allocation from ByteBuffAllocator";
  String BYTE_BUFF_ALLOCATOR_POOL_ALLOCATION_BYTES = "ByteBuffAllocatorPoolAllocationBytes";
  String BYTE_BUFF_ALLOCATOR_POOL_ALLOCATION_BYTES_DESC =
    "Bytes of pool allocation from ByteBuffAllocator";
  String BYTE_BUFF_ALLOCATOR_HEAP_ALLOCATION_RATIO = "ByteBuffAllocatorHeapAllocationRatio";
  String BYTE_BUFF_ALLOCATOR_HEAP_ALLOCATION_RATIO_DESC =
    "Ratio of heap allocation from ByteBuffAllocator, means heapAllocation/totalAllocation";
  String BYTE_BUFF_ALLOCATOR_TOTAL_BUFFER_COUNT = "ByteBuffAllocatorTotalBufferCount";
  String BYTE_BUFF_ALLOCATOR_TOTAL_BUFFER_COUNT_DESC = "Total buffer count in ByteBuffAllocator";
  String BYTE_BUFF_ALLOCATOR_USED_BUFFER_COUNT = "ByteBuffAllocatorUsedBufferCount";
  String BYTE_BUFF_ALLOCATOR_USED_BUFFER_COUNT_DESC = "Used buffer count in ByteBuffAllocator";

  String ACTIVE_SCANNERS = "activeScanners";
  String ACTIVE_SCANNERS_DESC = "Gauge of currently active scanners";

  String SCANNER_LEASE_EXPIRED_COUNT = "scannerLeaseExpiredCount";
  String SCANNER_LEASE_EXPIRED_COUNT_DESC =
    "Count of scanners which were expired due to scanner lease timeout";
}
