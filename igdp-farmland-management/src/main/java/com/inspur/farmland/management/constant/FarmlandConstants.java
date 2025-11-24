package com.inspur.farmland.management.constant;

/**
 * 农田管理常量类
 * 
 * @author inspur
 */
public class FarmlandConstants {

    /**
     * 认证状态常量
     */
    public static final class CertificationStatus {
        /** 待审批 */
        public static final int PENDING = 0;
        /** 审批通过 */
        public static final int APPROVED = 1;
        /** 审批驳回 */
        public static final int REJECTED = 2;
    }

    /**
     * 土地流转状态常量
     */
    public static final class LandTransferStatus {
        /** 未流转 */
        public static final int NOT_TRANSFERRED = 0;
        /** 已流转 */
        public static final int TRANSFERRED = 1;
    }

    /**
     * 用户状态常量
     */
    public static final class UserStatus {
        /** 正常 */
        public static final String NORMAL = "0";
        /** 停用 */
        public static final String DISABLED = "1";
    }

    /**
     * 性别常量
     */
    public static final class Sex {
        /** 男 */
        public static final String MALE = "0";
        /** 女 */
        public static final String FEMALE = "1";
        /** 未知 */
        public static final String UNKNOWN = "2";
    }
}