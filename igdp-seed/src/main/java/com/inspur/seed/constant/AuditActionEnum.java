package com.inspur.seed.constant;

/**
 * Audit Action Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum AuditActionEnum {

    /**
     * Submit
     */
    SUBMIT("submit", "Submit"),

    /**
     * Approve
     */
    APPROVE("approve", "Approve"),

    /**
     * Reject
     */
    REJECT("reject", "Reject");

    private final String code;
    private final String message;

    AuditActionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static AuditActionEnum fromCode(String code) {
        for (AuditActionEnum action : values()) {
            if (action.getCode().equals(code)) {
                return action;
            }
        }
        return null;
    }
}
