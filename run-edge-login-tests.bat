@echo off
echo ========================================
echo    Edge浏览器自动化登录测试运行脚本
echo ========================================
echo.

:: 检查Java环境
echo [1/4] 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Java环境，请确保已安装Java 8或更高版本
    pause
    exit /b 1
)
echo ✅ Java环境检查通过

:: 检查Maven环境
echo.
echo [2/4] 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Maven环境，请确保已安装Maven
    pause
    exit /b 1
)
echo ✅ Maven环境检查通过

:: 检查应用程序是否运行
echo.
echo [3/4] 检查应用程序状态...
echo 正在检查 http://localhost:7002 是否可访问...

:: 使用PowerShell检查端口
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:7002/login' -TimeoutSec 5 -UseBasicParsing; if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 302) { exit 0 } else { exit 1 } } catch { exit 1 }" >nul 2>&1

if %errorlevel% neq 0 (
    echo ❌ 应用程序未在 http://localhost:7002 上运行！
    echo.
    echo 请先启动应用程序：
    echo   方法1: mvn spring-boot:run
    echo   方法2: 在IDE中运行主类
    echo.
    echo 启动应用程序后，请重新运行此脚本
    pause
    exit /b 1
)
echo ✅ 应用程序运行检查通过

:: 运行测试
echo.
echo [4/4] 开始运行Edge浏览器登录测试...
echo.
echo 测试将包括：
echo   - 管理员登录测试
echo   - 普通用户登录测试  
echo   - 错误密码测试
echo   - 页面元素检查测试
echo.

:: 创建截图目录
if not exist "screenshots" mkdir screenshots

:: 运行测试
echo 正在运行测试...
mvn test -Dtest=SimpleEdgeLoginTest -q

if %errorlevel% eq 0 (
    echo.
    echo ========================================
    echo ✅ 测试运行完成！
    echo ========================================
    echo.
    echo 测试结果：
    echo   - 查看控制台输出了解详细结果
    echo   - 如有失败，截图已保存到 screenshots 目录
    echo   - 完整测试报告位于 target/surefire-reports 目录
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ 测试运行过程中出现错误
    echo ========================================
    echo.
    echo 可能的原因：
    echo   1. 应用程序未正常运行
    echo   2. Edge浏览器未安装或版本过旧
    echo   3. 网络连接问题（WebDriverManager需要下载驱动）
    echo   4. 测试用户不存在于数据库中
    echo.
    echo 解决建议：
    echo   1. 确保应用程序在 http://localhost:7002 正常运行
    echo   2. 确保已安装最新版本的Microsoft Edge浏览器
    echo   3. 检查网络连接
    echo   4. 确保数据库中存在测试用户（admin/123, moka/123）
    echo.
)

echo 按任意键退出...
pause >nul
