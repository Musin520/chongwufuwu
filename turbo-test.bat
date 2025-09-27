@echo off
echo ⚡⚡⚡ TURBO测试启动器 ⚡⚡⚡
echo.

:: 记录开始时间
set start_time=%time%

:: 快速检查应用程序（跳过详细检查）
echo [1/3] 快速检查应用程序...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:7002/login' -TimeoutSec 2 -UseBasicParsing; exit 0 } catch { exit 1 }" >nul 2>&1

if %errorlevel% neq 0 (
    echo ❌ 应用程序未运行！请先启动: mvn spring-boot:run
    pause
    exit /b 1
)
echo ✅ 应用程序运行中

:: 选择测试模式
echo.
echo [2/3] 选择测试模式:
echo   1. TURBO模式 - 极速启动 (推荐)
echo   2. FAST模式  - 快速启动
echo   3. 标准模式  - 正常启动
echo.
set /p choice="请选择 (1-3, 默认1): "
if "%choice%"=="" set choice=1

:: 根据选择运行不同的测试
echo.
echo [3/3] 启动测试...

if "%choice%"=="1" (
    echo 🚀 启动TURBO模式测试...
    mvn test -Dtest=TurboEdgeLoginTest -q
    set test_name=TURBO
) else if "%choice%"=="2" (
    echo 🏃 启动FAST模式测试...
    mvn test -Dtest=FastEdgeLoginTest -q
    set test_name=FAST
) else (
    echo 🚶 启动标准模式测试...
    mvn test -Dtest=SimpleEdgeLoginTest -q
    set test_name=标准
)

:: 计算耗时
set end_time=%time%
echo.
echo ========================================
if %errorlevel% eq 0 (
    echo ✅ %test_name%模式测试完成！
) else (
    echo ❌ %test_name%模式测试失败！
)
echo ========================================
echo.
echo 启动时间: %start_time%
echo 结束时间: %end_time%
echo.

:: 显示各模式特点
echo 💡 各模式特点:
echo   TURBO模式: 极速启动(1-2秒)，无头模式，最小资源消耗
echo   FAST模式:  快速启动(3-5秒)，无头模式，优化配置
echo   标准模式:  正常启动(5-10秒)，可视模式，完整功能
echo.

if %errorlevel% neq 0 (
    echo 🔧 故障排除:
    echo   - 确保应用程序正在运行
    echo   - 确保Edge浏览器已安装
    echo   - 检查网络连接
    echo   - 尝试其他测试模式
    echo.
)

echo 按任意键退出...
pause >nul
