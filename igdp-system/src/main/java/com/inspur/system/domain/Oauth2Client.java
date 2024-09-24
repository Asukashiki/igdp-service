package com.inspur.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inspur.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Oath2单点客户端信息对象 oauth2_client
 * 
 * @author ruoyi
 * @date 2024-01-25
 */
@TableName("oauth2_client")
@Setter
@Getter
public class Oauth2Client extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应用id，应该全局唯一 */
    @TableId(type = IdType.INPUT)
    private String clientId;

    /**
     * 服务端名称
     * */
    private String clientName;

    /** 应用秘钥 */
    private String clientSecret;

    /** 应用签约的所有权限, 多个用逗号隔开 */
    private String contractScope;

    /** 应用允许授权的所有URL, 多个用逗号隔开 （可以使用*号通配符） */
    private String allowUrl;

    /** 单独配置此 Client 是否打开模式：授权码（Authorization Code） */
    private Boolean isCode;

    /** 单独配置此 Client 是否打开模式：隐藏式（Implicit） */
    private Boolean isImplicit;

    /** 单独配置此 Client 是否打开模式：密码式（Password） */
    private Boolean isPassword;

    /** 单独配置此 Client 是否打开模式：凭证式（Client Credentials） */
    private Boolean isClient;

    /** 是否自动判断此 Client 开放的授权模式 */
    private Boolean isAutoMode;

    /** 单独配置此Client：是否在每次Refresh-Token刷新Access-Token时，产生一个新的 Refresh-Token [ 默认取全局配置 ] */
    private Boolean isNewRefresh;

    /** 单独配置此Client：Access-Token保存的时间（单位：秒） [默认取全局配置] */
    private Long accessTokenTimeout;

    /** 单独配置此Client：Refresh-Token保存的时间（单位：秒） [默认取全局配置] */
    private Long refreshTokenTimeout;

    /** 单独配置此Client：Client-Token 保存的时间（单位：秒） [默认取全局配置] */
    private Long clientTokenTimeout;

    /** 单独配置此Client：Past-Client-Token保存的时间（单位：秒） [默认取全局配置] */
    private Long pastClientTokenTimeout;

    /**
     * 状态
     * 0可用；1停用
     * */
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("clientId", getClientId())
            .append("clientName", getClientName())
            .append("clientSecret", getClientSecret())
            .append("contractScope", getContractScope())
            .append("allowUrl", getAllowUrl())
            .append("isCode", getIsCode())
            .append("isImplicit", getIsImplicit())
            .append("isPassword", getIsPassword())
            .append("isClient", getIsClient())
            .append("isAutoMode", getIsAutoMode())
            .append("isNewRefresh", getIsNewRefresh())
            .append("accessTokenTimeout", getAccessTokenTimeout())
            .append("refreshTokenTimeout", getRefreshTokenTimeout())
            .append("clientTokenTimeout", getClientTokenTimeout())
            .append("pastClientTokenTimeout", getPastClientTokenTimeout())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
