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

package org.dromara.hmily.admin.service.repository.file;

import java.util.List;
import java.util.Map;

import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.spi.Join;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;

/**
 * File impl.
 *
 * @author xiaoyu(Myth)
 */
@Join
public class FileHmilyRepositoryService implements HmilyRepositoryService {
    
    @Override
    public void init(final HmilyAdminProperties properties) {
    
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
    
}