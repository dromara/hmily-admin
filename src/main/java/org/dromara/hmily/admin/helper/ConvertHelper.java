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

package org.dromara.hmily.admin.helper;

import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.dromara.hmily.admin.enums.HmilyParticipantStatusEnum;
import org.dromara.hmily.admin.enums.HmilyRoleEnum;
import org.dromara.hmily.admin.enums.HmilyTransactionStatusEnum;
import org.dromara.hmily.admin.page.CommonPager;
import org.dromara.hmily.admin.vo.HmilyParticipantVO;
import org.dromara.hmily.admin.vo.HmilyTransactionVO;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ConvertHelper.
 *
 * @author xiaoyu(Myth)
 */
public final class ConvertHelper {
    
    /**
     * conver HmilyTransactionDTO to HmilyTransactionVO.
     * */
    public static CommonPager<HmilyTransactionVO> converTransactionDTOToVO(final CommonPager<HmilyTransactionDTO> pager, final List<Map<String, Object>> numList){
        List<HmilyTransactionDTO> transactionDTOS = pager.getDataList();
        CommonPager<HmilyTransactionVO> transactionVOCommonPager = new CommonPager<>();
        if(null == transactionDTOS){
            transactionVOCommonPager.setPage(pager.getPage());
            return transactionVOCommonPager;
        }
        List<HmilyTransactionVO> transactionVOS = new LinkedList<>();
        Map<String, String> participantsNumMap = new LinkedHashMap<>();
        numList.forEach(
                o-> participantsNumMap.put(o.get("trans_id").toString(), o.get("count(1)").toString())
        );
        transactionDTOS.forEach(o->transactionVOS.add(bulidHmilyTransactionVO(o, participantsNumMap)));
        transactionVOCommonPager.setPage(pager.getPage());
        transactionVOCommonPager.setDataList(transactionVOS);
        return transactionVOCommonPager;
    }
    
    /**
     * conver HmilyParticipantDTO to HmilyParticipantVO.
     * */
    public static CommonPager<HmilyParticipantVO> converParticipantDTOToVO(final CommonPager<HmilyParticipantDTO> pager){
        List<HmilyParticipantDTO> participantDTOS = pager.getDataList();
        CommonPager<HmilyParticipantVO> participantVOCommonPager = new CommonPager<>();
        if(null == participantDTOS){
            participantVOCommonPager.setPage(pager.getPage());
            return participantVOCommonPager;
        }
        List<HmilyParticipantVO> participantVOS = new LinkedList<>();
        participantDTOS.forEach(o->participantVOS.add(bulidHmilyParticipantVO(o)));
        participantVOCommonPager.setPage(pager.getPage());
        participantVOCommonPager.setDataList(participantVOS);
        return participantVOCommonPager;
    }
    
    private static HmilyTransactionVO bulidHmilyTransactionVO(final HmilyTransactionDTO hmilyTransactionDTO, final Map<String, String> participantsNumMap){
        HmilyTransactionVO hmilyTransactionVO = new HmilyTransactionVO();
        hmilyTransactionVO.setTransType(hmilyTransactionDTO.getTransType());
        hmilyTransactionVO.setTransId(hmilyTransactionDTO.getTransId());
        hmilyTransactionVO.setAppName(hmilyTransactionDTO.getAppName());
        HmilyTransactionStatusEnum  statusEnum = HmilyTransactionStatusEnum.getStatusEnumByStatus(hmilyTransactionDTO.getStatus());
        if(null != statusEnum){
            hmilyTransactionVO.setStatus(statusEnum.name());
        }
        if(null != participantsNumMap.get(hmilyTransactionDTO.getTransId().toString())){
            hmilyTransactionVO.setParticipationsNum(Integer.parseInt(participantsNumMap.get(hmilyTransactionDTO.getTransId().toString())));
        }else {
            hmilyTransactionVO.setParticipationsNum(0);
        }
        hmilyTransactionVO.setCreateTime(hmilyTransactionDTO.getCreateTime());
        hmilyTransactionVO.setUpdateTime(hmilyTransactionDTO.getUpdateTime());
        return hmilyTransactionVO;
    }
    
    private static HmilyParticipantVO bulidHmilyParticipantVO(final HmilyParticipantDTO hmilyParticipantDTO){
        HmilyParticipantVO hmilyParticipantVO = new HmilyParticipantVO();
        hmilyParticipantVO.setRetry(hmilyParticipantDTO.getRetry());
        hmilyParticipantVO.setParticipantRefId(hmilyParticipantDTO.getParticipantRefId());
        hmilyParticipantVO.setParticipantId(hmilyParticipantDTO.getParticipantId());
        hmilyParticipantVO.setTargetMethod(hmilyParticipantDTO.getTargetMethod());
        hmilyParticipantVO.setTargetClass(hmilyParticipantDTO.getTargetClass());
        hmilyParticipantVO.setConfirmMethod(hmilyParticipantDTO.getConfirmMethod());
        hmilyParticipantVO.setCancelMethod(hmilyParticipantDTO.getCancelMethod());
        hmilyParticipantVO.setAppName(hmilyParticipantDTO.getAppName());
        hmilyParticipantVO.setTransId(hmilyParticipantDTO.getTransId());
        hmilyParticipantVO.setTransType(hmilyParticipantDTO.getTransType());
        hmilyParticipantVO.setVersion(hmilyParticipantDTO.getVersion());
        HmilyParticipantStatusEnum statusEnum = HmilyParticipantStatusEnum.getStatusEnumByStatus(hmilyParticipantDTO.getStatus());
        if(null != statusEnum){
            if(statusEnum.equals(HmilyParticipantStatusEnum.RUNNING) && hmilyParticipantDTO.getVersion() > 1){
                hmilyParticipantVO.setStatus(HmilyParticipantStatusEnum.RETRYING.name());
            }else {
                hmilyParticipantVO.setStatus(statusEnum.name());
            }
        }
        HmilyRoleEnum roleEnum = HmilyRoleEnum.getDescByCode(hmilyParticipantDTO.getRole());
        if(null != roleEnum){
            hmilyParticipantVO.setRole(roleEnum.getDesc());
        }
        hmilyParticipantVO.setCreateTime(hmilyParticipantDTO.getCreateTime());
        hmilyParticipantVO.setUpdateTime(hmilyParticipantDTO.getUpdateTime());
        return hmilyParticipantVO;
    }
}
