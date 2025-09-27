# 服务者筛选功能说明

## 功能概述

本功能允许用户根据时间、价格区间、评分等条件筛选合适的宠物服务提供者，提供了灵活的筛选条件和用户友好的界面。

## 主要特性

### 1. 多维度筛选条件
- **时间筛选**：支持按服务时间段筛选（上午、下午、晚上、全天）
- **价格区间筛选**：支持设置最低价格和最高价格
- **评分筛选**：支持按服务商最低评分筛选
- **服务类型筛选**：支持按宠物服务类型筛选
- **关键词搜索**：支持按服务名称关键词搜索
- **排序功能**：支持按价格、评分等排序

### 2. 用户界面
- 响应式设计，支持桌面和移动设备
- 直观的筛选面板
- 实时的价格区间滑块
- 星级评分显示
- 分页显示结果

## API接口

### 1. POST /product/filterServices
用于复杂筛选条件的服务筛选

**请求体示例：**
```json
{
    "serviceStartTime": "09:00",
    "serviceEndTime": "18:00",
    "minPrice": 50.0,
    "maxPrice": 300.0,
    "minRating": 4.0,
    "productType": 1,
    "keyword": "宠物美容",
    "sortBy": "price_asc",
    "pageNum": 1,
    "pageSize": 10
}
```

**响应示例：**
```json
{
    "code": 200,
    "msg": "查询成功",
    "data": {
        "list": [
            {
                "id": 1,
                "productname": "宠物基础洗护",
                "productdes": "包含洗澡、吹干、基础造型",
                "kedanjia": "80",
                "serviceStartTime": "08:00",
                "serviceEndTime": "12:00",
                "companyName": "爱宠美容中心",
                "companyRating": 4.8,
                "companyRatingCount": 156
            }
        ],
        "total": 15,
        "pageNum": 1,
        "pageSize": 10
    }
}
```

### 2. GET /product/filterServices
用于URL参数传递的简单筛选

**请求示例：**
```
GET /product/filterServices?serviceStartTime=09:00&serviceEndTime=18:00&minPrice=50&maxPrice=200&minRating=4.0&keyword=宠物&sortBy=price_asc&pageNum=1&pageSize=10
```

## 数据库设计

### 1. Product表扩展
已为Product实体类添加了以下筛选相关字段：
- `minPrice`: 最低价格（查询时使用）
- `maxPrice`: 最高价格（查询时使用）
- `serviceStartTime`: 服务开始时间
- `serviceEndTime`: 服务结束时间

### 2. SQL查询优化
- 添加了价格区间筛选的SQL条件
- 优化了时间段筛选逻辑，支持全天服务在各时间段显示
- 添加了相关索引以提高查询性能

## 使用方法

### 1. 访问筛选页面
访问 `/test/service-filter` 可以看到完整的筛选界面

### 2. 设置筛选条件
- 选择服务时间段
- 拖动价格滑块设置价格区间
- 选择最低评分要求
- 选择服务类型
- 输入关键词
- 选择排序方式

### 3. 查看结果
- 点击"应用筛选"按钮执行筛选
- 结果以卡片形式展示
- 支持分页浏览
- 可以查看服务详情或添加到购物车

## 测试数据

运行以下SQL脚本可以插入测试数据：
```sql
-- 执行脚本
source scripts/insert-demo-data-for-filter.sql;
```

测试数据包括：
- 5个服务类型
- 5个服务商（不同评分）
- 15个服务项目（不同时间段和价格）
- 相关的评价数据

## 测试用例

项目包含完整的测试用例，位于：
`src/test/java/com/javaPro/myProject/modules/product/controller/ProductControllerTest.java`

测试覆盖：
- 基本查询功能
- 时间筛选
- 价格区间筛选
- 评分筛选
- 复合条件筛选
- GET和POST接口
- 边界条件测试

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ProductControllerTest

# 运行特定测试方法
mvn test -Dtest=ProductControllerTest#testFilterByPriceRange
```

## 技术实现

### 1. 后端技术栈
- Spring Boot
- MyBatis
- MySQL
- PageHelper（分页）

### 2. 前端技术栈
- HTML5 + CSS3
- Bootstrap 5
- jQuery + jQuery UI
- AJAX

### 3. 关键实现点
- DTO模式封装筛选条件
- 动态SQL构建筛选查询
- 前后端分离的API设计
- 响应式UI设计

## 扩展建议

1. **地理位置筛选**：添加基于距离的筛选功能
2. **高级排序**：支持多字段组合排序
3. **筛选历史**：保存用户的筛选偏好
4. **实时筛选**：输入时实时更新结果
5. **筛选统计**：显示各筛选条件下的结果数量

## 注意事项

1. 确保数据库中有足够的测试数据
2. 价格字段在数据库中存储为VARCHAR，查询时需要转换为DECIMAL
3. 时间筛选支持跨时间段的服务显示
4. 分页参数需要合理设置以避免性能问题
5. 前端需要处理网络请求的错误情况
