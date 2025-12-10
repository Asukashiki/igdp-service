package com.inspur.agriculture.input.constant;


/**
 * 审核结果枚举
 *
 * @author system
 */
public enum InputAuditResultEnum {

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

    InputAuditResultEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InputAuditResultEnum getByCode(String code) {
        for (InputAuditResultEnum result : values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        return null;
    }
}
