package com.inspur.seed.constant;

/**
 * Demand Status Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum DemandStatusEnum {

    /**
     * Draft
     */
    DRAFT("draft", "Draft"),

    /**
     * Submitted
     */
    SUBMITTED("submitted", "Submitted"),

    /**
     * Approved
     */
    APPROVED("approved", "Approved"),

    /**
     * Rejected
     */
    REJECTED("rejected", "Rejected"),

    /**
     * Locked
     */
    LOCKED("locked", "Locked");

    private final String code;
    private final String message;

    DemandStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static DemandStatusEnum fromCode(String code) {
        for (DemandStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
