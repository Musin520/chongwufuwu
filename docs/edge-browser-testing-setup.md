# Edge浏览器登录自动化测试配置指南

## 🎯 概述

本文档介绍如何配置和运行使用Microsoft Edge浏览器的登录自动化测试。我们已经修改了现有的测试代码，使其优先使用Edge浏览器进行自动化测试。

## 📁 文件修改总结

### 1. 修改的现有文件

#### `SimpleLoginTest.java`
- **修改内容**: 添加了Edge WebDriver支持
- **优先级**: 优先使用Edge浏览器，Chrome作为备选
- **新增导入**: 
  ```java
  import org.openqa.selenium.edge.EdgeDriver;
  import org.openqa.selenium.edge.EdgeOptions;
  ```

#### `VisualLoginTest.java`
- **修改内容**: 调整浏览器初始化顺序
- **优先级**: 优先使用Edge浏览器，Chrome作为备选
- **改进**: 更好的错误处理和回退机制

### 2. 新增的文件

#### `EdgeLoginTest.java`
- **用途**: 专门使用Edge浏览器的登录测试类
- **特点**: 
  - 只使用Edge浏览器，不回退到其他浏览器
  - 包含完整的登录流程测试
  - 针对Edge浏览器优化的配置选项

#### `run-edge-tests.bat` (Windows)
- **用途**: Windows批处理脚本，用于运行Edge测试
- **功能**: 
  - 环境检查（Java、Maven）
  - 应用程序运行状态检查
  - 多种测试选项菜单

#### `run-edge-tests.sh` (Linux/Mac)
- **用途**: Unix/Linux shell脚本，用于运行Edge测试
- **功能**: 与Windows版本相同的功能

## 🔧 Edge浏览器配置选项

### 基本配置
```java
EdgeOptions options = new EdgeOptions();
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--disable-gpu");
options.addArguments("--window-size=1280,720");
options.addArguments("--start-maximized");
options.addArguments("--remote-allow-origins=*");
options.addArguments("--disable-web-security");
options.addArguments("--allow-running-insecure-content");
```

### Edge特有配置
```java
options.addArguments("--disable-features=VizDisplayCompositor");
options.addArguments("--disable-blink-features=AutomationControlled");
options.addArguments("--disable-extensions");
options.addArguments("--disable-plugins");
options.addArguments("--disable-images");

// Edge实验性选项
options.setExperimentalOption("useAutomationExtension", false);
options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
```

## 🚀 运行测试

### 方法1: 使用脚本运行

#### Windows:
```bash
# 运行批处理脚本
run-edge-tests.bat
```

#### Linux/Mac:
```bash
# 给脚本执行权限
chmod +x run-edge-tests.sh

# 运行脚本
./run-edge-tests.sh
```

### 方法2: 直接使用Maven命令

#### 运行所有Edge测试:
```bash
mvn test -Dtest=EdgeLoginTest
```

#### 运行特定测试:
```bash
# 管理员登录测试
mvn test -Dtest=EdgeLoginTest#testAdminLoginWithEdge

# 普通用户登录测试
mvn test -Dtest=EdgeLoginTest#testUserLoginWithEdge

# 服务商登录测试
mvn test -Dtest=EdgeLoginTest#testMerchantLoginWithEdge

# 错误凭据测试
mvn test -Dtest=EdgeLoginTest#testInvalidLoginWithEdge

# 完整登录流程测试
mvn test -Dtest=EdgeLoginTest#testCompleteLoginFlowWithEdge
```

#### 运行修改后的现有测试（优先使用Edge）:
```bash
# 简化测试（优先Edge）
mvn test -Dtest=SimpleLoginTest

# 可视化测试（优先Edge）
mvn test -Dtest=VisualLoginTest
```

## 📋 测试前准备

### 1. 环境要求
- ✅ Java 8或更高版本
- ✅ Maven 3.6或更高版本
- ✅ Microsoft Edge浏览器（最新版本）
- ✅ 应用程序运行在端口7002

### 2. 启动应用程序
```bash
# 在项目根目录运行
mvn spring-boot:run
```

### 3. 验证应用程序运行
- 访问: http://localhost:7002
- 确保登录页面可以正常访问

## 🎯 测试覆盖范围

### EdgeLoginTest.java 包含的测试:
1. **管理员登录测试** - 测试admin用户登录
2. **普通用户登录测试** - 测试kaka用户登录
3. **服务商登录测试** - 测试maijia3用户登录
4. **错误凭据测试** - 测试错误用户名密码的处理
5. **完整登录流程测试** - 综合测试所有登录场景

### 测试用户凭据:
- **管理员**: admin / 123
- **普通用户**: kaka / 123
- **服务商**: maijia3 / 123

## 🔍 故障排除

### 常见问题及解决方案:

#### 1. Edge浏览器未找到
```
解决方案: 确保已安装Microsoft Edge浏览器
```

#### 2. WebDriverManager下载失败
```
解决方案: 检查网络连接，或手动下载EdgeDriver
```

#### 3. 应用程序未运行
```
解决方案: 先启动应用程序 mvn spring-boot:run
```

#### 4. 端口7002被占用
```
解决方案: 
- 停止占用端口的进程
- 或修改应用程序端口配置
```

## 📊 测试报告

测试运行后会生成以下报告:
- **控制台输出**: 详细的测试执行日志
- **JUnit报告**: target/surefire-reports/
- **JaCoCo覆盖率报告**: target/site/jacoco/

## 🎉 优势

使用Edge浏览器进行自动化测试的优势:
1. **兼容性**: 与Windows系统深度集成
2. **性能**: 基于Chromium内核，性能优秀
3. **稳定性**: Microsoft官方支持，更新及时
4. **安全性**: 内置安全功能，测试更可靠

## 📝 注意事项

1. **浏览器版本**: 确保Edge浏览器为最新版本
2. **WebDriver版本**: WebDriverManager会自动管理驱动版本
3. **测试数据**: 确保数据库中存在测试用户
4. **网络环境**: 确保可以访问WebDriverManager的下载源

## 🔄 后续改进

可以考虑的改进方向:
1. 添加并行测试支持
2. 集成CI/CD流水线
3. 添加更多浏览器支持
4. 增加测试数据管理
5. 添加截图和视频录制功能
