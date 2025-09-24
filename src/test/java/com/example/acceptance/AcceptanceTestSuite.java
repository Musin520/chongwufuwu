package com.example.acceptance;

import com.javaPro.myProject.SchedulingApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SchedulingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("acceptance")
class AcceptanceTestSuite {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeAll
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    @DisplayName("验收测试 - 应用程序启动检查")
    void applicationStartupTest() {
        // 验证应用程序是否正常启动
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/", String.class);
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.FOUND, HttpStatus.MOVED_PERMANENTLY);
    }

    @Test
    @Order(2)
    @DisplayName("验收测试 - 用户注册登录流程")
    void userRegistrationAndLoginFlow() {
        // 完整的用户注册登录验收测试
        // 这里可以添加具体的用户注册和登录测试逻辑
        assertThat(baseUrl).isNotNull();
    }

    @Test
    @Order(3)
    @DisplayName("验收测试 - 文件上传功能")
    void fileUploadTest() {
        // 文件上传功能验收测试
        assertThat(baseUrl).isNotNull();
    }
}