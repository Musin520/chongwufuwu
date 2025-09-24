package com.example.integration;

import com.javaPro.myProject.SchedulingApplication;
import com.javaPro.myProject.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SchedulingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@Disabled("需要Docker环境，在CI/CD中启用")
class OssIntegrationTest {

    @Container
    static GenericContainer<?> minio = new GenericContainer<>("minio/minio:latest")
            .withExposedPorts(9000)
            .withEnv("MINIO_ACCESS_KEY", "testkey")
            .withEnv("MINIO_SECRET_KEY", "testsecret")
            .withCommand("server", "/data");

    @Autowired
    private FileUploadService fileUploadService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("oss.endpoint", () -> "http://localhost:" + minio.getMappedPort(9000));
        registry.add("oss.accessKeyId", () -> "testkey");
        registry.add("oss.accessKeySecret", () -> "testsecret");
        registry.add("oss.bucketName", () -> "test-bucket");
        registry.add("oss.urlPrefix", () -> "http://localhost:" + minio.getMappedPort(9000) + "/test-bucket/");
    }

    @BeforeEach
    void setUp() {
        // 确保MinIO容器已启动
        assertThat(minio.isRunning()).isTrue();
    }

    @Test
    void testFileUploadIntegration() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        // When
        String result = fileUploadService.uploadFile(file);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("test-bucket");
        assertThat(result).endsWith(".jpg");
    }
}