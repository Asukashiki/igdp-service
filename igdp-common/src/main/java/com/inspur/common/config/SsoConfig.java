package com.inspur.common.config;

import com.inspur.common.core.domain.model.SsoInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName SsoConfig
 * @date 2024/9/4 17:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoConfig {
    private List<SsoInfo> ssoInfoList;

    public SsoInfo getSsoInfo(String name) {
        SsoInfo ssoInfo = null;
        for (SsoInfo info : ssoInfoList) {
            if (info.getName().equals(name)) {
                ssoInfo = info;
                break;
            }
        }
        return ssoInfo;
    }

    public SsoInfo getSsoInfoByClientId(String clientId) {
        SsoInfo ssoInfo = null;
        for (SsoInfo info : ssoInfoList) {
            if (info.getClientId().equals(clientId)) {
                ssoInfo = info;
                break;
            }
        }
        return ssoInfo;
    }
}
