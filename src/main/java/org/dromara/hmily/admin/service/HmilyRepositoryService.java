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

package org.dromara.hmily.admin.service;

import java.util.List;

import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;

/**
 * The interface Hmily repository service.
 */
public interface HmilyRepositoryService {
    
    /**
     * Init.
     *
     * @param properties the properties
     */
    void init(HmilyAdminProperties properties);
    
    /**
     * acquired {@linkplain HmilyTransactionDTO} by page.
     *
     * @param query {@linkplain RepositoryQuery}
     * @return CommonPager TransactionRecoverVO
     */
    CommonPager<HmilyTransactionDTO> listByPage(RepositoryQuery query);
    
    /**
     * Batch remove hmily participant boolean.
     *
     * @param participantIds the participant ids
     * @return the boolean
     */
    Boolean batchRemoveHmilyParticipant(List<Long> participantIds);
    
    /**
     * Update hmily participant retry boolean.
     *
     * @param participantId the participant ids
     * @param retry          the retry
     * @return the boolean
     */
    Boolean updateHmilyParticipantRetry(Long participantId, Integer retry);
    
    /**
     * getCompensationInfo by participantId.
     *
     * @param participantId participantId.
     * @return the string
     */
    StringBuilder getCompensationInfo(Long participantId);
    
}
