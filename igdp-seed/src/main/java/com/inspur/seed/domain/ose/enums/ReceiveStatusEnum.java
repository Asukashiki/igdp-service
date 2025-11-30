package com.inspur.seed.domain.ose.enums;

/**
 * 接收状态枚举
 *
 * @author igdp
 */
public enum ReceiveStatusEnum {

    PENDING("PENDING", "待确认"),
    CONFIRMED("CONFIRMED", "已确认");

    private final String code;
    private final String desc;

    ReceiveStatusEnum(String code, String desc) {
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
        for (ReceiveStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
