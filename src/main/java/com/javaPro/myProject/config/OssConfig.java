package com.javaPro.myProject.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    private static final Logger logger = LoggerFactory.getLogger(OssConfig.class);

    @Value("${oss.endpoint:}")
    private String endpoint;

    @Value("${oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret:}")
    private String accessKeySecret;

    @Bean
    @ConditionalOnProperty(name = "oss.enabled", havingValue = "true", matchIfMissing = false)
    public OSS ossClient() {
        // 检查配置是否有效
        if (isValidConfig()) {
            logger.info("创建OSS客户端，endpoint: {}", endpoint);
            return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        } else {
            logger.warn("OSS配置无效，跳过OSS客户端创建");
            return null;
        }
    }

    private boolean isValidConfig() {
        return endpoint != null && !endpoint.isEmpty() &&
               accessKeyId != null && !accessKeyId.isEmpty() &&
               !accessKeyId.equals("your-access-key-id") &&
               accessKeySecret != null && !accessKeySecret.isEmpty() &&
               !accessKeySecret.equals("your-access-key-secret");
    }
}