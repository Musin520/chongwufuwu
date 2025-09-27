#!/bin/bash

echo "=== 开始自动化测试 ==="
echo "时间: $(date)"
echo ""

# 设置错误处理
set -e

# 检查必要的工具
check_requirements() {
    echo "检查系统要求..."

    if ! command -v java &> /dev/null; then
        echo "✗ Java未安装或不在PATH中"
        exit 1
    fi

    if ! command -v mvn &> /dev/null; then
        echo "✗ Maven未安装或不在PATH中"
        exit 1
    fi

    echo "✓ Java版本: $(java -version 2>&1 | head -n 1)"
    echo "✓ Maven版本: $(mvn -version 2>&1 | head -n 1)"
    echo ""
}

# 运行Java配置检查
run_config_check() {
    echo "=== 运行Selenium配置检查 ==="

    # 编译项目
    echo "编译项目..."
    if mvn clean compile -q; then
        echo "✓ 项目编译成功"
    else
        echo "✗ 项目编译失败"
        exit 1
    fi

    # 运行配置检查
    echo "运行配置检查..."
    if mvn exec:java -Dexec.mainClass="ConfigCheck" -q; then
        echo "✓ Selenium配置检查通过"
    else
        echo "⚠ Selenium配置检查失败，但继续运行测试"
    fi
    echo ""
}

# 运行集成测试
run_integration_tests() {
    echo "=== 运行Spring Boot集成测试 ==="

    if mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=test; then
        echo "✓ 集成测试成功完成"
        return 0
    else
        echo "✗ 集成测试失败"
        return 1
    fi
}

# 运行Selenium测试
run_selenium_tests() {
    echo "=== 运行Selenium UI测试 ==="

    if mvn test -Pselenium-tests -Dspring.profiles.active=test; then
        echo "✓ Selenium测试成功完成"
        return 0
    else
        echo "✗ Selenium测试失败"
        return 1
    fi
}

# 运行所有测试
run_all_tests() {
    echo "=== 运行所有测试 ==="

    if mvn test -Dspring.profiles.active=test; then
        echo "✓ 所有测试成功完成"
        return 0
    else
        echo "✗ 部分测试失败"
        return 1
    fi
}

# 主函数
main() {
    check_requirements

    # 首先尝试Python版本（如果存在）
    if [ -f "run_selenium_test.py" ]; then
        echo "=== 尝试运行Python Selenium测试 ==="
        if command -v python3 &> /dev/null && python3 run_selenium_test.py; then
            echo "✓ Python测试成功完成"
            echo ""
        else
            echo "⚠ Python测试失败或不可用，切换到Java版本"
            echo ""
        fi
    fi

    # 运行Java测试
    run_config_check

    # 根据参数选择测试类型
    case "${1:-all}" in
        "integration")
            run_integration_tests
            ;;
        "selenium")
            run_selenium_tests
            ;;
        "all"|"")
            integration_result=0
            selenium_result=0

            echo "运行集成测试..."
            run_integration_tests || integration_result=1
            echo ""

            echo "运行Selenium测试..."
            run_selenium_tests || selenium_result=1
            echo ""

            if [ $integration_result -eq 0 ] && [ $selenium_result -eq 0 ]; then
                echo "✓ 所有测试都成功完成"
                exit 0
            elif [ $integration_result -eq 0 ]; then
                echo "✓ 集成测试成功，⚠ Selenium测试失败"
                exit 1
            elif [ $selenium_result -eq 0 ]; then
                echo "⚠ 集成测试失败，✓ Selenium测试成功"
                exit 1
            else
                echo "✗ 所有测试都失败了"
                exit 1
            fi
            ;;
        *)
            echo "用法: $0 [integration|selenium|all]"
            echo "  integration - 只运行集成测试"
            echo "  selenium    - 只运行Selenium测试"
            echo "  all         - 运行所有测试（默认）"
            exit 1
            ;;
    esac
}

# 运行主函数
main "$@"