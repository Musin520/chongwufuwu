-- 数据库字段添加脚本
-- 为支持高级功能添加必要的字段

USE chongwufuwu;

-- 1. 为product表添加服务时间字段
ALTER TABLE product 
ADD COLUMN service_start_time VARCHAR(10) COMMENT '服务开始时间' AFTER detailimg,
ADD COLUMN service_end_time VARCHAR(10) COMMENT '服务结束时间' AFTER service_start_time;

-- 2. 为company表添加服务商相关字段
ALTER TABLE company 
ADD COLUMN service_time TEXT COMMENT '服务时间段（JSON格式）' AFTER createid,
ADD COLUMN avg_rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '平均评分' AFTER service_time,
ADD COLUMN rating_count INT DEFAULT 0 COMMENT '评价总数' AFTER avg_rating,
ADD COLUMN service_area VARCHAR(500) COMMENT '服务区域' AFTER rating_count;

-- 3. 确保order_evalute表有rating字段（如果不存在则添加）
-- 先检查字段是否存在，如果不存在则添加
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

-- 4. 为product表添加一些示例数据的服务时间
UPDATE product SET 
service_start_time = '09:00',
service_end_time = '18:00'
WHERE service_start_time IS NULL;

-- 5. 为company表添加一些示例数据
UPDATE company SET 
service_time = '{"weekdays": "09:00-18:00", "weekends": "10:00-17:00"}',
avg_rating = 4.5,
rating_count = 10,
service_area = '全市范围'
WHERE service_time IS NULL;

-- 6. 创建触发器来自动更新company表的评分统计
DELIMITER $$

CREATE TRIGGER update_company_rating_after_insert
AFTER INSERT ON order_evalute
FOR EACH ROW
BEGIN
    UPDATE company 
    SET avg_rating = (
        SELECT AVG(rating) 
        FROM order_evalute 
        WHERE companyid = NEW.companyid
    ),
    rating_count = (
        SELECT COUNT(*) 
        FROM order_evalute 
        WHERE companyid = NEW.companyid
    )
    WHERE id = NEW.companyid;
END$$

CREATE TRIGGER update_company_rating_after_update
AFTER UPDATE ON order_evalute
FOR EACH ROW
BEGIN
    UPDATE company 
    SET avg_rating = (
        SELECT AVG(rating) 
        FROM order_evalute 
        WHERE companyid = NEW.companyid
    ),
    rating_count = (
        SELECT COUNT(*) 
        FROM order_evalute 
        WHERE companyid = NEW.companyid
    )
    WHERE id = NEW.companyid;
END$$

CREATE TRIGGER update_company_rating_after_delete
AFTER DELETE ON order_evalute
FOR EACH ROW
BEGIN
    UPDATE company 
    SET avg_rating = COALESCE((
        SELECT AVG(rating) 
        FROM order_evalute 
        WHERE companyid = OLD.companyid
    ), 0),
    rating_count = (
        SELECT COUNT(*) 
        FROM order_evalute 
        WHERE companyid = OLD.companyid
    )
    WHERE id = OLD.companyid;
END$$

DELIMITER ;

-- 7. 验证字段是否添加成功
SELECT 'product表字段检查:' as info;
DESCRIBE product;

SELECT 'company表字段检查:' as info;
DESCRIBE company;

SELECT 'order_evalute表字段检查:' as info;
DESCRIBE order_evalute;
