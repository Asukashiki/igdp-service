package com.inspur.agriculture.input.domain.supplier.enums;

/**
 * 认证状态枚举
 *
 * @author igdp
 */
public enum CertStatusEnum {

    /** 未通过 */
    UN_PASSED(0, "未通过"),

    /** 审核中 */
    AUDITING(1, "审核中"),

    /** 已通过 */
    PASSED(2, "已通过");

    private final Integer code;
    private final String desc;

    CertStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (CertStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "";
    }
}
