package com.inspur.agriculture.input.domain.feedback.enums;

/**
 * 反馈状态枚举
 *
 * @author igdp
 */
public enum FeedbackStatusEnum {

    PENDING("0", "待处理"),
    PROCESSING("1", "处理中"),
    COMPLETED("2", "已完成"),
    CLOSED("3", "已关闭");

    private final String code;
    private final String desc;

    FeedbackStatusEnum(String code, String desc) {
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
        for (FeedbackStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    public static FeedbackStatusEnum getByCode(String code) {
        for (FeedbackStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
