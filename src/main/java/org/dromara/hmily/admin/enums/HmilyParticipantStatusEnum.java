package org.dromara.hmily.admin.enums;

/**
 * HmilyTransactionStatusEnum
 *
 * @author zhangwanjie3
 */
public enum HmilyParticipantStatusEnum {
    
    /**
     * TCC running with status try、confrim、cancel、delete.
     * */
    RUNNING("0,2,3,4"),
    
    /**
     * TCC suspend process with status try_done.
     * */
    SUSPEND("1"),
    
    /**
     * TCC retrying with status confrim&cancel and version not equal 1.
     * */
    RETRYING("2,3"),
    
    /**
     * TCC retry_exceed with status exceeding the maximum retry number.
     * */
    RETRY_EXCEED("8");
    
    /**
     * status.
     * */
    private String status;
    
    /**
     * constructor by param.
     * */
    HmilyParticipantStatusEnum(final String status) {
        this.status = status;
    }
    
    /**
     * get status.
     * */
    public String getStatus() {
        return status;
    }
    
    /**
     * get HmilyParticipantStatusEnum by status
     * */
    public static HmilyParticipantStatusEnum getStatusEnumByStatus(final Integer status){
        if(null == status){
            return null;
        }
        String statusString = String.valueOf(status);
        for (HmilyParticipantStatusEnum o: HmilyParticipantStatusEnum.values()){
            if(o.getStatus().contains(statusString)){
                return o;
            }
        }
        return null;
    }
}
