package com.javaPro.myProject.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;


import java.time.Duration;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 快速版Edge浏览器自动化登录测试
 * 专注于速度优化，最小化等待时间和资源消耗
 */
public class FastEdgeLoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:7002";
    private static final String LOGIN_PAGE_URL = BASE_URL + "/login";
    
    // 快速模式配置
    private static final boolean HEADLESS_MODE = true;  // 无头模式，大幅提升速度
    private static final int FAST_TIMEOUT = 3;          // 快速超时时间（秒）
    private static final int POLL_INTERVAL = 100;       // 轮询间隔（毫秒）

    @BeforeEach
    void setUp() {
        long startTime = System.currentTimeMillis();
        System.out.println("=== 极速启动模式 ===");

        // 1. 并行检查应用程序（异步）
        
        try {
            // 直接设置系统属性，避免网络连接
            System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
            System.out.println("✅ 使用本地Edge驱动配置");
        } catch (Exception e) {
            System.out.println("⚠ 驱动配置警告: " + e.getMessage());
        }

        // 3. 极速配置Edge选项
        EdgeOptions options = new EdgeOptions();
        
        // 无头模式 - 最大速度提升
        if (HEADLESS_MODE) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1280,720");
        }
        
        // 极速优化配置
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-images");
        options.addArguments("--disable-javascript");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-background-networking");
        options.addArguments("--remote-allow-origins=*");
        
        // 4. 启动浏览器（使用本地驱动）
        try {
            driver = new EdgeDriver(options);
        } catch (Exception e) {
            System.out.println("⚠ 无法启动Edge浏览器: " + e.getMessage());
            System.out.println("⚠ 跳过Selenium测试，可能是网络连接问题");
            Assumptions.assumeTrue(false, "跳过测试：无法连接到WebDriver服务");
        }
        
        // 5. 极速超时配置
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(2));
        
        // 6. 快速等待对象
        wait = new WebDriverWait(driver, Duration.ofSeconds(FAST_TIMEOUT), Duration.ofMillis(POLL_INTERVAL));

        long endTime = System.currentTimeMillis();
        System.out.println("✅ 极速启动完成，耗时: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("快速管理员登录测试")
    void testFastAdminLogin() {
        System.out.println("=== 快速管理员登录 ===");
        fastLogin("admin", "123", "管理员");
    }

    @Test
    @DisplayName("快速用户登录测试")
    void testFastUserLogin() {
        System.out.println("=== 快速用户登录 ===");
        fastLogin("moka", "123", "用户");
    }

    @Test
    @DisplayName("快速错误密码测试")
    void testFastWrongPassword() {
        System.out.println("=== 快速错误密码测试 ===");
        
        driver.get(LOGIN_PAGE_URL);
        
        // 快速输入错误凭据
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("account")));
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        
        usernameInput.sendKeys("admin");
        passwordInput.sendKeys("wrong");
        loginButton.click();
        
        // 快速验证仍在登录页面
        try {
            Thread.sleep(300); // 最小等待
            assertTrue(driver.getCurrentUrl().contains("/login"), "错误密码应该登录失败");
            System.out.println("✅ 错误密码测试通过");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 快速登录方法
     */
    private void fastLogin(String username, String password, String userType) {
        // 1. 快速导航
        driver.get(LOGIN_PAGE_URL);
        
        // 2. 快速定位和输入
        WebElement usernameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("account")));
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
        
        // 3. 快速验证
        wait.until(ExpectedConditions.urlContains("/web/"));
        assertTrue(driver.getCurrentUrl().contains("/web/"), userType + "登录失败");
        System.out.println("✅ " + userType + "快速登录成功");
    }

    /**
     * 快速应用程序检查
     */
    private boolean isApplicationRunning() {
        try {
            URL url = new URL(BASE_URL + "/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            
            return responseCode == 200 || responseCode == 302;
        } catch (Exception e) {
            return false;
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            // 立即关闭，无等待
            driver.quit();
            System.out.println("✅ 浏览器已立即关闭");
        }
        System.out.println("=== 快速测试完成 ===\n");
    }
}
