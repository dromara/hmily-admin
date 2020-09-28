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

import org.dromara.hmily.admin.service.HmilyRepositoryService;
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
public class MongoHmilyRepositoryServiceTest {
    
    @Autowired
    private HmilyRepositoryService hmilyRepositoryService;
    
    @Test
    public void batchRemoveHmilyParticipant() {
        List<Long> list = new LinkedList<>();
        long participantId = 1L;
        list.add(participantId);
        Boolean success = hmilyRepositoryService.batchRemoveHmilyParticipant(list);
    }
    
    @Test
    public void updateHmilyParticipantRetry() {
        long participantId = 4753590803514232832L;
        Integer retry = 5;
        Boolean success = hmilyRepositoryService.updateHmilyParticipantRetry(participantId,retry);
    }
    
    @Test
    public void getCompensationInfo() {
        long participantId = 4753590803514232832L;
        StringBuilder stringBuilder = hmilyRepositoryService.getCompensationInfo(participantId);
        System.out.println(stringBuilder.toString());
    }
    

}