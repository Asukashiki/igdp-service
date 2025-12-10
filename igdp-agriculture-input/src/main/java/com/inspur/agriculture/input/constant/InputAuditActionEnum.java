package com.inspur.agriculture.input.constant;

/**
 * Audit Action Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum InputAuditActionEnum {

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

    InputAuditActionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static InputAuditActionEnum fromCode(String code) {
        for (InputAuditActionEnum action : values()) {
            if (action.getCode().equals(code)) {
                return action;
            }
        }
        return null;
    }
}
