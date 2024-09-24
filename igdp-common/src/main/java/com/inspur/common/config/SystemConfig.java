package com.inspur.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author liyunlong
 */
@Component
@ConfigurationProperties(prefix = "sys")
@Setter
@Getter
public class SystemConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 上传路径 */
    @Getter
    private static String profile;

    /** 获取地址开关 */
    @Getter
    private static boolean addressEnabled;

    /** 验证码类型 */
    @Getter
    private static String captchaType;

    /**
     * 获取账号用户相关信息方式
     * 默认是内置方式inner
     * */
    @Getter
    private static String accountSelectType;

    public void setProfile(String profile)
    {
        SystemConfig.profile = profile;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        SystemConfig.addressEnabled = addressEnabled;
    }

    public void setCaptchaType(String captchaType) {
        SystemConfig.captchaType = captchaType;
    }

    public void setAccountSelectType(String accountSelectType) {
        SystemConfig.accountSelectType = accountSelectType;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

}
