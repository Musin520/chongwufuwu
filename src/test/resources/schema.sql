-- H2测试数据库初始化脚本
-- 创建用户表
CREATE TABLE IF NOT EXISTS sysuser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    sex VARCHAR(10),
    phonenumber VARCHAR(20),
    account VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    idcard VARCHAR(20),
    address VARCHAR(200),
    img VARCHAR(500),
    role VARCHAR(20) DEFAULT 'user',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    age INT,
    remark VARCHAR(500),
    money DECIMAL(10,2) DEFAULT 0.00,
    petname VARCHAR(50),
    petage INT,
    petdes VARCHAR(500),
    pettype VARCHAR(50)
);

-- 创建公司表
CREATE TABLE IF NOT EXISTS company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    companyname VARCHAR(100) NOT NULL,
    companydescription TEXT,
    companyaddress VARCHAR(200),
    companyphone VARCHAR(20),
    companyemail VARCHAR(100),
    companylicense VARCHAR(100),
    companylegal VARCHAR(50),
    companyimg VARCHAR(500),
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active',
    userid BIGINT,
    FOREIGN KEY (userid) REFERENCES sysuser(id)
);

-- 创建产品类型表
CREATE TABLE IF NOT EXISTS producttype (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(50) NOT NULL,
    typedescription VARCHAR(200),
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建产品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    productname VARCHAR(100) NOT NULL,
    productdescription TEXT,
    productprice DECIMAL(10,2) NOT NULL,
    productimg VARCHAR(500),
    productdetailimg TEXT,
    productstatus VARCHAR(20) DEFAULT 'active',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    companyid BIGINT,
    typeid BIGINT,
    service_start_time TIME,
    service_end_time TIME,
    rating DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (companyid) REFERENCES company(id),
    FOREIGN KEY (typeid) REFERENCES producttype(id)
);

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordernumber VARCHAR(50) UNIQUE NOT NULL,
    userid BIGINT NOT NULL,
    productid BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    totalprice DECIMAL(10,2) NOT NULL,
    orderstatus VARCHAR(20) DEFAULT 'pending',
    ordertime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completetime TIMESTAMP,
    remark VARCHAR(500),
    FOREIGN KEY (userid) REFERENCES sysuser(id),
    FOREIGN KEY (productid) REFERENCES product(id)
);

-- 创建订单评价表
CREATE TABLE IF NOT EXISTS orderevalute (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orderid BIGINT NOT NULL,
    userid BIGINT NOT NULL,
    companyid BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orderid) REFERENCES orders(id),
    FOREIGN KEY (userid) REFERENCES sysuser(id),
    FOREIGN KEY (companyid) REFERENCES company(id)
);

-- 创建购物车表
CREATE TABLE IF NOT EXISTS shopcart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    productid BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES sysuser(id),
    FOREIGN KEY (productid) REFERENCES product(id)
);

-- 创建用户收藏表
CREATE TABLE IF NOT EXISTS userlike (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    productid BIGINT NOT NULL,
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES sysuser(id),
    FOREIGN KEY (productid) REFERENCES product(id)
);

-- 创建消息表
CREATE TABLE IF NOT EXISTS message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    senderid BIGINT NOT NULL,
    receiverid BIGINT NOT NULL,
    content TEXT NOT NULL,
    messagetype VARCHAR(20) DEFAULT 'text',
    sendtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    readstatus BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (senderid) REFERENCES sysuser(id),
    FOREIGN KEY (receiverid) REFERENCES sysuser(id)
);

-- 创建网站公告表
CREATE TABLE IF NOT EXISTS webnotice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    publishtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active',
    authorid BIGINT,
    FOREIGN KEY (authorid) REFERENCES sysuser(id)
);

-- 创建资金流水表
CREATE TABLE IF NOT EXISTS tmoney (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transactiontype VARCHAR(20) NOT NULL,
    description VARCHAR(200),
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES sysuser(id)
);

-- 创建交易类型表
CREATE TABLE IF NOT EXISTS ttype (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(50) NOT NULL,
    typedescription VARCHAR(200),
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
