@echo off
echo ========================================
echo    Edge浏览器登录自动化测试
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
echo 选择要运行的Edge登录测试：
echo ========================================
echo.
echo 1. 运行完整Edge登录测试（推荐）
echo 2. 运行简化Edge登录测试（适合初学者）
echo 3. 运行管理员登录测试
echo 4. 运行普通用户登录测试
echo 5. 运行错误凭据测试
echo 6. 运行页面元素检查测试
echo 7. 运行完整登录流程测试
echo.

set /p testChoice="请选择测试选项 (1-7): "

echo.
echo ========================================
echo 开始运行Edge登录测试...
echo ========================================
echo.

if "%testChoice%"=="1" (
    echo 运行完整Edge登录测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest
) else if "%testChoice%"=="2" (
    echo 运行简化Edge登录测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.SimpleEdgeLoginTest
) else if "%testChoice%"=="3" (
    echo 运行管理员登录测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testAdminLogin
) else if "%testChoice%"=="4" (
    echo 运行普通用户登录测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testUserLogin
) else if "%testChoice%"=="5" (
    echo 运行错误凭据测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testInvalidLogin
) else if "%testChoice%"=="6" (
    echo 运行页面元素检查测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testLoginPageElements
) else if "%testChoice%"=="7" (
    echo 运行完整登录流程测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest#testCompleteLoginFlow
) else (
    echo ❌ 无效选择，运行完整Edge登录测试...
    mvn test -Dtest=com.javaPro.myProject.selenium.EdgeLoginTest
)

echo.
echo ========================================
echo 测试完成！
echo ========================================
echo.

if %errorlevel% equ 0 (
    echo ✅ 测试执行完成！
    echo 请查看上面的详细测试结果
) else (
    echo ❌ 测试执行过程中出现问题
    echo 请检查：
    echo 1. 应用程序是否正常运行在端口7002
    echo 2. Edge浏览器是否已安装
    echo 3. 测试用户是否存在于数据库中
)

echo.
echo 测试用户信息：
echo - 管理员：admin / 123
echo - 普通用户：kaka / 123  
echo - 服务商：maijia3 / 123
echo.
echo 按任意键退出...
pause > nul
