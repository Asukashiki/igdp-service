package com.inspur.agriculture.input.domain.inventory.enums;

/**
 * 库存状态枚举
 *
 * @author igdp
 */
public enum StockStatusEnum {

    NORMAL("0", "正常"),
    NEAR_EXPIRY("1", "临期"),
    EXPIRED("2", "过期");

    private final String code;
    private final String desc;

    StockStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据代码获取描述
     *
     * @param code 状态代码
     * @return 状态描述
     */
    public static String getDescByCode(String code) {
        for (StockStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }

    /**
     * 根据代码获取枚举
     *
     * @param code 状态代码
     * @return 枚举对象
     */
    public static StockStatusEnum getByCode(String code) {
        for (StockStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 验证代码是否有效
     *
     * @param code 状态代码
     * @return 是否有效
     */
    public static boolean isValid(String code) {
        return getByCode(code) != null;
    }
}
