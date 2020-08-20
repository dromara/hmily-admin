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

package org.dromara.hmily.admin.service.repository;

import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.page.PageParameter;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author xiaoyu(Myth)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MysqlHmilyRepositoryServiceTest {
    
    @Autowired
    private HmilyRepositoryService hmilyRepositoryService;

    @Test
    public void listByPageHmilyTransaction() {
        RepositoryQuery query = new RepositoryQuery();
        PageParameter pageParameter = new PageParameter(1,10);
        query.setPageParameter(pageParameter);
        final CommonPager<HmilyTransactionDTO> pager = hmilyRepositoryService.listByPageHmilyTransaction(query);
        Assert.assertNotNull(pager.getDataList());
    }
    
    @Test
    public void listByPageHmilyParticipant() {
        RepositoryQuery query = new RepositoryQuery();
        query.setTransId(1L);
        query.setRetry(1);
        query.setAppName("1");
        PageParameter pageParameter = new PageParameter(1,10);
        query.setPageParameter(pageParameter);
        final CommonPager<HmilyParticipantDTO> pager = hmilyRepositoryService.listByPageHmilyParticipant(query);
        Assert.assertNotNull(pager.getDataList());
    }
    
    @Test
    public void batchRemoveHmilyTransaction() {
        List<Long> list = new LinkedList<>();
        long transactionId = 1L;
        long transactionId1 = 2L;
        list.add(transactionId);
        list.add(transactionId1);
        Boolean success = hmilyRepositoryService.batchRemoveHmilyTransaction(list);
    }
    
    @Test
    public void batchRemoveHmilyParticipant() {
        List<Long> list = new LinkedList<>();
        long participantId = 1L;
        list.add(participantId);
        Boolean success = hmilyRepositoryService.batchRemoveHmilyParticipant(list);
    }
    
    @Test
    public void updateHmilyParticipantRetry() {
        long participantId = 1L;
        Integer retry = 1;
        Boolean success = hmilyRepositoryService.updateHmilyParticipantRetry(participantId,retry);
    }
    
    @Test
    public void queryByTransId() {
        List<Long> list = new LinkedList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        hmilyRepositoryService.queryByTransIds(list);
    }

}