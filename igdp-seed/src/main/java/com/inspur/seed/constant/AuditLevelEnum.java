package com.inspur.seed.constant;

/**
 * Audit Level Enum
 *
 * @author igdp
 * @date 2025-12-04
 */
public enum AuditLevelEnum {

    /**
     * Village Level
     */
    VILLAGE("village", "Village"),

    /**
     * Town Level
     */
    TOWN("town", "Town"),

    /**
     * District Level
     */
    DISTRICT("district", "District"),

    /**
     * State Level
     */
    STATE("state", "State"),

    /**
     * Ministry Level
     */
    MINISTRY("ministry", "Ministry");

    private final String code;
    private final String message;

    AuditLevelEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static AuditLevelEnum fromCode(String code) {
        for (AuditLevelEnum level : values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }

    /**
     * Get next audit level
     */
    public AuditLevelEnum getNextLevel() {
        switch (this) {
            case VILLAGE:
                return TOWN;
            case TOWN:
                return DISTRICT;
            case DISTRICT:
                return STATE;
            case STATE:
                return MINISTRY;
            default:
                return null;
        }
    }
}
