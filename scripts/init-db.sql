-- 数据库初始化脚本
-- 用于Docker容器启动时初始化数据库

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS chongwufuwu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE chongwufuwu;

-- 创建应用用户
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'app_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON chongwufuwu.* TO 'app_user'@'%';

-- 创建系统用户表
CREATE TABLE IF NOT EXISTS sysuser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL COMMENT '用户姓名',
    sex VARCHAR(10) COMMENT '性别',
    phonenumber VARCHAR(20) COMMENT '联系方式',
    account VARCHAR(50) UNIQUE NOT NULL COMMENT '账号',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    idcard VARCHAR(20) COMMENT '证件号',
    address VARCHAR(255) COMMENT '地址信息',
    img VARCHAR(500) COMMENT '图片',
    role VARCHAR(10) NOT NULL DEFAULT '2' COMMENT '角色（1管理员，2用户，3服务商）',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    age VARCHAR(10) COMMENT '年龄',
    petname VARCHAR(100) COMMENT '宠物名称',
    petage VARCHAR(10) COMMENT '宠物年龄',
    petdes TEXT COMMENT '宠物描述',
    pettype VARCHAR(50) COMMENT '宠物类型',
    remark TEXT COMMENT '备注信息',
    money DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额',
    INDEX idx_account (account),
    INDEX idx_role (role),
    INDEX idx_createtime (createtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 创建公司表
CREATE TABLE IF NOT EXISTS company (
    id INT AUTO_INCREMENT PRIMARY KEY,
    companyname VARCHAR(200) NOT NULL COMMENT '公司名称',
    faren VARCHAR(100) COMMENT '法人',
    phonenumber VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '公司地址',
    yingyezhizhaohao VARCHAR(100) COMMENT '营业执照号',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态（0待审核，1已审核，2已拒绝）',
    createid INT COMMENT '创建人ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    remark TEXT COMMENT '备注',
    INDEX idx_status (status),
    INDEX idx_createid (createid),
    FOREIGN KEY (createid) REFERENCES sysuser(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司表';

-- 创建产品类型表
CREATE TABLE IF NOT EXISTS producttype (
    id INT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(100) NOT NULL COMMENT '类型名称',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    remark TEXT COMMENT '备注',
    INDEX idx_typename (typename)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品类型表';

-- 创建产品表
CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productname VARCHAR(200) NOT NULL COMMENT '产品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    img VARCHAR(500) COMMENT '主图片',
    detailimg TEXT COMMENT '详情图片JSON',
    productdes TEXT COMMENT '产品描述',
    typeid INT COMMENT '类型ID',
    createid INT COMMENT '创建人ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status VARCHAR(10) DEFAULT '1' COMMENT '状态（1上架，0下架）',
    remark TEXT COMMENT '备注',
    INDEX idx_productname (productname),
    INDEX idx_typeid (typeid),
    INDEX idx_createid (createid),
    INDEX idx_status (status),
    FOREIGN KEY (typeid) REFERENCES producttype(id) ON DELETE SET NULL,
    FOREIGN KEY (createid) REFERENCES sysuser(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- 创建购物车表
CREATE TABLE IF NOT EXISTS shopcart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT NOT NULL COMMENT '用户ID',
    productid INT NOT NULL COMMENT '产品ID',
    num INT DEFAULT 1 COMMENT '数量',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_userid (userid),
    INDEX idx_productid (productid),
    FOREIGN KEY (userid) REFERENCES sysuser(id) ON DELETE CASCADE,
    FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    orderno VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    userid INT NOT NULL COMMENT '用户ID',
    productid INT NOT NULL COMMENT '产品ID',
    num INT DEFAULT 1 COMMENT '数量',
    totalprice DECIMAL(10,2) NOT NULL COMMENT '总价',
    status VARCHAR(20) DEFAULT '待付款' COMMENT '订单状态',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    paytime TIMESTAMP NULL COMMENT '支付时间',
    finishtime TIMESTAMP NULL COMMENT '完成时间',
    remark TEXT COMMENT '备注',
    INDEX idx_orderno (orderno),
    INDEX idx_userid (userid),
    INDEX idx_productid (productid),
    INDEX idx_status (status),
    FOREIGN KEY (userid) REFERENCES sysuser(id) ON DELETE CASCADE,
    FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建充值记录表
CREATE TABLE IF NOT EXISTS tmoney (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT NOT NULL COMMENT '用户ID',
    money DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    auditstatus VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    audittime TIMESTAMP NULL COMMENT '审核时间',
    remark TEXT COMMENT '备注',
    INDEX idx_userid (userid),
    INDEX idx_auditstatus (auditstatus),
    FOREIGN KEY (userid) REFERENCES sysuser(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值记录表';

-- 创建用户收藏表
CREATE TABLE IF NOT EXISTS userlike (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT NOT NULL COMMENT '用户ID',
    productid INT NOT NULL COMMENT '产品ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    spare1 VARCHAR(100) COMMENT '备用字段1',
    productname VARCHAR(200) COMMENT '产品名称',
    img VARCHAR(500) COMMENT '产品图片',
    UNIQUE KEY uk_user_product (userid, productid),
    INDEX idx_userid (userid),
    INDEX idx_productid (productid),
    FOREIGN KEY (userid) REFERENCES sysuser(id) ON DELETE CASCADE,
    FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- 创建网站公告表
CREATE TABLE IF NOT EXISTS webnotice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    img VARCHAR(500) COMMENT '公告图片',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status VARCHAR(10) DEFAULT '1' COMMENT '状态（1显示，0隐藏）',
    INDEX idx_status (status),
    INDEX idx_createtime (createtime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网站公告表';

-- 创建消息表
CREATE TABLE IF NOT EXISTS message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    type VARCHAR(20) DEFAULT 'system' COMMENT '消息类型',
    status VARCHAR(10) DEFAULT '0' COMMENT '状态（0未读，1已读）',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_userid (userid),
    INDEX idx_status (status),
    INDEX idx_type (type),
    FOREIGN KEY (userid) REFERENCES sysuser(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 插入初始数据
-- 插入管理员用户
INSERT IGNORE INTO sysuser (username, account, password, role, phonenumber, sex) VALUES 
('系统管理员', 'admin', 'admin123', '1', '13800138000', '男');

-- 插入产品类型
INSERT IGNORE INTO producttype (typename) VALUES 
('宠物美容'), ('宠物寄养'), ('宠物医疗'), ('宠物用品'), ('宠物训练');

-- 插入测试公告
INSERT IGNORE INTO webnotice (title, content) VALUES 
('欢迎使用宠物服务平台', '欢迎来到我们的宠物服务平台，这里为您提供最优质的宠物服务！');

-- 刷新权限
FLUSH PRIVILEGES;
