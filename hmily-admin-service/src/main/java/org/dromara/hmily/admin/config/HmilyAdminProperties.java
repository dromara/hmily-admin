/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hmily.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Hmily admin properties.
 *
 * @author xiaoyu
 */
@Data
@ConfigurationProperties(prefix = "hmily.admin")
public class HmilyAdminProperties {
    
    private Integer retryMax = 10;
    
    /**
     * repository.
     */
    private String repository = "mysql";
    
    /**
     * db config.
     */
    private HmilyDatabaseProperties hmilyDbConfig;
    
    /**
     * mongo config.
     */
    private HmilyMongoProperties hmilyMongoConfig;
    
    /**
     * redis config.
     */
    private HmilyRedisProperties hmilyRedisConfig;
    
    /**
     * zookeeper config.
     */
    private HmilyZookeeperProperties hmilyZookeeperConfig;
    
    /**
     * file config.
     */
    private HmilyFileProperties hmilyFileConfig;
    
}
