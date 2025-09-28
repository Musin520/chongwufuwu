-- H2测试数据库测试数据初始化脚本

-- 清理现有数据（按依赖关系顺序删除）
DELETE FROM tmoney;
DELETE FROM orderevalute;
DELETE FROM userlike;
DELETE FROM shopcart;
DELETE FROM orders;
DELETE FROM product;
DELETE FROM producttype;
DELETE FROM company;
DELETE FROM message;
DELETE FROM webnotice;
DELETE FROM ttype;
DELETE FROM sysuser;

-- 插入测试用户数据
INSERT INTO sysuser (username, sex, phonenumber, account, password, idcard, address, img, role, age, remark, petname, petage, petdes, pettype) VALUES
('管理员', '男', '13800138000', 'admin', '123', '110101199001011234', '北京市朝阳区', '/images/admin.jpg', 'admin', 30, '系统管理员', NULL, NULL, NULL, NULL),
('卡卡', '女', '13800138001', 'kaka', '123', '110101199002021234', '北京市海淀区', '/images/kaka.jpg', 'user', 25, '普通用户', '小白', 3, '活泼可爱的小狗', '金毛'),
('张三', '男', '13800138002', 'zhangsan', '123', '110101199003031234', '北京市西城区', '/images/zhangsan.jpg', 'user', 28, '普通用户', '小黑', 2, '温顺的小猫', '英短'),
('李四', '女', '13800138003', 'lisi', '123', '110101199004041234', '北京市东城区', '/images/lisi.jpg', 'user', 26, '普通用户', '小花', 1, '可爱的小兔子', '荷兰兔'),
('卖家4', '男', '13800138004', 'maijia4', '123', '110101199005051234', '北京市丰台区', '/images/maijia4.jpg', 'company', 35, '服务商用户', NULL, NULL, NULL, NULL);

-- 插入公司数据（使用正确的用户ID引用）
INSERT INTO company (companyname, companydescription, companyaddress, companyphone, companyemail, companylicense, companylegal, companyimg, userid, avg_rating, rating_count, service_area) VALUES
('爱宠服务中心', '专业的宠物护理服务', '北京市朝阳区宠物街1号', '010-12345678', 'aichong@example.com', 'BJ001234567890', '王经理', '/images/company1.jpg', (SELECT id FROM sysuser WHERE account = 'maijia4'), 4.7, 156, '朝阳区、海淀区'),
('宠物乐园', '提供全方位宠物服务', '北京市海淀区宠物大道2号', '010-87654321', 'chongwu@example.com', 'BJ009876543210', '李经理', '/images/company2.jpg', NULL, 4.3, 89, '海淀区、西城区');

-- 插入产品类型数据
INSERT INTO producttype (typename, type_name, typedescription) VALUES
('宠物美容', '宠物美容', '专业的宠物美容护理服务'),
('宠物医疗', '宠物医疗', '宠物健康检查和医疗服务'),
('宠物寄养', '宠物寄养', '安全可靠的宠物寄养服务'),
('宠物训练', '宠物训练', '专业的宠物行为训练'),
('宠物用品', '宠物用品', '各类宠物生活用品');

-- 插入产品数据（使用正确的外键引用）
INSERT INTO product (productname, productdes, kedanjia, img, detailimg, companyid, producttype, service_start_time, service_end_time, status, chengben, kucun, fahuotianshu, chandi, guige) VALUES
('宠物洗澡美容', '专业宠物洗澡、修毛、美容服务', 88.00, '/images/product1.jpg', '["images/detail1.jpg","images/detail2.jpg"]', (SELECT id FROM company WHERE companyname = '爱宠服务中心'), (SELECT id FROM producttype WHERE typename = '宠物美容'), '09:00:00', '18:00:00', 'active', 60.00, 100, 1, '北京', '标准'),
('宠物健康检查', '全面的宠物健康体检服务', 150.00, '/images/product2.jpg', '["images/detail3.jpg","images/detail4.jpg"]', (SELECT id FROM company WHERE companyname = '爱宠服务中心'), (SELECT id FROM producttype WHERE typename = '宠物医疗'), '08:00:00', '17:00:00', 'active', 100.00, 50, 1, '北京', '专业'),
('宠物日托服务', '白天宠物托管照料服务', 120.00, '/images/product3.jpg', '["images/detail5.jpg"]', (SELECT id FROM company WHERE companyname = '宠物乐园'), (SELECT id FROM producttype WHERE typename = '宠物寄养'), '08:00:00', '19:00:00', 'active', 80.00, 20, 1, '北京', '日托'),
('宠物基础训练', '宠物基本行为训练课程', 200.00, '/images/product4.jpg', '["images/detail6.jpg"]', (SELECT id FROM company WHERE companyname = '爱宠服务中心'), (SELECT id FROM producttype WHERE typename = '宠物训练'), '10:00:00', '16:00:00', 'active', 150.00, 30, 1, '北京', '基础');

