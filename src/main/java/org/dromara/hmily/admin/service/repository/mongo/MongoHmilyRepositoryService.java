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

package org.dromara.hmily.admin.service.repository.mongo;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.UpdateResult;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyMongoProperties;
import org.dromara.hmily.admin.exception.HmilyAdminException;
import org.dromara.hmily.admin.helper.PageHelper;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.page.PageParameter;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.spi.Join;
import org.dromara.hmily.admin.vo.HmilyTransactionVO;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

/**
 * Mongodb impl.
 *
 * @author xiaoyu(Myth)
 */
@Join
@Slf4j
public class MongoHmilyRepositoryService implements HmilyRepositoryService {
    
    private MongoTemplate template;
    
    @Override
    public void init(final HmilyAdminProperties properties) {
        final HmilyMongoProperties hmilyMongoConfig = properties.getHmilyMongoConfig();
        MongoClientFactoryBean clientFactoryBean = buildMongoClientFactoryBean(hmilyMongoConfig);
        try {
            clientFactoryBean.afterPropertiesSet();
            template = new MongoTemplate(Objects.requireNonNull(clientFactoryBean.getObject()), hmilyMongoConfig.getMongoDbName());
        } catch (Exception e) {
            log.error("mongo init error please check you config:{}", e.getMessage());
            throw new HmilyAdminException(e);
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
    public Boolean updateHmilyParticipantRetry(Long participantIds, Integer retry) {
        return null;
    }
    
    private MongoClientFactoryBean buildMongoClientFactoryBean(final HmilyMongoProperties hmilyMongoConfig) {
        MongoClientFactoryBean clientFactoryBean = new MongoClientFactoryBean();
        MongoCredential credential = MongoCredential.createScramSha1Credential(hmilyMongoConfig.getMongoUserName(),
                hmilyMongoConfig.getMongoDbName(),
                hmilyMongoConfig.getMongoUserPwd().toCharArray());
        clientFactoryBean.setCredentials(new MongoCredential[]{credential});
        List<String> urls = Lists.newArrayList(Splitter.on(",").trimResults().split(hmilyMongoConfig.getMongoDbUrl()));
        ServerAddress[] sds = new ServerAddress[urls.size()];
        for (int i = 0; i < sds.length; i++) {
            List<String> adds = Lists.newArrayList(Splitter.on(":").trimResults().split(urls.get(i)));
            InetSocketAddress address = new InetSocketAddress(adds.get(0), Integer.parseInt(adds.get(1)));
            sds[i] = new ServerAddress(address);
        }
        clientFactoryBean.setReplicaSetSeeds(sds);
        return clientFactoryBean;
    }

}
