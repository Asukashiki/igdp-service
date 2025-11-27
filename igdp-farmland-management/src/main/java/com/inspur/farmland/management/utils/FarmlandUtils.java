package com.inspur.farmland.management.utils;

import cn.hutool.core.util.StrUtil;
import com.inspur.farmland.management.constant.FarmlandConstants;

/**
 * 农田管理工具类
 * 
 * @author inspur
 */
public class FarmlandUtils {

    /**
     * 获取认证状态描述
     * 
     * @param status 状态值
     * @return 状态描述
     */
    public static String getCertificationStatusDesc(Integer status) {
        if (status == null) {
            return "";
        }
        
        switch (status) {
            case FarmlandConstants.CertificationStatus.PENDING:
                return "待审批";
            case FarmlandConstants.CertificationStatus.APPROVED:
                return "审批通过";
            case FarmlandConstants.CertificationStatus.REJECTED:
                return "审批驳回";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取土地流转状态描述
     * 
     * @param status 状态值
     * @return 状态描述
     */
    public static String getLandTransferStatusDesc(Integer status) {
        if (status == null) {
            return "";
        }
        
        switch (status) {
            case FarmlandConstants.LandTransferStatus.NOT_TRANSFERRED:
                return "未流转";
            case FarmlandConstants.LandTransferStatus.TRANSFERRED:
                return "已流转";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取用户状态描述
     * 
     * @param status 状态值
     * @return 状态描述
     */
    public static String getUserStatusDesc(String status) {
        if (StrUtil.isBlank(status)) {
            return "";
        }
        
        switch (status) {
            case FarmlandConstants.UserStatus.NORMAL:
                return "正常";
            case FarmlandConstants.UserStatus.DISABLED:
                return "停用";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取性别描述
     * 
     * @param sex 性别值
     * @return 性别描述
     */
    public static String getSexDesc(String sex) {
        if (StrUtil.isBlank(sex)) {
            return "";
        }
        
        switch (sex) {
            case FarmlandConstants.Sex.MALE:
                return "男";
            case FarmlandConstants.Sex.FEMALE:
                return "女";
            case FarmlandConstants.Sex.UNKNOWN:
                return "未知";
            default:
                return "未知";
        }
    }
}