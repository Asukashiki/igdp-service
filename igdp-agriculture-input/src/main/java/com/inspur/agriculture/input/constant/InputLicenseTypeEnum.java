package com.inspur.agriculture.input.constant;

/**
 * 证件类型枚举
 *
 * @author system
 */
public enum InputLicenseTypeEnum {

    /**
     * 营业执照
     */
    BUSINESS_LICENSE("business_license", "Business License"),

    /**
     * 种子许可证
     */
    SEED_LICENSE("seed_license", "Seed License"),

    /**
     * 税务登记证
     */
    TAX_CERTIFICATE("tax_certificate", "Tax Certificate"),

    /**
     * 工厂许可证
     */
    FACTORY_PERMIT("factory_permit", "Factory Permit");

    private final String code;
    private final String desc;

    InputLicenseTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InputLicenseTypeEnum getByCode(String code) {
        for (InputLicenseTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
