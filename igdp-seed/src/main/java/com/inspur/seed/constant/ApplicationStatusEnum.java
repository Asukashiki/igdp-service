package com.inspur.seed.constant;

/**
 * 申请状态枚举
 *
 * @author system
 */
public enum ApplicationStatusEnum {

    /**
     * 草稿
     */
    DRAFT("draft", "Draft"),

    /**
     * 待审核
     */
    PENDING("pending", "Pending"),

    /**
     * 已通过
     */
    APPROVED("approved", "Approved"),

    /**
     * 已驳回
     */
    REJECTED("rejected", "Rejected");

    private final String code;
    private final String desc;

    ApplicationStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ApplicationStatusEnum getByCode(String code) {
        for (ApplicationStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
