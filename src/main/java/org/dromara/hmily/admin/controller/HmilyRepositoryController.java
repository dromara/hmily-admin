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
import org.dromara.hmily.admin.config.HmilyAdminProperties;
import org.dromara.hmily.admin.dto.BatchDTO;
import org.dromara.hmily.admin.helper.ConvertHelper;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.query.RepositoryQuery;
import org.dromara.hmily.admin.result.AjaxResponse;
import org.dromara.hmily.admin.service.HmilyRepositoryService;
import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.dromara.hmily.admin.vo.HmilyParticipantVO;
import org.dromara.hmily.admin.vo.HmilyTransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * transaction log rest controller.
 * @author xiaoyu(Myth)
 */
@RestController
@RequestMapping("/repository")
public class HmilyRepositoryController {

    private final HmilyRepositoryService hmilyRepositoryService;
    
    @Autowired
    HmilyAdminProperties hmilyAdminProperties;
    
    @Autowired(required = false)
    public HmilyRepositoryController(final HmilyRepositoryService hmilyRepositoryService) {
        this.hmilyRepositoryService = hmilyRepositoryService;
    }

    @Permission
    @GetMapping(value = "/listPageHmilyTransaction")
    public AjaxResponse listPageHmilyTransaction(@RequestBody final RepositoryQuery recoverQuery) {
        final CommonPager<HmilyTransactionDTO> pager = hmilyRepositoryService.listByPageHmilyTransaction(recoverQuery);
        CommonPager<HmilyTransactionVO>  pagerConvered = new CommonPager<>();
        if(null != pager.getDataList()){
            List<Long> transIds = pager.getDataList().stream().map(HmilyTransactionDTO::getTransId).collect(Collectors.toList());
            List<Map<String, Object>> numList = hmilyRepositoryService.queryByTransIds(transIds);
            pagerConvered = ConvertHelper.converTransactionDTOToVO(pager, numList);
        }
        pagerConvered.setPage(pager.getPage());
        return AjaxResponse.success(pagerConvered);
    }
    
    @Permission
    @GetMapping(value = "/listPageHmilyParticipant")
    public AjaxResponse listPageHmilyParticipant(@RequestBody final RepositoryQuery recoverQuery) {
        final CommonPager<HmilyParticipantDTO> pager = hmilyRepositoryService.listByPageHmilyParticipant(recoverQuery);
        CommonPager<HmilyParticipantVO> pagerConvered = ConvertHelper.converParticipantDTOToVO(pager);
        return AjaxResponse.success(pagerConvered);
    }
    
    @DeleteMapping(value = "/batchRemoveHmilyTransaction")
    @Permission
    public AjaxResponse batchRemoveHmilyTransaction(@RequestBody final BatchDTO batchDTO) {
        if(batchDTO.getIds().isEmpty()){
            return AjaxResponse.error("TransIds should not empty");
        }
        final Boolean success = hmilyRepositoryService.batchRemoveHmilyTransaction(batchDTO.getIds());
        return AjaxResponse.success(success);
    }
    
    @DeleteMapping(value = "/batchRemoveHmilyParticipant")
    @Permission
    public AjaxResponse batchRemoveHmilyParticipant(@RequestBody final BatchDTO batchDTO) {
        if(batchDTO.getIds().isEmpty()){
            return AjaxResponse.error("ParticipantIds should not empty");
        }
        final Boolean success = hmilyRepositoryService. batchRemoveHmilyParticipant(batchDTO.getIds());
        return AjaxResponse.success(success);
    }

    @PostMapping(value = "/updateHmilyParticipantRetry")
    @Permission
    public AjaxResponse updateHmilyParticipantRetry(@RequestBody final BatchDTO batchDTO) {
        if(null == batchDTO.getId() || null == batchDTO.getRetry()){
            return AjaxResponse.error("Info is not enough, please check it");
        }
        if (hmilyAdminProperties.getRetryMax() < batchDTO.getRetry()) {
            return AjaxResponse.error("The number of retries exceeds the maximum setting, please reset！");
        }
        final Boolean success = hmilyRepositoryService.updateHmilyParticipantRetry(batchDTO.getId(), batchDTO.getRetry());
        return AjaxResponse.success(success);
    }
    

}
