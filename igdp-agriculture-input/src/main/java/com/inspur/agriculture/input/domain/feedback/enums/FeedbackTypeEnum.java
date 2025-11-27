package com.inspur.agriculture.input.domain.feedback.enums;

/**
 * 反馈类型枚举
 *
 * @author igdp
 */
public enum FeedbackTypeEnum {

    COMPLAINT("0", "投诉"),
    SUGGESTION("1", "建议"),
    CONSULTATION("2", "咨询"),
    BUG_REPORT("3", "故障报告"),
    OTHER("4", "其他");

    private final String code;
    private final String desc;

    FeedbackTypeEnum(String code, String desc) {
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
        for (FeedbackTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static FeedbackTypeEnum getByCode(String code) {
        for (FeedbackTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
