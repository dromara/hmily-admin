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

import java.io.Serializable;
import lombok.Data;

/**
 * HmilyParticipantDTO.
 *
 * @author xiaoyu(Myth)
 */
@Data
public class HmilyParticipantDTO implements Serializable {
    
    /**
     * participant id.
     */
    private Long participantId;
    
    /**
     * participant ref id.
     */
    private Long participantRefId;
    
    /**
     * transaction id.
     */
    private Long transId;
    
    /**
     * trans type.
     */
    private String transType;
    
    /**
     * transaction status.
     */
    private Integer status;
    
    /**
     * app name.
     */
    private String appName;
    
    /**
     * transaction role .
     */
    private int role;
    
    /**
     * retry.
     */
    private int retry;
    
    /**
     * Call interface name.
     */
    private String targetClass;
    
    /**
     * Call interface method name.
     */
    private String targetMethod;
    
    /**
     * confirm Method.
     */
    private String confirmMethod;
    
    /**
     * cancel Method.
     */
    private String cancelMethod;
    
    /**
     * version.
     */
    private Integer version = 1;
    
    /**
     * createTime.
     */
    private String createTime;
    
    /**
     * updateTime.
     */
    private String updateTime;
}
