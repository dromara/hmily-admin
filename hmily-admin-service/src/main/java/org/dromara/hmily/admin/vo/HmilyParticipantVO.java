package org.dromara.hmily.admin.vo;

import lombok.Data;

/**
 * HmilyParticipantVO.
 *
 * @author zhangwanjie3
 */
@Data
public class HmilyParticipantVO {
    /**
     * participant id.
     */
    private String participantId;
    
    /**
     * participant ref id.
     */
    private String participantRefId;
    
    /**
     * transaction id.
     */
    private String transId;
    
    /**
     * trans type.
     */
    private String transType;
    
    /**
     * transaction status.
     */
    private String status;
    
    /**
     * app name.
     */
    private String appName;
    
    /**
     * transaction role .
     */
    private String role;
    
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
