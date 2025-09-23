-- 检查数据库表结构的SQL脚本
-- 用于验证数据库是否包含所需的字段

-- 检查product表结构
DESCRIBE product;

-- 检查company表结构  
DESCRIBE company;

-- 检查order_evalute表结构
DESCRIBE order_evalute;

-- 检查是否有基础数据
SELECT COUNT(*) as product_count FROM product;
SELECT COUNT(*) as company_count FROM company;
SELECT COUNT(*) as user_count FROM sysuser;

-- 检查product表是否有必要的字段
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'chongwufuwu' 
AND TABLE_NAME = 'product';

-- 检查company表是否有必要的字段
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'chongwufuwu' 
AND TABLE_NAME = 'company';
