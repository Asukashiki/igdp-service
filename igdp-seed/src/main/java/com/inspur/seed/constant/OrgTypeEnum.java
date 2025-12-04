package com.inspur.seed.constant;

/**
 * 机构类型枚举
 *
 * @author system
 */
public enum OrgTypeEnum {

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

    OrgTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrgTypeEnum getByCode(String code) {
        for (OrgTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
