/**
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
package org.apache.hadoop.hbase.regionserver.compactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.regionserver.HStoreFile;

/**
 * Class to generate several lists of StoreFiles that are all the same size.
 */
class ConstantSizeFileListGenerator extends StoreFileListGenerator {

  /** How many mb's mock storefiles should be. */
  private static final int FILESIZE = 5;

  @Override
  public final Iterator<List<HStoreFile>> iterator() {
    return new Iterator<List<HStoreFile>>() {
      private int count = 0;

      @Override
      public boolean hasNext() {
        return count < MAX_FILE_GEN_ITERS;
      }

      @Override
      public List<HStoreFile> next() {
        count += 1;
        ArrayList<HStoreFile> files = new ArrayList<>(NUM_FILES_GEN);
        for (int i = 0; i < NUM_FILES_GEN; i++) {
          files.add(createMockStoreFile(FILESIZE));
        }
        return files;
      }

      @Override
      public void remove() {

      }
    };
  }

}
