package com.inspur.seed.constant;

/**
 * 审核结果枚举
 *
 * @author system
 */
public enum AuditResultEnum {

    /**
     * 通过
     */
    PASSED("passed", "Passed"),

    /**
     * 驳回
     */
    REJECTED("rejected", "Rejected");

    private final String code;
    private final String desc;

    AuditResultEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AuditResultEnum getByCode(String code) {
        for (AuditResultEnum result : values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        return null;
    }
}
