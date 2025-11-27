package com.inspur.agriculture.input.domain.feedback.enums;

/**
 * 反馈优先级枚举
 *
 * @author igdp
 */
public enum FeedbackPriorityEnum {

    LOW("0", "低"),
    MEDIUM("1", "中"),
    HIGH("2", "高"),
    URGENT("3", "紧急");

    private final String code;
    private final String desc;

    FeedbackPriorityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (FeedbackPriorityEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static FeedbackPriorityEnum getByCode(String code) {
        for (FeedbackPriorityEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
