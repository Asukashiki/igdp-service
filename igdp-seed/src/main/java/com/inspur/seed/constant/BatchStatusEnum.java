package com.inspur.seed.constant;

/**
 * Batch Status Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum BatchStatusEnum {

    /**
     * Collecting
     */
    COLLECTING("collecting", "Collecting"),

    /**
     * Reviewing
     */
    REVIEWING("reviewing", "Reviewing"),

    /**
     * Completed
     */
    COMPLETED("completed", "Completed"),

    /**
     * Locked
     */
    LOCKED("locked", "Locked");

    private final String code;
    private final String message;

    BatchStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static BatchStatusEnum fromCode(String code) {
        for (BatchStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
