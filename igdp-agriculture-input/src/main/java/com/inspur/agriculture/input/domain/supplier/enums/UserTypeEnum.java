package com.inspur.agriculture.input.domain.supplier.enums;

/**
 * 用户类型枚举
 *
 * @author igdp
 */
public enum UserTypeEnum {

    /** 普通用户 */
    NORMAL_USER(1, "普通用户"),

    /** 供应商用户 */
    SUPPLIER_USER(2, "供应商用户"),

    /** 审核人员 */
    AUDITOR(3, "审核人员");

    private final Integer code;
    private final String desc;

    UserTypeEnum(Integer code, String desc) {
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
        for (UserTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
