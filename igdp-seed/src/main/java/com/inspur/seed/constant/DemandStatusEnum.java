package com.inspur.seed.constant;

/**
 * Demand Status Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum DemandStatusEnum {

    /**
     * Draft (0)
     */
    DRAFT("0", "Draft"),

    /**
     * Submitted (1)
     */
    SUBMITTED("1", "Submitted"),

    /**
     * Approved (2)
     */
    APPROVED("2", "Approved"),

    /**
     * Rejected (3)
     */
    REJECTED("3", "Rejected"),

    /**
     * Locked (4)
     */
    LOCKED("4", "Locked");

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
