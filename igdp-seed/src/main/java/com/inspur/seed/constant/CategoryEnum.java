package com.inspur.seed.constant;

/**
 * 投入品大类枚举
 *
 * @author system
 */
public enum CategoryEnum {

    /**
     * 种子
     */
    SEED("seed", "Seed"),

    /**
     * 化肥
     */
    FERTILIZER("fertilizer", "Fertilizer"),

    /**
     * 农药
     */
    PESTICIDE("pesticide", "Pesticide");

    private final String code;
    private final String desc;

    CategoryEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CategoryEnum getByCode(String code) {
        for (CategoryEnum category : values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}
