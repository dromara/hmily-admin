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

package org.dromara.hmily.admin.service.repository.db;

import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyDatabaseProperties;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.vo.HmilyTransactionVO;

@Slf4j
public abstract class AbstractHmilyRepositoryService implements HmilyRepositoryService {
    
    
    @Override
    public void init(final HmilyAdminProperties properties) {
        try {
            HmilyDatabaseProperties hmilyDbConfig = properties.getHmilyDbConfig();
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(hmilyDbConfig.getUrl());
            hikariDataSource.setDriverClassName(hmilyDbConfig.getDriverClassName());
            hikariDataSource.setUsername(hmilyDbConfig.getUsername());
            hikariDataSource.setPassword(hmilyDbConfig.getPassword());
            hikariDataSource.setMaximumPoolSize(hmilyDbConfig.getMaxActive());
            hikariDataSource.setMinimumIdle(hmilyDbConfig.getMinIdle());
            hikariDataSource.setConnectionTimeout(hmilyDbConfig.getConnectionTimeout());
            hikariDataSource.setIdleTimeout(hmilyDbConfig.getIdleTimeout());
            hikariDataSource.setMaxLifetime(hmilyDbConfig.getMaxLifetime());
            hikariDataSource.setConnectionTestQuery(hmilyDbConfig.getConnectionTestQuery());
            if (hmilyDbConfig.getDataSourcePropertyMap() != null && !hmilyDbConfig.getDataSourcePropertyMap().isEmpty()) {
                hmilyDbConfig.getDataSourcePropertyMap().forEach(hikariDataSource::addDataSourceProperty);
            }
        } catch (Exception e) {
            log.error("hmily jdbc log init exception please check config:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public CommonPager<HmilyTransactionVO> listByPage(final RepositoryQuery query) {
        return null;
    }
    
    @Override
    public Boolean batchRemoveHmilyTransaction(final List<Long> transIds) {
        return null;
    }
    
    @Override
    public Boolean batchRemoveHmilyParticipant(final List<Long> participantIds) {
        return null;
    }
    
    @Override
    public Boolean updateHmilyParticipantRetry(final Long participantIds, final Integer retry) {
        return null;
    }
}
