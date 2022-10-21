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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyMongoProperties;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.dromara.hmily.admin.enums.HmilyActionEnum;
import org.dromara.hmily.admin.exception.HmilyAdminException;
import org.dromara.hmily.admin.helper.PageHelper;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.service.repository.mongo.entity.ParticipantMongoEntity;
import org.dromara.hmily.admin.service.repository.mongo.entity.TransactionMongoEntity;
import org.dromara.hmily.admin.spi.Join;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.query.Criteria;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Mongodb impl.
 *
 * @author xiaoyu(Myth)
 */
@Join
@Slf4j
public class MongoHmilyRepositoryService implements HmilyRepositoryService {
    
    private MongodbTemplateService service;
    
    @Override
    public void init(final HmilyAdminProperties properties) {
        final HmilyMongoProperties hmilyMongoConfig = properties.getHmilyMongoConfig();
        MongoClientFactoryBean clientFactoryBean = buildMongoClientFactoryBean(hmilyMongoConfig);
        try {
            clientFactoryBean.afterPropertiesSet();
            service = new MongodbTemplateService(Objects.requireNonNull(clientFactoryBean.getObject()), hmilyMongoConfig.getMongoDbName());
        } catch (Exception e) {
            log.error("mongo init error please check you config:{}", e.getMessage());
            throw new HmilyAdminException(e);
        }
    }
    
    @Override
    public CommonPager<HmilyTransactionDTO> listByPage(final RepositoryQuery query) {
        final List<TransactionMongoEntity> transactionMongoEntitieList = service.find(TransactionMongoEntity.class, fillQueryConditions(query),
                query.getPageParameter().getCurrentPage(), query.getPageParameter().getPageSize());
        CommonPager<HmilyTransactionDTO> pager = new CommonPager<>();
        if (transactionMongoEntitieList.isEmpty()) {
            pager.setPage(PageHelper.buildPage(query.getPageParameter(), 0));
            pager.setDataList(new LinkedList<>());
            return pager;
        }
        List<HmilyTransactionDTO> hmilyTransactionDTOS = new LinkedList<>();
        transactionMongoEntitieList.forEach(o -> {
            HmilyTransactionDTO hmilyTransactionDTO = MongoEntityConvert.convert(o);
            final List<ParticipantMongoEntity> participantMongoEntityList = service.find(ParticipantMongoEntity.class, Criteria.where("trans_id").is(o.getTransId()));
            List<HmilyParticipantDTO> participantDTOList = new LinkedList<>();
            if (null != participantMongoEntityList && !participantMongoEntityList.isEmpty()) {
                participantMongoEntityList.forEach(oo -> participantDTOList.add(MongoEntityConvert.convert(oo)));
            }
            hmilyTransactionDTO.setParticipantDTOS(participantDTOList);
            hmilyTransactionDTOS.add(hmilyTransactionDTO);
        });
        pager.setDataList(hmilyTransactionDTOS);
        final int totalCount = service.count(TransactionMongoEntity.class, fillQueryConditions(query));
        pager.setPage(PageHelper.buildPage(query.getPageParameter(), totalCount));
        return pager;
    }
    
    @Override
    public Boolean batchRemoveHmilyParticipant(final List<Long> participantIds) {
        if (participantIds.isEmpty()) {
            return Boolean.FALSE;
        }
        service.delete(ParticipantMongoEntity.class,
                Criteria.where("participant_id").in(participantIds));
        return Boolean.TRUE;
    }
    
    @Override
    public Boolean updateHmilyParticipantRetry(final Long participantId, final Integer retry) {
        if (null == retry || null == participantId) {
            return Boolean.FALSE;
        }
        Pair<String, Object> newDataPair = Pair.of("retry", retry);
        final int update = service.update(ParticipantMongoEntity.class,
                Criteria.where("participant_id").is(participantId), newDataPair);
        if (update != 1) {
            return Boolean.FALSE;
        }
        Pair<String, Object> statusPair = Pair.of("status", HmilyActionEnum.PRE_TRY.getCode());
        final int statusUpdate = service.update(ParticipantMongoEntity.class,
                Criteria.where("participant_id").is(participantId)
                        .and("status").is(HmilyActionEnum.DEATH.getCode()), statusPair);
        return Boolean.TRUE;
    }
    
