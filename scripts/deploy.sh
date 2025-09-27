#!/bin/bash

# 部署脚本
# 用于自动化部署应用到不同环境

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 显示帮助信息
show_help() {
    echo "部署脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -e, --env ENV           部署环境 (dev|test|staging|prod)"
    echo "  -v, --version VERSION   应用版本号"
    echo "  -b, --build             是否重新构建镜像"
    echo "  -d, --database          是否初始化数据库"
    echo "  -h, --help              显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 -e dev -v 1.0.0 -b    # 部署到开发环境，版本1.0.0，重新构建"
    echo "  $0 -e prod -v 1.0.1      # 部署到生产环境，版本1.0.1"
}

# 默认参数
ENVIRONMENT=""
VERSION=""
BUILD_IMAGE=false
INIT_DATABASE=false

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -e|--env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -v|--version)
            VERSION="$2"
            shift 2
            ;;
        -b|--build)
            BUILD_IMAGE=true
            shift
            ;;
        -d|--database)
            INIT_DATABASE=true
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            log_error "未知参数: $1"
            show_help
            exit 1
            ;;
    esac
done

# 验证必需参数
if [[ -z "$ENVIRONMENT" ]]; then
    log_error "必须指定部署环境"
    show_help
    exit 1
fi

if [[ -z "$VERSION" ]]; then
    log_error "必须指定版本号"
    show_help
    exit 1
fi

# 验证环境
if [[ ! "$ENVIRONMENT" =~ ^(dev|test|staging|prod)$ ]]; then
    log_error "无效的环境: $ENVIRONMENT"
    log_info "支持的环境: dev, test, staging, prod"
    exit 1
fi

log_info "开始部署..."
log_info "环境: $ENVIRONMENT"
log_info "版本: $VERSION"
log_info "重新构建: $BUILD_IMAGE"
log_info "初始化数据库: $INIT_DATABASE"

# 设置环境变量
case $ENVIRONMENT in
    "dev")
        COMPOSE_FILE="docker-compose.yml"
        SPRING_PROFILE="docker"
        ;;
    "test")
        COMPOSE_FILE="docker-compose.test.yml"
        SPRING_PROFILE="test"
        ;;
    "staging")
        COMPOSE_FILE="docker-compose.staging.yml"
        SPRING_PROFILE="staging"
        ;;
    "prod")
        COMPOSE_FILE="docker-compose.prod.yml"
        SPRING_PROFILE="prod"
        ;;
esac

# 检查必需的文件
if [[ ! -f "$COMPOSE_FILE" ]]; then
    log_error "Docker Compose文件不存在: $COMPOSE_FILE"
    exit 1
fi

if [[ ! -f "Dockerfile" ]]; then
    log_error "Dockerfile不存在"
    exit 1
fi

# 构建应用
if $BUILD_IMAGE; then
    log_info "构建Maven项目..."
    if ! mvn clean package -DskipTests; then
        log_error "Maven构建失败"
        exit 1
    fi
    log_success "Maven构建完成"

    log_info "构建Docker镜像..."
    if ! docker build -t myapp:$VERSION .; then
        log_error "Docker镜像构建失败"
        exit 1
    fi
    
    # 标记为latest
    docker tag myapp:$VERSION myapp:latest
    log_success "Docker镜像构建完成"
fi

# 停止现有服务
log_info "停止现有服务..."
docker-compose -f $COMPOSE_FILE down || true

# 初始化数据库
if $INIT_DATABASE; then
    log_info "初始化数据库..."
    
    # 启动数据库服务
    docker-compose -f $COMPOSE_FILE up -d mysql
    
    # 等待数据库启动
    log_info "等待数据库启动..."
    sleep 30
    
    # 执行数据库初始化脚本
    if [[ -f "scripts/init-db.sql" ]]; then
        docker-compose -f $COMPOSE_FILE exec -T mysql mysql -uroot -proot123 < scripts/init-db.sql
        log_success "数据库初始化完成"
    else
        log_warning "数据库初始化脚本不存在"
    fi
fi

# 启动服务
log_info "启动服务..."
export APP_VERSION=$VERSION
export SPRING_PROFILES_ACTIVE=$SPRING_PROFILE

if ! docker-compose -f $COMPOSE_FILE up -d; then
    log_error "服务启动失败"
    exit 1
fi

# 等待服务启动
log_info "等待服务启动..."
sleep 60

# 健康检查
log_info "执行健康检查..."
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -f http://localhost:7002/actuator/health > /dev/null 2>&1; then
        log_success "应用健康检查通过"
        break
    fi
    
    RETRY_COUNT=$((RETRY_COUNT + 1))
    log_info "健康检查失败，重试 $RETRY_COUNT/$MAX_RETRIES..."
    sleep 10
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    log_error "应用健康检查失败"
    log_info "查看应用日志:"
    docker-compose -f $COMPOSE_FILE logs app
    exit 1
fi

# 显示服务状态
log_info "服务状态:"
docker-compose -f $COMPOSE_FILE ps

# 显示访问信息
log_success "部署完成！"
log_info "应用访问地址: http://localhost:7002"
log_info "健康检查地址: http://localhost:7002/actuator/health"

if [[ "$ENVIRONMENT" == "dev" ]]; then
    log_info "MinIO控制台: http://localhost:9001 (minioadmin/minioadmin123)"
fi

# 清理旧镜像
log_info "清理旧镜像..."
docker image prune -f

log_success "部署流程完成！"
