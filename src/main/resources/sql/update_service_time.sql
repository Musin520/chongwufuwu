-- 为没有设置服务时间段的产品添加时间段数据
-- 这个脚本会为所有service_start_time或service_end_time为空的产品随机分配时间段

-- 更新产品服务时间段
UPDATE product 
SET 
    service_start_time = CASE 
        WHEN MOD(id, 4) = 0 THEN '08:00'  -- 上午服务
        WHEN MOD(id, 4) = 1 THEN '12:00'  -- 下午服务
        WHEN MOD(id, 4) = 2 THEN '18:00'  -- 晚上服务
        ELSE '08:00'                      -- 全天服务开始
    END,
    service_end_time = CASE 
        WHEN MOD(id, 4) = 0 THEN '12:00'  -- 上午服务结束
        WHEN MOD(id, 4) = 1 THEN '18:00'  -- 下午服务结束
        WHEN MOD(id, 4) = 2 THEN '22:00'  -- 晚上服务结束
        ELSE '22:00'                      -- 全天服务结束
    END
WHERE service_start_time IS NULL OR service_end_time IS NULL OR service_start_time = '' OR service_end_time = '';

-- 为了测试筛选功能，确保有足够的数据分布
-- 再次更新一些产品为特定时间段

-- 设置一些产品为上午服务 (08:00-12:00)
UPDATE product 
SET service_start_time = '08:00', service_end_time = '12:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 5
    ) AS temp
);

-- 设置一些产品为下午服务 (12:00-18:00)
UPDATE product 
SET service_start_time = '12:00', service_end_time = '18:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 5 OFFSET 5
    ) AS temp
);

-- 设置一些产品为晚上服务 (18:00-22:00)
UPDATE product 
SET service_start_time = '18:00', service_end_time = '22:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 5 OFFSET 10
    ) AS temp
);

-- 设置一些产品为全天服务 (08:00-22:00)
UPDATE product 
SET service_start_time = '08:00', service_end_time = '22:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 5 OFFSET 15
    ) AS temp
);

-- 设置一些产品为早上到下午 (09:00-17:00)
UPDATE product 
SET service_start_time = '09:00', service_end_time = '17:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 3 OFFSET 20
    ) AS temp
);

-- 设置一些产品为下午到晚上 (14:00-21:00)
UPDATE product 
SET service_start_time = '14:00', service_end_time = '21:00'
WHERE id IN (
    SELECT * FROM (
        SELECT id FROM product ORDER BY id LIMIT 3 OFFSET 23
    ) AS temp
);

-- 查询更新结果
SELECT 
    id, 
    productname, 
    service_start_time, 
    service_end_time,
    CASE 
        WHEN service_start_time = '08:00' AND service_end_time = '12:00' THEN '上午服务'
        WHEN service_start_time = '12:00' AND service_end_time = '18:00' THEN '下午服务'
        WHEN service_start_time = '18:00' AND service_end_time = '22:00' THEN '晚上服务'
        WHEN service_start_time = '08:00' AND service_end_time = '22:00' THEN '全天服务'
        ELSE '自定义时间段'
    END AS service_period
FROM product 
WHERE service_start_time IS NOT NULL AND service_end_time IS NOT NULL
ORDER BY id;
