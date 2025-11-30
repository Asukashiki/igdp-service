package com.inspur.seed.domain.breed.enums;

/**
 * 生产状态枚举
 *
 * @author igdp
 */
public enum ProduceStatusEnum {

    FINISHED("FINISHED", "已完成");

    private final String code;
    private final String desc;

    ProduceStatusEnum(String code, String desc) {
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
        for (ProduceStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
