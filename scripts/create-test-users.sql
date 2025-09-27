-- 创建测试用户脚本
-- 用于三角色登录测试

USE chongwufuwu;

-- 删除可能存在的测试用户（避免重复）
DELETE FROM sysuser WHERE account IN ('admin', 'user', 'service');

-- 插入管理员用户 (role = '1')
INSERT INTO sysuser (username, account, password, role, phonenumber, sex) VALUES 
('系统管理员', 'admin', '123', '1', '13800138000', '男');

-- 插入普通用户 (role = '2') 
INSERT INTO sysuser (username, account, password, role, phonenumber, sex) VALUES 
('测试用户', 'user', '123', '2', '13800138001', '女');

-- 插入服务商用户 (role = '3')
INSERT INTO sysuser (username, account, password, role, phonenumber, sex) VALUES 
('测试服务商', 'service', '123', '3', '13800138002', '男');

-- 为服务商用户创建对应的公司记录
INSERT INTO company (id, companyname, phonenumber, createid, status) 
SELECT id, CONCAT(username, '的服务商店'), phonenumber, id, '1' 
FROM sysuser WHERE account = 'service';

-- 查看创建的用户
SELECT id, username, account, role, 
       CASE role 
           WHEN '1' THEN '管理员'
           WHEN '2' THEN '普通用户' 
           WHEN '3' THEN '服务商'
           ELSE '未知角色'
       END as role_name
FROM sysuser 
WHERE account IN ('admin', 'user', 'service')
ORDER BY role;

COMMIT;
