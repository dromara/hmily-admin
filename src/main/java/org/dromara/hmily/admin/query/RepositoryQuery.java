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

package org.dromara.hmily.admin.query;

import lombok.Data;
import org.dromara.hmily.admin.enums.HmilyTransactionStatusEnum;
import org.dromara.hmily.admin.enums.HmilyParticipantStatusEnum;
import org.dromara.hmily.admin.page.PageParameter;

import java.io.Serializable;

/**
 * query condition.
 *
 * @author xiaoyu(Myth)
 */
@Data
public class RepositoryQuery implements Serializable {

    private static final long serialVersionUID = 3297929795348894462L;

    /**
     * app name.
     */
    private String appName;

    /**
     * transId.
     */
    private Long transId;
    
    /**
     * participantRefId.
     */
    private Long participantRefId;
    
    /**
     * transType.
     */
    private String transType;
    
    /**
     * {@linkplain HmilyTransactionStatusEnum}
     * {@linkplain HmilyParticipantStatusEnum}
     */
    private String status;
    
    /**
     * retry.
     */
    private Integer retry;
    
    /**
     * createTime
     * */
    private String createTime;
    
    /**
     * updateTime
     * */
    private String updateTime;

    /**
     * pageParameter.
     */
    private PageParameter pageParameter;


}
