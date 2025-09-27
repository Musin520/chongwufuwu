-- 添加服务时间字段到product表
-- 这个脚本用于为现有的product表添加服务时间相关字段

USE chongwufuwu;

-- 添加服务开始时间字段
ALTER TABLE product ADD COLUMN service_start_time TIME COMMENT '服务开始时间';

-- 添加服务结束时间字段  
ALTER TABLE product ADD COLUMN service_end_time TIME COMMENT '服务结束时间';

-- 为现有数据设置默认的服务时间（可选）
-- 这里设置一个通用的服务时间范围：上午9点到下午6点
UPDATE product SET 
    service_start_time = '09:00:00',
    service_end_time = '18:00:00'
WHERE service_start_time IS NULL OR service_end_time IS NULL;

-- 添加索引以提高查询性能
CREATE INDEX idx_service_time ON product(service_start_time, service_end_time);

-- 显示表结构确认字段已添加
DESCRIBE product;
