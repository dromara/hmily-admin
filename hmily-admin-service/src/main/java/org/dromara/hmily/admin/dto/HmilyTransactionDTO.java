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

package org.dromara.hmily.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmilyTransactionDTO.
 *
 * @author xiaoyu(Myth)
 */
@Data
public class HmilyTransactionDTO implements Serializable {
    
    /**
     * transaction id.
     * */
    private Long transId;
    
    /**
     * app name.
     * */
    private String appName;
    
    /**
     * transaction overall status.
     * */
    private Integer status;
    
    /**
     * transaction type.
     * */
    private String transType;
    
    /**
     * retry.
     * */
    private Integer retry;
    
    /**
     * version.
     * */
    private Integer version;
    
    /**
     * create time.
     * */
    private String createTime;
    
    /**
     * update time.
     * */
    private String updateTime;
    
    /**
     * participant list.
     * */
    private List<HmilyParticipantDTO> participantDTOS;
    
}
