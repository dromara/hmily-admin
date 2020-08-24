package org.dromara.hmily.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * HmilyTransactionVO.
 *
 * @author zhangwanjie3
 */
@Data
public class HmilyTransactionVO {
    
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
    private String status;
    
    /**
     * transaction type.
     * */
    private String transType;
    
    /**
     * participation number.
     * */
    private Integer participationsNum;
    
    /**
     * create time.
     * */
    private String createTime;
    
    /**
     * update time.
     * */
    private String updateTime;
    
    /**
     *  participants list.
     * */
    private List<HmilyParticipantVO> participantVOS;
}
