package org.dromara.hmily.admin.enums;

/**
 * HmilyTransactionStatusEnum
 *
 * @author zhangwanjie3
 */
public enum HmilyTransactionStatusEnum {
    
    /**
     * TCC transaction running with status try.
     * */
    RUN("0"),
    
    /**
     * TCC sucess  with status confirm.
     * */
    SUCESS("2"),
    
    /**
     * TCC failure with status cancel.
     * */
    FAILURE("3");
    
    /**
     * status.
     * */
    private String status;
    
    /**
     * constructor by param.
     * */
    HmilyTransactionStatusEnum(final String status) {
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
    public static HmilyTransactionStatusEnum getStatusEnumByStatus(final Integer status){
        if(null == status){
            return null;
        }
        for (HmilyTransactionStatusEnum o: HmilyTransactionStatusEnum.values()){
            if(o.getStatus().equals(status.toString())){
                return o;
            }
        }
        return null;
    }
}
