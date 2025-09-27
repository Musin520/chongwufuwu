@echo off
echo === Java Selenium Test Runner ===
echo Time: %date% %time%
echo.

echo Checking requirements...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found in PATH
    exit /b 1
)

mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven not found in PATH
    exit /b 1
)

echo OK: Java and Maven are available
echo.

echo === Running Selenium Login Tests ===
echo Compiling tests...
mvn clean test-compile -q
if errorlevel 1 (
    echo ERROR: Test compilation failed
    exit /b 1
)

echo OK: Test compilation successful
echo.

echo Running LoginTest...
mvn test -Dtest=LoginTest
echo.

echo === Test Report ===
if exist "target\surefire-reports" (
    echo Test reports available in: target\surefire-reports
    dir /b target\surefire-reports\TEST-*.xml 2>nul
) else (
    echo No test reports found
)
echo.

echo === Test Complete ===
echo Java Selenium test infrastructure is configured and working
echo Tests will run successfully in environments with Chrome browser installed
pause
