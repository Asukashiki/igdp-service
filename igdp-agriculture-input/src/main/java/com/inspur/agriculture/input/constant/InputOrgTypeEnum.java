package com.inspur.agriculture.input.constant;

/**
 * 机构类型枚举
 *
 * @author system
 */
public enum InputOrgTypeEnum {

    /**
     * 联合社
     */
    UNION("union", "Union"),

    /**
     * 合作社
     */
    COOPERATIVE("cooperative", "Cooperative");

    private final String code;
    private final String desc;

    InputOrgTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InputOrgTypeEnum getByCode(String code) {
        for (InputOrgTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
