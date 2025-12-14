package com.inspur.seed.constant;

/**
 * 育种批次状态枚举类
 *
 * @author inspur
 * @date 2025-12-11
 */
public enum BreedingBatchStatusEnum {

    /**
     * S0: 草稿
     */
    DRAFT("S0", "草稿"),

    /**
     * S1: 待审核
     */
    PENDING_APPROVAL("S1", "待审核"),

    /**
     * S2: 审核通过
     */
    APPROVED("S2", "审核通过"),

    /**
     * S3: 审核驳回
     */
    REJECTED("S3", "审核驳回"),

    /**
     * S4: 进行中
     */
    IN_PROGRESS("S4", "进行中"),

    /**
     * S5: 暂停
     */
    PAUSED("S5", "暂停"),

    /**
     * S6: 终止
     */
    TERMINATED("S6", "终止"),

    /**
     * S7: 完成
     */
    COMPLETED("S7", "完成"),

    /**
     * S8: 已归档
     */
    ARCHIVED("S8", "已归档"),

    /**
     * S9: 已作废
     */
    CANCELLED("S9", "已作废"),

    /**
     * S10: 异常
     */
    EXCEPTION("S10", "异常");

    private final String code;
    private final String message;

    BreedingBatchStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static BreedingBatchStatusEnum fromCode(String code) {
        for (BreedingBatchStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}