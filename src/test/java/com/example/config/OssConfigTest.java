package com.example.config;

import com.javaPro.myProject.SchedulingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SchedulingApplication.class)
@TestPropertySource(properties = {
    "oss.endpoint=https://test-endpoint.com",
    "oss.accessKeyId=test-key",
    "oss.accessKeySecret=test-secret",
    "oss.bucketName=test-bucket",
    "oss.urlPrefix=https://test-prefix.com/"
})
class OssConfigTest {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${oss.urlPrefix}")
    private String urlPrefix;

    @Test
    void testOssConfigurationLoading() {
        assertThat(endpoint).isEqualTo("https://test-endpoint.com");
        assertThat(bucketName).isEqualTo("test-bucket");
        assertThat(urlPrefix).isEqualTo("https://test-prefix.com/");
    }
}