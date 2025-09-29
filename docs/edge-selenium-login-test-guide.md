# Edge浏览器Selenium登录自动化测试指南

## 🎯 概述

我为您创建了一个完整的使用Edge浏览器的Selenium登录功能自动化测试。这个测试包含了完整的登录场景覆盖，适合学习和实际使用。

## 📁 文件结构

```
myJavaProject/
├── src/test/java/com/javaPro/myProject/selenium/
│   ├── EdgeLoginTest.java           # 完整版Edge登录测试
│   └── SimpleEdgeLoginTest.java     # 简化版Edge登录测试（适合初学者）
├── run-edge-login-test.bat          # Windows运行脚本
└── docs/
    └── edge-selenium-login-test-guide.md  # 本文档
```

## 🔧 测试类说明

### 1. EdgeLoginTest.java - 完整版测试

**特点：**
- 完整的错误处理和日志输出
- 应用程序运行状态检查
- 多种登录场景覆盖
- 自动退出登录功能
- 页面元素验证

**测试方法：**
- `testAdminLogin()` - 管理员登录测试
- `testUserLogin()` - 普通用户登录测试
- `testMerchantLogin()` - 服务商登录测试
- `testInvalidLogin()` - 错误凭据测试
- `testLoginPageElements()` - 页面元素验证测试
- `testCompleteLoginFlow()` - 完整登录流程测试

### 2. SimpleEdgeLoginTest.java - 简化版测试

**特点：**
- 代码简洁易懂
- 适合Selenium初学者
- 基本的登录测试功能
- 详细的控制台输出

**测试方法：**
- `testLogin()` - 基本登录测试
- `testWrongPassword()` - 错误密码测试
- `testUserLogin()` - 普通用户登录测试
- `testPageElements()` - 页面元素检查测试

## 🚀 运行方式

### 方法1：使用运行脚本（推荐）

```bash
# Windows
run-edge-login-test.bat
```

脚本会提供菜单选择：
1. 运行完整Edge登录测试（推荐）
2. 运行简化Edge登录测试（适合初学者）
3. 运行管理员登录测试
4. 运行普通用户登录测试
5. 运行错误凭据测试
6. 运行页面元素检查测试
7. 运行完整登录流程测试

### 方法2：使用Maven命令

```bash
# 运行完整Edge登录测试
mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest

# 运行简化Edge登录测试
mvn test -Dtest=com.javaPro.myProject.selenium.SimpleEdgeLoginTest

# 运行特定测试方法
mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testAdminLogin
mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testInvalidLogin
mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testCompleteLoginFlow
```

## 📋 测试前准备

### 1. 环境要求
- ✅ Java 8或更高版本
- ✅ Maven 3.6或更高版本
- ✅ Microsoft Edge浏览器（最新版本）
- ✅ 网络连接（用于下载WebDriver）

### 2. 启动应用程序
```bash
# 在项目根目录运行
mvn spring-boot:run
```

### 3. 验证应用程序
- 访问：http://localhost:7002
- 确保登录页面可以正常访问

### 4. 测试用户数据
确保数据库中存在以下测试用户：
- **管理员**：admin / 123
- **普通用户**：kaka / 123
- **服务商**：maijia3 / 123

## 🎯 测试场景覆盖

### 1. 正常登录测试
- 管理员登录 → 跳转到 `/web/index`
- 普通用户登录 → 跳转到 `/web/userindex`
- 服务商登录 → 跳转到 `/web/index`

### 2. 异常情况测试
- 错误用户名密码 → 停留在登录页面
- 不存在的用户 → 登录被拒绝

### 3. 页面元素测试
- 用户名输入框存在且可见
- 密码输入框存在且可见
- 登录按钮存在且可点击
- 页面标题正确显示

### 4. 完整流程测试
- 错误凭据测试 → 正常登录测试 → 退出登录测试

## 💡 代码示例解析

### 基本的Edge浏览器启动
```java
// 自动下载和配置Edge驱动
WebDriverManager.edgedriver().setup();

// 创建Edge浏览器选项
EdgeOptions options = new EdgeOptions();
options.addArguments("--start-maximized");
options.addArguments("--remote-allow-origins=*");

// 启动Edge浏览器
WebDriver driver = new EdgeDriver(options);
```

### 登录操作示例
```java
// 1. 打开登录页面
driver.get("http://localhost:7002/login");

// 2. 找到并填写用户名
WebElement usernameInput = driver.findElement(By.id("account"));
usernameInput.clear();
usernameInput.sendKeys("admin");

// 3. 找到并填写密码
WebElement passwordInput = driver.findElement(By.id("password"));
passwordInput.clear();
passwordInput.sendKeys("123");

// 4. 点击登录按钮
WebElement loginButton = driver.findElement(By.id("login"));
loginButton.click();

// 5. 验证登录结果
String currentUrl = driver.getCurrentUrl();
if (currentUrl.contains("/web/")) {
    System.out.println("登录成功！");
}
```

### 等待元素出现
```java
// 使用显式等待
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.id("account"))
);
```

## 🔍 故障排除

### 常见问题及解决方案

#### 1. Edge浏览器未找到
```
错误：SessionNotCreatedException
解决：确保已安装Microsoft Edge浏览器
```

#### 2. WebDriver下载失败
```
错误：WebDriverManager下载失败
解决：检查网络连接，或手动下载EdgeDriver
```

#### 3. 应用程序未运行
```
错误：应用程序未在 http://localhost:7002 上运行
解决：先启动应用程序 mvn spring-boot:run
```

#### 4. 元素找不到
```
错误：NoSuchElementException
解决：检查元素ID是否正确，或增加等待时间
```

#### 5. 测试用户不存在
```
错误：登录失败，仍在登录页面
解决：确保数据库中存在测试用户
```

## 📊 测试报告

测试运行后会在控制台输出详细信息：
- ✅ 成功的测试步骤
- ❌ 失败的测试步骤
- 📝 详细的执行日志
- 🔍 URL跳转信息

## 🎉 优势特点

### 1. 自动化程度高
- 自动下载和配置WebDriver
- 自动检查应用程序运行状态
- 自动处理浏览器启动和关闭

### 2. 易于使用
- 提供运行脚本，一键执行
- 详细的控制台输出
- 清晰的测试结果反馈

### 3. 覆盖全面
- 多种登录场景
- 正常和异常情况
- 页面元素验证

### 4. 代码质量高
- 良好的错误处理
- 清晰的代码结构
- 详细的注释说明

## 📝 使用建议

1. **初学者**：建议从 `SimpleEdgeLoginTest.java` 开始学习
2. **实际项目**：使用 `EdgeLoginTest.java` 进行完整测试
3. **调试问题**：观察控制台输出，查看详细的执行日志
4. **扩展功能**：可以基于现有代码添加更多测试场景

## 🔄 后续扩展

可以考虑添加的功能：
1. 截图功能（失败时自动截图）
2. 测试数据外部化（Excel/CSV文件）
3. 并行测试执行
4. 集成测试报告（Allure/ExtentReports）
5. 持续集成支持（Jenkins/GitHub Actions）

现在您就有了一个完整的Edge浏览器Selenium登录自动化测试！
