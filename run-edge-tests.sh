#!/bin/bash

echo "========================================"
echo "   Edge浏览器登录自动化测试运行脚本"
echo "========================================"
echo

echo "检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "❌ Java未安装或未配置到PATH中"
    exit 1
fi
java -version

echo
echo "检查Maven环境..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven未安装或未配置到PATH中"
    exit 1
fi
mvn -version

echo
echo "检查应用程序是否运行在端口7002..."
if ! netstat -an 2>/dev/null | grep -q :7002 && ! ss -an 2>/dev/null | grep -q :7002; then
    echo "⚠️ 应用程序可能未运行在端口7002"
    echo "请确保先启动应用程序：mvn spring-boot:run"
    echo
    read -p "是否继续运行测试？(y/n): " choice
    if [[ "$choice" != "y" && "$choice" != "Y" ]]; then
        echo "测试已取消"
        exit 1
    fi
fi

echo
echo "========================================"
echo "开始运行Edge浏览器登录测试..."
echo "========================================"
echo

echo "选择要运行的测试："
echo "1. 运行所有Edge测试"
echo "2. 只运行管理员登录测试"
echo "3. 只运行普通用户登录测试"
echo "4. 只运行服务商登录测试"
echo "5. 只运行错误凭据测试"
echo "6. 运行完整登录流程测试"
echo "7. 运行简化测试（使用Edge）"
echo "8. 运行可视化测试（使用Edge）"
echo

read -p "请选择测试选项 (1-8): " testChoice

case $testChoice in
    1)
        echo "运行所有Edge测试..."
        mvn test -Dtest=EdgeLoginTest
        ;;
    2)
        echo "运行管理员登录测试..."
        mvn test -Dtest=EdgeLoginTest#testAdminLoginWithEdge
        ;;
    3)
        echo "运行普通用户登录测试..."
        mvn test -Dtest=EdgeLoginTest#testUserLoginWithEdge
        ;;
    4)
        echo "运行服务商登录测试..."
        mvn test -Dtest=EdgeLoginTest#testMerchantLoginWithEdge
        ;;
    5)
        echo "运行错误凭据测试..."
        mvn test -Dtest=EdgeLoginTest#testInvalidLoginWithEdge
        ;;
    6)
        echo "运行完整登录流程测试..."
        mvn test -Dtest=EdgeLoginTest#testCompleteLoginFlowWithEdge
        ;;
    7)
        echo "运行简化测试（使用Edge）..."
        mvn test -Dtest=SimpleLoginTest
        ;;
    8)
        echo "运行可视化测试（使用Edge）..."
        mvn test -Dtest=VisualLoginTest
        ;;
    *)
        echo "❌ 无效选择，运行所有Edge测试..."
        mvn test -Dtest=EdgeLoginTest
        ;;
esac

echo
echo "========================================"
echo "测试完成！"
echo "========================================"
echo

if [ $? -eq 0 ]; then
    echo "✅ 所有测试通过！"
else
    echo "❌ 部分测试失败，请查看上面的详细信息"
fi

echo
echo "按Enter键退出..."
read
