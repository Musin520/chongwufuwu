-- 安全的数据库字段检查和添加脚本
USE chongwufuwu;

-- 检查并添加product表字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'product' 
     AND COLUMN_NAME = 'service_start_time') = 0,
    'ALTER TABLE product ADD COLUMN service_start_time VARCHAR(10) COMMENT ''服务开始时间'' AFTER detailimg',
    'SELECT ''service_start_time字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'product' 
     AND COLUMN_NAME = 'service_end_time') = 0,
    'ALTER TABLE product ADD COLUMN service_end_time VARCHAR(10) COMMENT ''服务结束时间'' AFTER service_start_time',
    'SELECT ''service_end_time字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加company表字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'company' 
     AND COLUMN_NAME = 'service_time') = 0,
    'ALTER TABLE company ADD COLUMN service_time TEXT COMMENT ''服务时间段（JSON格式）'' AFTER createid',
    'SELECT ''service_time字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'company' 
     AND COLUMN_NAME = 'avg_rating') = 0,
    'ALTER TABLE company ADD COLUMN avg_rating DECIMAL(3,2) DEFAULT 0.00 COMMENT ''平均评分'' AFTER service_time',
    'SELECT ''avg_rating字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'company' 
     AND COLUMN_NAME = 'rating_count') = 0,
    'ALTER TABLE company ADD COLUMN rating_count INT DEFAULT 0 COMMENT ''评价总数'' AFTER avg_rating',
    'SELECT ''rating_count字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'company' 
     AND COLUMN_NAME = 'service_area') = 0,
    'ALTER TABLE company ADD COLUMN service_area VARCHAR(500) COMMENT ''服务区域'' AFTER rating_count',
    'SELECT ''service_area字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加order_evalute表字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'chongwufuwu' 
     AND TABLE_NAME = 'order_evalute' 
     AND COLUMN_NAME = 'rating') = 0,
    'ALTER TABLE order_evalute ADD COLUMN rating INT DEFAULT 5 COMMENT ''评分(1-5星)'' AFTER companyid',
    'SELECT ''rating字段已存在'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新示例数据
UPDATE product SET 
service_start_time = '09:00',
service_end_time = '18:00'
WHERE service_start_time IS NULL OR service_start_time = '';

UPDATE company SET 
service_time = '{"weekdays": "09:00-18:00", "weekends": "10:00-17:00"}',
avg_rating = 4.5,
rating_count = 10,
service_area = '全市范围'
WHERE service_time IS NULL OR service_time = '';

-- 显示最终的表结构
SELECT 'product表结构:' as info;
DESCRIBE product;

SELECT 'company表结构:' as info;
DESCRIBE company;

SELECT 'order_evalute表结构:' as info;
DESCRIBE order_evalute;
