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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.ReflectionUtils;
import org.apache.yetus.audience.InterfaceAudience;

/**
 * Factory class to get the instance of configured connection registry.
 */
@InterfaceAudience.Private
final class ConnectionRegistryFactory {

  private ConnectionRegistryFactory() {
  }

  /** Returns The connection registry implementation to use. */
  static ConnectionRegistry getRegistry(Configuration conf) {
    Class<? extends ConnectionRegistry> clazz =
      conf.getClass(CLIENT_CONNECTION_REGISTRY_IMPL_CONF_KEY, ZKConnectionRegistry.class,
        ConnectionRegistry.class);
    return ReflectionUtils.newInstance(clazz, conf);
  }
}