-- 插入订单数据（使用正确的外键引用）
INSERT INTO orders (ordernumber, userid, productid, quantity, totalprice, orderstatus, completetime) VALUES
('ORD20241001001', (SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM product WHERE productname = '宠物洗澡美容'), 1, 88.00, 'completed', '2024-10-01 15:30:00'),
('ORD20241002001', (SELECT id FROM sysuser WHERE account = 'zhangsan'), (SELECT id FROM product WHERE productname = '宠物健康检查'), 1, 150.00, 'completed', '2024-10-02 16:45:00'),
('ORD20241003001', (SELECT id FROM sysuser WHERE account = 'lisi'), (SELECT id FROM product WHERE productname = '宠物日托服务'), 1, 120.00, 'pending', NULL),
('ORD20241004001', (SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM product WHERE productname = '宠物基础训练'), 1, 200.00, 'processing', NULL);

-- 插入订单评价数据（使用正确的外键引用）
INSERT INTO orderevalute (orderid, userid, companyid, rating, comment) VALUES
((SELECT id FROM orders WHERE ordernumber = 'ORD20241001001'), (SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM company WHERE companyname = '爱宠服务中心'), 5, '服务非常好，小狗洗得很干净，工作人员很专业！'),
((SELECT id FROM orders WHERE ordernumber = 'ORD20241002001'), (SELECT id FROM sysuser WHERE account = 'zhangsan'), (SELECT id FROM company WHERE companyname = '爱宠服务中心'), 4, '医生很仔细，检查很全面，就是等待时间有点长。');

-- 插入购物车数据（使用正确的外键引用）
INSERT INTO shopcart (userid, productid, quantity) VALUES
((SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM product WHERE productname = '宠物日托服务'), 1),
((SELECT id FROM sysuser WHERE account = 'zhangsan'), (SELECT id FROM product WHERE productname = '宠物基础训练'), 1);

-- 插入用户收藏数据（使用正确的外键引用）
INSERT INTO userlike (userid, productid) VALUES
((SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM product WHERE productname = '宠物健康检查')),
((SELECT id FROM sysuser WHERE account = 'kaka'), (SELECT id FROM product WHERE productname = '宠物基础训练')),
((SELECT id FROM sysuser WHERE account = 'zhangsan'), (SELECT id FROM product WHERE productname = '宠物洗澡美容'));

-- 插入网站公告数据（使用正确的外键引用）
INSERT INTO webnotice (title, content, authorid) VALUES
('欢迎使用宠物服务平台', '我们致力于为您的宠物提供最优质的服务！', (SELECT id FROM sysuser WHERE account = 'admin')),
('国庆节服务安排通知', '国庆期间我们将正常营业，为您的宠物提供贴心服务。', (SELECT id FROM sysuser WHERE account = 'admin'));

-- 插入交易类型数据
INSERT INTO ttype (typename, typedescription) VALUES
('充值', '用户账户充值'),
('消费', '服务消费支出'),
('退款', '订单退款');

-- 插入资金流水数据（使用正确的外键引用）
INSERT INTO tmoney (userid, amount, transactiontype, description) VALUES
((SELECT id FROM sysuser WHERE account = 'kaka'), 500.00, '充值', '账户充值'),
((SELECT id FROM sysuser WHERE account = 'kaka'), -88.00, '消费', '宠物洗澡美容服务'),
((SELECT id FROM sysuser WHERE account = 'zhangsan'), 300.00, '充值', '账户充值'),
((SELECT id FROM sysuser WHERE account = 'zhangsan'), -150.00, '消费', '宠物健康检查服务');
