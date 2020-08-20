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

package org.dromara.hmily.admin.service.repository.zookeeper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyZookeeperProperties;
import org.dromara.hmily.admin.exception.HmilyAdminException;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.spi.Join;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;

/**
 * Zookeeper impl.
 *
 * @author xiaoyu(Myth)
 */
@Join
@Slf4j
public class ZookeeperHmilyRepositoryService implements HmilyRepositoryService {
    
    private static final CountDownLatch LATCH = new CountDownLatch(1);
    
    private static volatile ZooKeeper zooKeeper;
    
    private String rootPathPrefix = "/hmily";
    
    @Override
    public void init(final HmilyAdminProperties properties) {
        try {
            connect(properties.getHmilyZookeeperConfig());
        } catch (Exception e) {
            log.error("zookeeper init error please check you config:{}", e.getMessage());
            throw new HmilyAdminException(e.getMessage());
        }
    }
    
    @Override
    public CommonPager<HmilyTransactionDTO> listByPageHmilyTransaction(final RepositoryQuery query) {
        return null;
    }
    
    @Override
    public CommonPager<HmilyParticipantDTO> listByPageHmilyParticipant(final RepositoryQuery query) {
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
    
    @Override
    public List<Map<String, Object>> queryByTransIds(final List<Long> transIds) {
        return null;
    }
    
    private void connect(final HmilyZookeeperProperties config) {
        try {
            zooKeeper = new ZooKeeper(config.getHost(), config.getSessionTimeOut(), watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    LATCH.countDown();
                }
            });
            LATCH.await();
            Stat stat = zooKeeper.exists(rootPathPrefix, false);
            if (stat == null) {
                zooKeeper.create(rootPathPrefix, rootPathPrefix.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new HmilyAdminException(e);
        }
    }
}