    @Override
    public StringBuilder getCompensationInfo(final Long participantId) {
        StringBuilder compensationInfo = new StringBuilder();
        final List<ParticipantMongoEntity> participantMongoEntityList = service.find(ParticipantMongoEntity.class,
                Criteria.where("participant_id").is(participantId));
        if (participantMongoEntityList.isEmpty()) {
            return compensationInfo.append("No such data is stored in the repository");
        }
        HmilyParticipantDTO hmilyParticipantDTO = MongoEntityConvert.convert(participantMongoEntityList.get(0));
        final List<TransactionMongoEntity> transactionMongoEntityList = service.find(TransactionMongoEntity.class,
                Criteria.where("transaction_id").is(hmilyParticipantDTO.getTransId()));
        if (transactionMongoEntityList.isEmpty()) {
            compensationInfo.append("invocation: { " + hmilyParticipantDTO.getCancelHmilyInvocation() + " },");
            compensationInfo.append("method: { " + hmilyParticipantDTO.getCancelMethod() + " }");
            return compensationInfo;
        }
        HmilyTransactionDTO hmilyTransactionDTO = MongoEntityConvert.convert(transactionMongoEntityList.get(0));
        if (null == hmilyTransactionDTO
                || hmilyTransactionDTO.getStatus() == HmilyActionEnum.CANCELING.getCode()) {
            compensationInfo.append("invocation: { " + hmilyParticipantDTO.getCancelHmilyInvocation() + " },");
            compensationInfo.append("method: { " + hmilyParticipantDTO.getCancelMethod() + " }");
        } else if (hmilyTransactionDTO.getStatus() == HmilyActionEnum.CONFIRMING.getCode()) {
            compensationInfo.append("invocation: { " + hmilyParticipantDTO.getConfirmHmilyInvocation() + " }");
            compensationInfo.append("method: { " + hmilyParticipantDTO.getConfirmMethod() + " }");
        } else {
            compensationInfo.append("No manual compensation is required for global transaction execution");
        }
        return compensationInfo;
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
    
    private Criteria fillQueryConditions(final RepositoryQuery query) {
        Criteria criteria = Criteria.where("_id").exists(true);
        if (null != query.getTransId() && StringUtils.isNotBlank(query.getTransId().toString())) {
            criteria.and("trans_id").is(query.getTransId());
        }
        if (null != query.getAppName() && StringUtils.isNotBlank(query.getAppName())) {
            criteria.and("app_name").is(query.getAppName());
        }
        if (null != query.getTransType() && StringUtils.isNotBlank(query.getTransType())) {
            criteria.and("trans_type").is(query.getTransType());
        }
        if (null != query.getRetry() && StringUtils.isNotBlank(query.getRetry().toString())) {
            criteria.and("retry").is(query.getRetry());
        }
        if (null != query.getStatus() && StringUtils.isNotBlank(query.getStatus())) {
            criteria.and("status").is(HmilyActionEnum.valueOf(query.getStatus()).getCode());
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != query.getCreateTime() && StringUtils.isNotBlank(query.getCreateTime())) {
                criteria.and("create_time").gte(simpleDateFormat.parse(query.getCreateTime()));
            }
            if (null != query.getUpdateTime() && StringUtils.isNotBlank(query.getUpdateTime())) {
                criteria.and("update_time").lte(simpleDateFormat.parse(query.getUpdateTime()));
            }
        } catch (ParseException ex) {
            log.error("查询条件， 转化时间格式错误， 错误信息如下：" + ex.getMessage());
        }
        return criteria;
    }
    
}
