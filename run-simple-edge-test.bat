@echo off
echo ========================================
echo    简化版Edge浏览器自动化登录测试
echo ========================================
echo.

:: 检查应用程序是否运行
echo [1/2] 检查应用程序状态...
echo 正在检查 http://localhost:7002 是否可访问...

:: 使用PowerShell检查端口
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:7002/login' -TimeoutSec 5 -UseBasicParsing; if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 302) { exit 0 } else { exit 1 } } catch { exit 1 }" >nul 2>&1

if %errorlevel% neq 0 (
    echo ❌ 应用程序未在 http://localhost:7002 上运行！
    echo.
    echo 请先启动应用程序：
    echo   mvn spring-boot:run
    echo.
    echo 启动应用程序后，请重新运行此脚本
    pause
    exit /b 1
)
echo ✅ 应用程序运行检查通过

:: 运行测试
echo.
echo [2/2] 开始运行Edge浏览器登录测试...
echo.
echo 测试将包括：
echo   - 管理员登录测试（admin/123）
echo   - 普通用户登录测试（moka/123）
echo   - 错误密码测试
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
    echo 测试特点：
    echo   - 自动检查应用程序运行状态
    echo   - 智能驱动管理（WebDriverManager + 本地备用）
    echo   - 失败时自动截图
    echo   - 测试完成后自动关闭浏览器
    echo   - 详细的日志输出
    echo.
) else (
    echo.
    echo ========================================
    echo ❌ 测试运行过程中出现错误
    echo ========================================
    echo.
    echo 请检查：
    echo   1. 应用程序是否正常运行
    echo   2. Edge浏览器是否已安装
    echo   3. 测试用户是否存在（admin/123, moka/123）
    echo   4. 网络连接是否正常
    echo.
)

echo 按任意键退出...
pause >nul
