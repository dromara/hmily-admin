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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.config.HmilyDatabaseProperties;
import org.dromara.hmily.admin.enums.HmilyParticipantStatusEnum;
import org.dromara.hmily.admin.enums.HmilyTransactionStatusEnum;
import org.dromara.hmily.admin.helper.PageHelper;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.page.PageParameter;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public abstract class AbstractHmilyRepositoryService implements HmilyRepositoryService {
    
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate() ;
    
    /**
     * The constant SELECT_HMILY_TRANSACTION_COMMON.
     */
    protected static final String SELECT_HMILY_TRANSACTION_COMMON = "select trans_id, app_name, status, trans_type, retry, version, update_time, create_time from hmily_transaction_global ";
    
    /**
     * The constant SELECTOR_HMILY_PARTICIPANT_COMMON.
     */
    protected static final String SELECTOR_HMILY_PARTICIPANT_COMMON = "select participant_id, participant_ref_id, trans_id, trans_type, status, app_name,"
            + "role, retry, target_class, target_method, confirm_method, cancel_method, confirm_invocation, cancel_invocation, version, update_time, create_time from hmily_transaction_participant ";
    
    /**
     * The constant SELECT_COUNT_HMILY_TRANSACTION.
     */
    protected static final String SELECT_COUNT_HMILY_TRANSACTION = "select count(1) from hmily_transaction_global where 1 = 1";
    
    /**
     * The constant SELECT_COUNT_HMILY_PARTICIPANT.
     */
    protected static final String SELECT_COUNT_HMILY_PARTICIPANT = "select count(1) from hmily_transaction_participant where 1 = 1";
    
    /**
     * The constant DELETE_HMILY_TRANSACTION_COMMON.
     */
    protected static final String DELETE_HMILY_TRANSACTION_COMMON = "delete from hmily_transaction_global";
    
    /**
     * The constant DELETE_HMILY_PARTICIPANT_COMMON.
     */
    protected static final String DELETE_HMILY_PARTICIPANT_COMMON = "delete from hmily_transaction_participant";
    
    /**
     * The constant UPDATE_HMILY_PARTICIPANT_RETYR.
     */
    protected static final String UPDATE_HMILY_PARTICIPANT_RETYR = "update hmily_transaction_participant set retry = ? where participant_id = ?";
    
    /**
     * The constant QUERY_COUNT_PARTICIPANT_BY_TRANSID
     * */
    protected static final String QUERY_COUNT_PARTICIPANT_BY_TRANSID = "select trans_id, count(1) from hmily_transaction_participant  WHERE trans_id in (?) GROUP BY(trans_id);";
   
    /**
     * Bulid  query sql for HmilyTransaction by different database.
     *
     * @param sql sql
     * @param pageParameter pageParameter
     * @return query sql with page limit
     * */
    protected abstract String bulidSqlByPage(String sql, PageParameter pageParameter);
    
    /**
     * Bulid query time condition
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
    public  CommonPager<HmilyTransactionDTO> listByPageHmilyTransaction(final RepositoryQuery query){
        String querySql = bulidQuerySqlHmilyTransaction(query);
        String querySqlByPage = bulidSqlByPage(querySql, query.getPageParameter());
        CommonPager<HmilyTransactionDTO> pager = new CommonPager<>();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(querySqlByPage);
        if (!mapList.isEmpty() ) {
            pager.setDataList(mapList.stream().map(this::buildTransactionByMap).collect(Collectors.toList()));
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
    public  CommonPager<HmilyParticipantDTO> listByPageHmilyParticipant(final RepositoryQuery query){
        String querySql = bulidQuerySqlParticipant(query);
        String querySqlByPage = bulidSqlByPage(querySql, query.getPageParameter());
        List<Map<String, Object>> map = jdbcTemplate.queryForList(querySqlByPage);
        CommonPager<HmilyParticipantDTO> pager = new CommonPager<>();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(querySqlByPage);
        if (!mapList.isEmpty() ) {
            pager.setDataList(mapList.stream().map(this::buildParticipantByMap).collect(Collectors.toList()));
        }
        String queryConditions = fillQueryConditions(query).toString();
        final Integer totalCount =
                jdbcTemplate.queryForObject(SELECT_COUNT_HMILY_PARTICIPANT + queryConditions, Integer.class);
        if (Objects.nonNull(totalCount)) {
            pager.setPage(PageHelper.buildPage(query.getPageParameter(), totalCount));
        }
        return pager;
    }
    
    @Override
    public Boolean batchRemoveHmilyTransaction(final List<Long> transIds) {
        if(transIds.isEmpty()){
            return Boolean.FALSE;
        }
        StringBuilder deleteSqlByTransIds=new StringBuilder();
        deleteSqlByTransIds.append(DELETE_HMILY_TRANSACTION_COMMON);
        deleteSqlByTransIds.append(" where trans_id in (");
        for (Long o : transIds){
            deleteSqlByTransIds.append(o + ",");
        }
        deleteSqlByTransIds.deleteCharAt(deleteSqlByTransIds.length()-1);
        deleteSqlByTransIds.append(")");
        jdbcTemplate.execute(deleteSqlByTransIds.toString());
        return Boolean.TRUE;
    }
    
    @Override
    public Boolean batchRemoveHmilyParticipant(final List<Long> participantIds) {
        if(participantIds.isEmpty()){
            return Boolean.FALSE;
        }
        StringBuilder deleteSqlByParticipantIds=new StringBuilder();
        deleteSqlByParticipantIds.append(DELETE_HMILY_PARTICIPANT_COMMON);
        deleteSqlByParticipantIds.append(" where participant_id in (");
        for (Long o : participantIds){
            deleteSqlByParticipantIds.append(o + ",");
        }
        deleteSqlByParticipantIds.deleteCharAt(deleteSqlByParticipantIds.length()-1);
        deleteSqlByParticipantIds.append(")");
        jdbcTemplate.execute(deleteSqlByParticipantIds.toString());
        return Boolean.TRUE;
    }
    
    @Override
    public Boolean updateHmilyParticipantRetry(final Long participantId, final Integer retry) {
        if(null == retry || null == participantId){
            return Boolean.FALSE;
        }
        String updateSql = UPDATE_HMILY_PARTICIPANT_RETYR.replaceFirst("\\?",retry.toString()).replaceFirst("\\?",participantId.toString());
        jdbcTemplate.execute(updateSql);
        return Boolean.TRUE;
    }
    
    @Override
    public List<Map<String, Object>> queryByTransIds(final List<Long> transIds){
        if(transIds.isEmpty()){
            return new LinkedList<>();
        }
        StringBuilder transIdsString = new StringBuilder();
        transIds.stream().forEach(o->transIdsString.append(o+","));
        String querySql = QUERY_COUNT_PARTICIPANT_BY_TRANSID.replaceFirst("\\?",transIdsString.toString().substring(0,transIdsString.lastIndexOf(",")));
        final List<Map<String, Object>> numList = jdbcTemplate.queryForList(querySql);
        return numList;
    }
    
    private String bulidQuerySqlHmilyTransaction(final RepositoryQuery query){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SELECT_HMILY_TRANSACTION_COMMON);
        sqlBuilder.append(" where 1 = 1");
        StringBuilder queryConditions=fillQueryConditions(query);
        sqlBuilder.append(queryConditions);
        return sqlBuilder.toString();
    }
    
    private String bulidQuerySqlParticipant(final RepositoryQuery query){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(SELECTOR_HMILY_PARTICIPANT_COMMON);
        sqlBuilder.append(" where 1 = 1");
        StringBuilder queryConditions=fillQueryConditions(query);
        sqlBuilder.append(queryConditions);
        return sqlBuilder.toString();
    }
    
    private StringBuilder fillQueryConditions(final RepositoryQuery query)  {
        StringBuilder queryConditions=new StringBuilder("");
        if(null != query.getTransId() && StringUtils.isNotBlank(query.getTransId().toString())){
            queryConditions.append(" and trans_id = " + query.getTransId());
        }
        if(null != query.getAppName() && StringUtils.isNotBlank(query.getAppName())){
            queryConditions.append(" and app_name = '" + query.getAppName() + "'");
        }
        if(null != query.getTransType() && StringUtils.isNotBlank(query.getTransType())){
            queryConditions.append(" and trans_type = '" + query.getTransType().toUpperCase() + "'");
        }
        if(null != query.getRetry() && StringUtils.isNotBlank(query.getRetry().toString())){
            queryConditions.append(" and retry = " + query.getRetry());
        }
        if(null != query.getStatus() && StringUtils.isNotBlank(query.getStatus())){
            String status = getStatusByEnum(query.getStatus());
            if("RETRYING".equals(query.getStatus())){
                queryConditions.append(" and status in (" + status + ") and version > 1 ");
            }else if("RUNNING".equals(query.getStatus())){
                queryConditions.append(" and status in (" + status + ") and version = 1 ");
            }else {
                queryConditions.append(" and status in (" + status + ")");
            }
        }
        if(null != query.getCreateTime() && StringUtils.isNotBlank(query.getCreateTime()) ){
            queryConditions.append(" and create_time > " + buildTimeQueryCondition(query.getCreateTime()));
        }
        if(null != query.getUpdateTime() && StringUtils.isNotBlank(query.getUpdateTime()) ){
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
        vo.setCancelMethod((String)map.get("cancel_method"));
        vo.setConfirmMethod((String)map.get("confirm_method"));
        vo.setTargetClass((String)map.get("target_class"));
        vo.setTargetMethod((String)map.get("target_class"));
        vo.setRole((Integer) map.get("role"));
        vo.setRetry((Integer) map.get("retry"));
        vo.setParticipantId((Long) map.get("participant_id"));
        vo.setParticipantRefId((Long) map.get("participant_ref_id"));
        
        return vo;
    }
    
    private String getStatusByEnum(final String status){
        for (HmilyTransactionStatusEnum o : HmilyTransactionStatusEnum.values()){
            if(o.name().equals(status)){
                return o.getStatus();
            }
        }
        for (HmilyParticipantStatusEnum o : HmilyParticipantStatusEnum.values()){
            if(o.name().equals(status)){
                return o.getStatus();
            }
        }
        return "-1";
    }
}
