@echo off
echo ========================================
echo    Edge浏览器登录自动化测试运行脚本
echo ========================================
echo.

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo ❌ Java未安装或未配置到PATH中
    pause
    exit /b 1
)

echo.
echo 检查Maven环境...
mvn -version
if %errorlevel% neq 0 (
    echo ❌ Maven未安装或未配置到PATH中
    pause
    exit /b 1
)

echo.
echo 检查应用程序是否运行在端口7002...
netstat -an | findstr :7002 > nul
if %errorlevel% neq 0 (
    echo ⚠️ 应用程序可能未运行在端口7002
    echo 请确保先启动应用程序：mvn spring-boot:run
    echo.
    set /p choice="是否继续运行测试？(y/n): "
    if /i "%choice%" neq "y" (
        echo 测试已取消
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo 开始运行Edge浏览器登录测试...
echo ========================================
echo.

echo 选择要运行的测试：
echo 1. 运行所有Edge测试
echo 2. 只运行管理员登录测试
echo 3. 只运行普通用户登录测试
echo 4. 只运行服务商登录测试
echo 5. 只运行错误凭据测试
echo 6. 运行完整登录流程测试
echo 7. 运行简化测试（使用Edge）
echo 8. 运行可视化测试（使用Edge）
echo.

set /p testChoice="请选择测试选项 (1-8): "

if "%testChoice%"=="1" (
    echo 运行所有Edge测试...
    mvn test -Dtest=EdgeLoginTest
) else if "%testChoice%"=="2" (
    echo 运行管理员登录测试...
    mvn test -Dtest=EdgeLoginTest#testAdminLoginWithEdge
) else if "%testChoice%"=="3" (
    echo 运行普通用户登录测试...
    mvn test -Dtest=EdgeLoginTest#testUserLoginWithEdge
) else if "%testChoice%"=="4" (
    echo 运行服务商登录测试...
    mvn test -Dtest=EdgeLoginTest#testMerchantLoginWithEdge
) else if "%testChoice%"=="5" (
    echo 运行错误凭据测试...
    mvn test -Dtest=EdgeLoginTest#testInvalidLoginWithEdge
) else if "%testChoice%"=="6" (
    echo 运行完整登录流程测试...
    mvn test -Dtest=EdgeLoginTest#testCompleteLoginFlowWithEdge
) else if "%testChoice%"=="7" (
    echo 运行简化测试（使用Edge）...
    mvn test -Dtest=SimpleLoginTest
) else if "%testChoice%"=="8" (
    echo 运行可视化测试（使用Edge）...
    mvn test -Dtest=VisualLoginTest
) else (
    echo ❌ 无效选择，运行所有Edge测试...
    mvn test -Dtest=EdgeLoginTest
)

echo.
echo ========================================
echo 测试完成！
echo ========================================
echo.

if %errorlevel% equ 0 (
    echo ✅ 所有测试通过！
) else (
    echo ❌ 部分测试失败，请查看上面的详细信息
)

echo.
echo 按任意键退出...
pause > nul
