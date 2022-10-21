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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyDatabaseProperties;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.dromara.hmily.admin.enums.HmilyActionEnum;
import org.dromara.hmily.admin.helper.ByteAndHexadecimalHelper;
import org.dromara.hmily.admin.helper.PageHelper;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.page.PageParameter;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractHmilyRepositoryService implements HmilyRepositoryService {
    
    /**
     * jdbcTemplate.
     * */
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();
    
    /**
     * The constant SELECT_HMILY_TRANSACTION_COMMON.
     */
    private static final String SELECT_HMILY_TRANSACTION_COMMON = "select trans_id, app_name, status, trans_type, retry, version, update_time, create_time from hmily_transaction_global ";
    
    /**
     * The constant SELECTOR_HMILY_PARTICIPANT_COMMON.
     */
    private static final String SELECT_HMILY_PARTICIPANT_COMMON = "select participant_id, participant_ref_id, trans_id, trans_type, status, app_name,"
            + "role, retry, target_class, target_method, confirm_method, cancel_method, version, update_time, create_time from hmily_transaction_participant ";
    
    /**
     * The constant SELECT_COUNT_HMILY_TRANSACTION.
     */
    private static final String SELECT_COUNT_HMILY_TRANSACTION = "select count(1) from hmily_transaction_global where 1 = 1";
    
    /**
     * The constant DELETE_HMILY_PARTICIPANT_COMMON.
     */
    private static final String DELETE_HMILY_PARTICIPANT_COMMON = "delete from hmily_transaction_participant";
    
    /**
     * The constant UPDATE_HMILY_PARTICIPANT_RETYR.
     */
    private static final String UPDATE_HMILY_PARTICIPANT_RETYR = "update hmily_transaction_participant set retry = ? where participant_id = ?";
    
    /**
     * The constant UPDATE_HMILY_PARTICIPANT_STATUS.
     */
    private static final String UPDATE_HMILY_PARTICIPANT_STATUS = "update hmily_transaction_participant set status = ? where participant_id = ?";
    
    /**
     * The constant SELECTOR_HMILY_PARTICIPANT_COMMON_WITH_INVOCATION.
     */
    private static final String SELECT_HMILY_PARTICIPANT_COMMON_WITH_INVOCATION = "select participant_id, participant_ref_id, trans_id, trans_type, status, app_name,"
            + "role, retry, target_class, target_method, confirm_method, cancel_method, confirm_invocation, cancel_invocation, version, update_time, create_time from hmily_transaction_participant ";
    
    /**
     * Bulid  query sql for HmilyTransaction by different database.
     *
     * @param sql sql
     * @param pageParameter pageParameter
     * @return query sql with page limit
     * */
    protected abstract String bulidSqlByPage(String sql, PageParameter pageParameter);
    
    /**
     * Bulid query time condition.
     *
     * @param time time
     * @return time query condition
     * */
    protected abstract String buildTimeQueryCondition(String time);
    
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
            jdbcTemplate.setDataSource(hikariDataSource);
        } catch (Exception ex) {
            log.error("hmily jdbc log init exception please check config:{}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public CommonPager<HmilyTransactionDTO> listByPage(final RepositoryQuery query) {
        String querySql = bulidQuerySqlHmilyTransaction(query);
        String querySqlByPage = bulidSqlByPage(querySql, query.getPageParameter());
        CommonPager<HmilyTransactionDTO> pager = new CommonPager<>();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(querySqlByPage);
        if (!mapList.isEmpty()) {
            pager.setDataList(mapList.stream().map(this::buildTransactionByMap).collect(Collectors.toList()));
            pager.getDataList().forEach(o -> {
                String queryParticipants = SELECT_HMILY_PARTICIPANT_COMMON + "where trans_id = " + o.getTransId();
                List<Map<String, Object>> participantsMapList = jdbcTemplate.queryForList(queryParticipants);
                if (!participantsMapList.isEmpty()) {
                    o.setParticipantDTOS(participantsMapList.stream().map(this::buildParticipantByMap).collect(Collectors.toList()));
                }
            });
        }
        String queryConditions = fillQueryConditions(query).toString();
        final Integer totalCount =
                jdbcTemplate.queryForObject(SELECT_COUNT_HMILY_TRANSACTION + queryConditions, Integer.class);
        if (Objects.nonNull(totalCount)) {
            pager.setPage(PageHelper.buildPage(query.getPageParameter(), totalCount));
        }
        return pager;
    }
    
    @Override
    public Boolean batchRemoveHmilyParticipant(final List<Long> participantIds) {
        if (participantIds.isEmpty()) {
            return Boolean.FALSE;
        }
        StringBuilder deleteSqlByParticipantIds = new StringBuilder();
        deleteSqlByParticipantIds.append(DELETE_HMILY_PARTICIPANT_COMMON);
        deleteSqlByParticipantIds.append(" where participant_id in (");
        for (Long o : participantIds) {
            deleteSqlByParticipantIds.append(o + ",");
        }
        deleteSqlByParticipantIds.deleteCharAt(deleteSqlByParticipantIds.length() - 1);
        deleteSqlByParticipantIds.append(")");
        jdbcTemplate.execute(deleteSqlByParticipantIds.toString());
        return Boolean.TRUE;
    }
    
    @Override
    public Boolean updateHmilyParticipantRetry(final Long participantId, final Integer retry) {
        if (null == retry || null == participantId) {
            return Boolean.FALSE;
        }
        String updateRetry = UPDATE_HMILY_PARTICIPANT_RETYR.replaceFirst("\\?", retry.toString()).replaceFirst("\\?", participantId.toString());
        jdbcTemplate.execute(updateRetry);
        StringBuilder updateStatus = new StringBuilder();
        updateStatus.append(UPDATE_HMILY_PARTICIPANT_STATUS.replaceFirst("\\?", String.valueOf(HmilyActionEnum.PRE_TRY.getCode())).replaceFirst("\\?", participantId.toString()));
        updateStatus.append(" and status = " + HmilyActionEnum.DEATH.getCode());
        jdbcTemplate.execute(updateStatus.toString());
        return Boolean.TRUE;
    }
    
    @Override
    public StringBuilder getCompensationInfo(final Long participantId) {
        StringBuilder compensationInfo = new StringBuilder();
        StringBuilder querySql = new StringBuilder();
        querySql.append(SELECT_HMILY_PARTICIPANT_COMMON_WITH_INVOCATION);
        querySql.append(" where participant_id = " + participantId);
        Map<String, Object> map = jdbcTemplate.queryForMap(querySql.toString());
        if (map.isEmpty()) {
            return compensationInfo.append("No such data is stored in the repository");
        }
        HmilyParticipantDTO hmilyParticipantDTO = buildParticipantByMap(map);
        StringBuilder queryTransSql = new StringBuilder();
        queryTransSql.append(SELECT_HMILY_TRANSACTION_COMMON);
        queryTransSql.append(" where trans_id = " + hmilyParticipantDTO.getTransId());
        Map<String, Object> hmilyTransactionDTOMap = jdbcTemplate.queryForMap(queryTransSql.toString());
        HmilyTransactionDTO hmilyTransactionDTO = buildTransactionByMap(hmilyTransactionDTOMap);
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
    
    private String bulidQuerySqlHmilyTransaction(final RepositoryQuery query) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SELECT_HMILY_TRANSACTION_COMMON);
        sqlBuilder.append(" where 1 = 1");
        StringBuilder queryConditions = fillQueryConditions(query);
        sqlBuilder.append(queryConditions);
        return sqlBuilder.toString();
    }
    
    private StringBuilder fillQueryConditions(final RepositoryQuery query) {
        StringBuilder queryConditions = new StringBuilder("");
        if (null != query.getTransId() && StringUtils.isNotBlank(query.getTransId().toString())) {
            queryConditions.append(" and trans_id = " + query.getTransId());
        }
        if (null != query.getAppName() && StringUtils.isNotBlank(query.getAppName())) {
            queryConditions.append(" and app_name = '" + query.getAppName() + "'");
        }
        if (null != query.getTransType() && StringUtils.isNotBlank(query.getTransType())) {
            queryConditions.append(" and trans_type = '" + query.getTransType().toUpperCase() + "'");
        }
        if (null != query.getRetry() && StringUtils.isNotBlank(query.getRetry().toString())) {
            queryConditions.append(" and retry = " + query.getRetry());
        }
        if (null != query.getStatus() && StringUtils.isNotBlank(query.getStatus())) {
            queryConditions.append(" and status = " + HmilyActionEnum.valueOf(query.getStatus()).getCode());
        }
        if (null != query.getCreateTime() && StringUtils.isNotBlank(query.getCreateTime())) {
            queryConditions.append(" and create_time > " + buildTimeQueryCondition(query.getCreateTime()));
        }
        if (null != query.getUpdateTime() && StringUtils.isNotBlank(query.getUpdateTime())) {
            queryConditions.append(" and update_time < " + buildTimeQueryCondition(query.getUpdateTime()));
        }
        return queryConditions;
    }
    
    private HmilyTransactionDTO buildTransactionByMap(final Map<String, Object> map) {
        HmilyTransactionDTO vo = new HmilyTransactionDTO();
        vo.setTransId((Long) map.get("trans_id"));
        vo.setAppName((String) map.get("app_name"));
        vo.setCreateTime(String.valueOf(map.get("create_time")));
        vo.setUpdateTime(String.valueOf(map.get("update_time")));
        vo.setVersion((Integer) map.get("version"));
        vo.setStatus((Integer) map.get("status"));
        vo.setTransType((String) map.get("trans_type"));
        return vo;
    }
    
    private HmilyParticipantDTO buildParticipantByMap(final Map<String, Object> map) {
        HmilyParticipantDTO vo = new HmilyParticipantDTO();
        vo.setTransId((Long) map.get("trans_id"));
        vo.setAppName((String) map.get("app_name"));
        vo.setCreateTime(String.valueOf(map.get("create_time")));
        vo.setUpdateTime(String.valueOf(map.get("update_time")));
        vo.setVersion((Integer) map.get("version"));
        vo.setStatus((Integer) map.get("status"));
        vo.setTransType((String) map.get("trans_type"));
        vo.setCancelMethod((String) map.get("cancel_method"));
        vo.setConfirmMethod((String) map.get("confirm_method"));
        vo.setTargetClass((String) map.get("target_class"));
        vo.setTargetMethod((String) map.get("target_class"));
        if (Objects.nonNull(map.get("confirm_invocation"))) {
            byte[] confirmInvocation = (byte[]) map.get("confirm_invocation");
            String confirmHmilyInvocation = ByteAndHexadecimalHelper.byte2Hex(confirmInvocation);
            vo.setConfirmHmilyInvocation(confirmHmilyInvocation);
        }
        if (Objects.nonNull(map.get("cancel_invocation"))) {
            byte[] cancelInvocation = (byte[]) map.get("cancel_invocation");
            String cancelHmilyInvocation = ByteAndHexadecimalHelper.byte2Hex(cancelInvocation);
            vo.setCancelHmilyInvocation(cancelHmilyInvocation);
        }
        vo.setRole((Integer) map.get("role"));
        vo.setRetry((Integer) map.get("retry"));
        vo.setParticipantId((Long) map.get("participant_id"));
        vo.setParticipantRefId((Long) map.get("participant_ref_id"));
        return vo;
    }
}
