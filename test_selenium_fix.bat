@echo off
echo ========================================
echo 测试Selenium修复 - 浏览器可视化测试
echo ========================================

echo.
echo 第一步：检查Java和Maven环境
java -version
echo.
mvn -version

echo.
echo 第二步：编译测试代码
mvn clean compile test-compile -q

echo.
echo 第三步：运行可视化登录测试
echo 注意：浏览器应该会自动打开并执行测试
echo 如果浏览器没有打开，请检查WebDriver配置
mvn test -Dtest=VisualLoginTest -q

echo.
echo ========================================
echo 测试完成！
echo ========================================
pause
