package com.inspur.agriculture.input.domain.supplier.enums;

/**
 * 账号状态枚举
 *
 * @author igdp
 */
public enum AccountStatusEnum {

    /** 禁用 */
    DISABLED(0, "禁用"),

    /** 正常 */
    NORMAL(1, "正常");

    private final Integer code;
    private final String desc;

    AccountStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (AccountStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "";
    }
}
