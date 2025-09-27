@echo off
echo ========================================
echo 启动可视化登录自动化测试
echo ========================================

echo.
echo 第一步：检查Java和Maven环境
java -version
echo.
mvn -version

echo.
echo 第二步：编译项目
mvn clean compile test-compile

echo.
echo 第三步：启动Spring Boot应用（后台运行）
echo 正在启动应用服务器...
start /B mvn spring-boot:run

echo.
echo 等待应用启动（30秒）...
timeout /t 30 /nobreak

echo.
echo 第四步：运行可视化登录测试
echo 注意：浏览器将会打开并执行自动化测试
echo 请观察浏览器中的登录流程
mvn test -Dtest=VisualLoginTest

echo.
echo 第五步：清理（关闭应用服务器）
echo 正在关闭应用服务器...
taskkill /f /im java.exe 2>nul

echo.
echo ========================================
echo 测试完成！
echo ========================================
pause
