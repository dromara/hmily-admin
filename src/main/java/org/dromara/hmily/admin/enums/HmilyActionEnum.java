package org.dromara.hmily.admin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * The enum hmily action enum.
 *
 * @author xiaoyu
 */
@RequiredArgsConstructor
@Getter
public enum HmilyActionEnum {
    
    /**
     * Pre try tcc action enum.
     */
    PRE_TRY(0, "开始执行try"),
    
    /**
     * Trying tcc action enum.
     */
    TRYING(1, "try阶段完成"),
    
    /**
     * Confirming tcc action enum.
     */
    CONFIRMING(2, "confirm阶段"),
    
    /**
     * Canceling tcc action enum.
     */
    CANCELING(3, "cancel阶段"),
    
    /**
     * DELETE hmily action enum.
     */
    DELETE(4, "删除状态"),
    
    /**
     * Death hmily action enum.
     */
    DEATH(8, "超过最大重试次数状态");
    
    private final int code;
    
    private final String desc;
    
    /**
     * Acquire by code tcc action enum.
     *
     * @param code the code
     * @return the tcc action enum
     */
    public static HmilyActionEnum acquireByCode(final int code) {
        return Arrays.stream(HmilyActionEnum.values())
                .filter(v -> Objects.equals(v.getCode(), code))
                .findFirst().orElse(HmilyActionEnum.TRYING);
    }
    
}
