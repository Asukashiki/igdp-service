package com.inspur.data.treating.rocketmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName CdtyOrganConfig
 * @date 2024/9/3 10:55
 */
@Component
@RefreshScope
@Configuration
@Data
public class CdtyOrganConfig {
    @Value("${sys.rocketmq.enabled:false}")
    private boolean enabled;

    @Value("${rocketmq.consumer.access-key:''}")
    private String accessKey;
    @Value("${rocketmq.consumer.secret-key:''}")
    private String secretKey;
    @Value("${rocketmq.name-server:''}")
    private String nameServer;


    @Value("${rocketmq.consumer.group:'ywglptGroup'}")
    public String consumerGroup;
}
