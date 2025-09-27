# 登录自动化测试指南

## 概述

本项目包含了完整的登录自动化测试套件，支持测试管理员、普通用户和服务商的登录功能。

## 测试文件说明

### 1. SimpleLoginTest.java (推荐)
- **简化的登录测试**，专注于核心功能
- 测试管理员、普通用户、服务商登录
- 测试错误凭据处理
- 更稳定，适合CI/CD环境

### 2. VisualLoginTest.java
- **完整的可视化登录测试**
- 包含详细的用户交互演示
- 支持多种浏览器（Chrome、Edge）
- 适合手动观察测试过程

## 前置条件

### 1. 确保应用程序运行
```bash
# 启动应用程序
mvn spring-boot:run

# 或者在IDE中运行SchedulingApplication类
```

### 2. 确保测试用户存在
确保数据库中存在以下测试用户：
- **管理员**: username=`admin`, password=`123`
- **普通用户**: username=`kaka`, password=`123`
- **服务商**: username=`maijia3`, password=`123`

### 3. 浏览器驱动
项目使用WebDriverManager自动管理浏览器驱动，无需手动下载。测试会自动清除驱动缓存并下载与当前Chrome版本兼容的ChromeDriver。

## 运行测试

### 方法1：使用脚本运行 (推荐)

**Windows:**
```cmd
# 双击运行或在命令行执行
run-login-tests.bat
```

**Linux/Mac:**
```bash
# 给脚本执行权限
chmod +x run-login-tests.sh

# 运行脚本
./run-login-tests.sh
```

### 方法2：使用Maven命令

**运行简化登录测试:**
```bash
mvn test -Dtest=SimpleLoginTest
```

**运行完整可视化测试:**
```bash
mvn test -Dtest=VisualLoginTest
```

**运行所有登录测试:**
```bash
mvn test -Dtest=*LoginTest
```

**运行特定测试方法:**
```bash
# 只测试管理员登录
mvn test -Dtest=SimpleLoginTest#testAdminLogin

# 只测试错误凭据
mvn test -Dtest=SimpleLoginTest#testInvalidLogin
```

### 方法3：在IDE中运行

1. 在IDE中打开测试文件
2. 右键点击测试类或测试方法
3. 选择"Run Test"

## 测试场景

### SimpleLoginTest 测试场景

1. **管理员登录测试** (`testAdminLogin`)
   - 使用admin/123登录
   - 验证跳转到管理员页面
   - 自动退出登录

2. **普通用户登录测试** (`testUserLogin`)
   - 使用kaka/123登录
   - 验证跳转到用户页面
   - 自动退出登录

3. **服务商登录测试** (`testMerchantLogin`)
   - 使用maijia3/123登录
   - 验证跳转到服务商页面
   - 自动退出登录

4. **错误凭据测试** (`testInvalidLogin`)
   - 使用错误的用户名/密码
   - 验证登录被拒绝
   - 确保仍在登录页面

### VisualLoginTest 测试场景

1. **完整三角色登录流程**
   - 测试错误凭据
   - 依次测试三种角色登录
   - 每次登录后退出
   - 可视化演示整个过程

## 配置选项

### 浏览器配置

在测试类中可以修改浏览器选项：

```java
ChromeOptions options = new ChromeOptions();

// 无头模式（后台运行，不显示浏览器窗口）
options.addArguments("--headless");

// 窗口大小
options.addArguments("--window-size=1920,1080");

// 禁用GPU加速（在某些环境中更稳定）
options.addArguments("--disable-gpu");
```

### 超时配置

```java
// 隐式等待时间
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

// 显式等待时间
wait = new WebDriverWait(driver, Duration.ofSeconds(15));
```

## 故障排除

### 1. 应用程序未运行
```
❌ 应用程序未运行！请先启动应用程序：mvn spring-boot:run
```
**解决方案**: 确保应用程序在http://localhost:7002上运行

### 2. 浏览器驱动问题
```
WebDriver初始化失败
```
**解决方案**: 
- 确保网络连接正常（WebDriverManager需要下载驱动）
- 尝试手动指定浏览器路径
- 检查浏览器版本是否过旧

### 3. 元素定位失败
```
所有定位器都无法找到元素
```
**解决方案**:
- 检查页面是否完全加载
- 增加等待时间
- 检查页面结构是否发生变化

### 4. 测试用户不存在
```
登录失败，仍在登录页面
```
**解决方案**: 确保数据库中存在测试用户，或修改测试中的用户名/密码

## 最佳实践

### 1. 测试环境隔离
- 使用专门的测试数据库
- 确保测试数据的一致性
- 避免测试影响生产数据

### 2. 测试稳定性
- 使用显式等待而不是Thread.sleep()
- 实现重试机制
- 处理网络延迟和页面加载时间

### 3. 测试维护
- 定期更新测试用例
- 保持测试代码的简洁性
- 添加详细的日志输出

## 扩展测试

### 添加新的测试场景

```java
@Test
@DisplayName("密码重置测试")
void testPasswordReset() {
    // 实现密码重置测试逻辑
}

@Test
@DisplayName("记住我功能测试")
void testRememberMe() {
    // 实现记住我功能测试逻辑
}
```

### 集成到CI/CD

```yaml
# GitHub Actions示例
- name: Run Login Tests
  run: |
    mvn spring-boot:run &
    sleep 30
    mvn test -Dtest=SimpleLoginTest
    pkill -f spring-boot:run
```

## 总结

登录自动化测试提供了：
- ✅ 完整的登录功能验证
- ✅ 多角色登录测试
- ✅ 错误处理验证
- ✅ 易于运行和维护
- ✅ 详细的测试报告

通过这些测试，可以确保登录功能的稳定性和可靠性，提高代码质量和用户体验。
