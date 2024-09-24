package com.inspur.doc.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author liyunlong
 * @version 1.0
 * @ClassName MinioClient
 * @date 2024/7/5 11:07
 */
@Configuration
@Slf4j
@RefreshScope
public class MinioClientConfig {
    @Value("${sys.doc.minio.endpoint:''}")
    private String endpoint;
    @Value("${sys.doc.minio.access-key:''}")
    private String accessKey;
    @Value("${sys.doc.minio.secret-key:''}")
    private String secretKey;
    @Value("${sys.doc.minio.bucket-name:''}")
    private String bucketName;

    /**
     * 注入minio 客户端
     */
    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("创建bucketName成功");
            }
        } catch (Exception e) {
            log.error("创建bucket失败", e);
        }
        return minioClient;
    }
}
