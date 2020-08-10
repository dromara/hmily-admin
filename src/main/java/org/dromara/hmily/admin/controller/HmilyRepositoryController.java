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

package org.dromara.hmily.admin.controller;

import org.dromara.hmily.admin.annotation.Permission;
import org.dromara.hmily.admin.dto.BatchDTO;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.result.AjaxResponse;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.vo.HmilyTransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * transaction log rest controller.
 * @author xiaoyu(Myth)
 */
@RestController
@RequestMapping("/repository")
public class HmilyRepositoryController {

    private final HmilyRepositoryService hmilyRepositoryService;

    @Autowired(required = false)
    public HmilyRepositoryController(final HmilyRepositoryService hmilyRepositoryService) {
        this.hmilyRepositoryService = hmilyRepositoryService;
    }

    @Permission
    @PostMapping(value = "/listPage")
    public AjaxResponse listPage(@RequestBody final RepositoryQuery recoverQuery) {
        final CommonPager<HmilyTransactionVO> pager = hmilyRepositoryService.listByPage(recoverQuery);
        return AjaxResponse.success(pager);
    }

    @PostMapping(value = "/batchRemoveHmilyTransaction")
    @Permission
    public AjaxResponse batchRemoveHmilyTransaction(@RequestBody final BatchDTO batchDTO) {
        final Boolean success = hmilyRepositoryService.batchRemoveHmilyTransaction(batchDTO.getIds());
        return AjaxResponse.success(success);
    }
    
    @PostMapping(value = "/batchRemoveHmilyParticipant")
    @Permission
    public AjaxResponse batchRemoveHmilyParticipant(@RequestBody final BatchDTO batchDTO) {
        final Boolean success = hmilyRepositoryService.batchRemoveHmilyParticipant(batchDTO.getIds());
        return AjaxResponse.success(success);
    }

    @PostMapping(value = "/updateHmilyParticipantRetry")
    @Permission
    public AjaxResponse updateHmilyParticipantRetry(@RequestBody final BatchDTO batchDTO) {
       /* if (recoverRetryMax < batchDTO.getRetry()) {
            return AjaxResponse.error("重试次数超过最大设置，请您重新设置！");
        }*/
        final Boolean success = hmilyRepositoryService.updateHmilyParticipantRetry(batchDTO.getId(), batchDTO.getRetry());
        
        return AjaxResponse.success(success);

    }
    

}
