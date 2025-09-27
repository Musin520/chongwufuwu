@echo off
echo ========================================
echo 登录自动化测试运行脚本
echo ========================================

echo.
echo 检查应用程序是否运行...
curl -s -o nul -w "%%{http_code}" http://localhost:7002/login > temp_response.txt
set /p response=<temp_response.txt
del temp_response.txt

if "%response%"=="200" (
    echo ✅ 应用程序正在运行 (HTTP %response%)
) else if "%response%"=="302" (
    echo ✅ 应用程序正在运行 (HTTP %response%)
) else (
    echo ❌ 应用程序未运行或无法访问 (HTTP %response%)
    echo.
    echo 请先启动应用程序：
    echo   mvn spring-boot:run
    echo.
    echo 然后重新运行此脚本
    pause
    exit /b 1
)

echo.
echo ========================================
echo 运行登录自动化测试
echo ========================================

echo.
echo 选择要运行的测试：
echo 1. 简化登录测试 (推荐)
echo 2. 完整可视化登录测试
echo 3. 运行所有测试
echo.
set /p choice="请输入选择 (1-3): "

if "%choice%"=="1" (
    echo.
    echo 运行简化登录测试...
    mvn test -Dtest=SimpleLoginTest
) else if "%choice%"=="2" (
    echo.
    echo 运行完整可视化登录测试...
    mvn test -Dtest=VisualLoginTest
) else if "%choice%"=="3" (
    echo.
    echo 运行所有登录测试...
    mvn test -Dtest=*LoginTest
) else (
    echo 无效选择，运行简化登录测试...
    mvn test -Dtest=SimpleLoginTest
)

echo.
echo ========================================
echo 测试完成
echo ========================================
pause
