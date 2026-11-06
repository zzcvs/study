-- 企业餐厅网络点餐系统数据库设计
-- 数据库：canteen_system

-- 创建数据库
CREATE DATABASE IF NOT EXISTS canteen_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE canteen_system;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `role` VARCHAR(20) NOT NULL COMMENT '角色：MANAGER-餐厅经理,KITCHEN-厨房主管,DISTRIBUTOR-配餐员,FINANCE-财务管理,EMPLOYEE-企业员工',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `department` VARCHAR(50) COMMENT '部门（员工用）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (`username`),
    INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 食谱表
CREATE TABLE IF NOT EXISTS `recipe` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '食谱ID',
    `name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
    `category` VARCHAR(50) NOT NULL COMMENT '菜品分类：主食、汤品、凉菜、热菜、甜点、饮料',
    `description` TEXT COMMENT '菜品描述',
    `ingredients` TEXT COMMENT '主要食材',
    `image_url` VARCHAR(255) COMMENT '菜品图片URL',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (`category`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食谱表';

-- 3. 菜单表
CREATE TABLE IF NOT EXISTS `menu` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
    `menu_date` DATE NOT NULL COMMENT '菜单日期',
    `meal_type` VARCHAR(20) NOT NULL COMMENT '餐别：早餐、午餐、晚餐',
    `recipe_id` BIGINT NOT NULL COMMENT '食谱ID',
    `stock` INT DEFAULT 100 COMMENT '库存数量',
    `sold_count` INT DEFAULT 0 COMMENT '已售数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_meal_recipe (`menu_date`, `meal_type`, `recipe_id`),
    INDEX idx_menu_date (`menu_date`),
    FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 4. 订单表
CREATE TABLE IF NOT EXISTS `order_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(50) NOT NULL COMMENT '用户姓名',
    `department` VARCHAR(50) COMMENT '部门',
    `order_date` DATE NOT NULL COMMENT '订餐日期',
    `meal_type` VARCHAR(20) NOT NULL COMMENT '餐别：早餐、午餐、晚餐',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `order_status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待处理，COOKING-制作中，READY-待取餐，COMPLETED-已完成，CANCELLED-已取消',
    `payment_status` VARCHAR(20) DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID-未支付，PAID-已支付',
    `distribute_status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '配餐状态：PENDING-待配餐，DISTRIBUTING-配餐中，COMPLETED-已完成',
    `remark` VARCHAR(255) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_order_date (`order_date`),
    INDEX idx_order_status (`order_status`),
    INDEX idx_order_no (`order_no`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 5. 订单详情表
CREATE TABLE IF NOT EXISTS `order_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单详情ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `recipe_id` BIGINT NOT NULL COMMENT '食谱ID',
    `recipe_name` VARCHAR(100) NOT NULL COMMENT '菜品名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (`order_id`),
    FOREIGN KEY (`order_id`) REFERENCES `order_info`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`recipe_id`) REFERENCES `recipe`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 6. 财务记录表
CREATE TABLE IF NOT EXISTS `finance_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '财务记录ID',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `order_count` INT DEFAULT 0 COMMENT '订单数量',
    `total_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '总金额',
    `paid_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '已支付金额',
    `unpaid_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '未支付金额',
    `meal_type` VARCHAR(20) COMMENT '餐别',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_record_date (`record_date`),
    INDEX idx_meal_type (`meal_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- 插入初始数据

-- 插入默认管理员账户（餐厅经理）
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `phone`, `email`, `department`, `status`) VALUES
('admin', '123456', '系统管理员', 'MANAGER', '13800138000', 'admin@company.com', '管理部', 1),
('kitchen', '123456', '厨房主管', 'KITCHEN', '13800138001', 'kitchen@company.com', '厨房', 1),
('distributor', '123456', '配餐员', 'DISTRIBUTOR', '13800138002', 'distributor@company.com', '配餐部', 1),
('finance', '123456', '财务主管', 'FINANCE', '13800138003', 'finance@company.com', '财务部', 1),
('employee1', '123456', '张三', 'EMPLOYEE', '13800138004', 'zhangsan@company.com', '技术部', 1),
('employee2', '123456', '李四', 'EMPLOYEE', '13800138005', 'lisi@company.com', '市场部', 1);

-- 插入示例食谱数据
INSERT INTO `recipe` (`name`, `category`, `description`, `ingredients`, `price`, `status`) VALUES
('红烧肉', '热菜', '经典红烧肉，肥而不腻', '五花肉、冰糖、生抽、老抽', 28.00, 1),
('宫保鸡丁', '热菜', '川菜经典，香辣可口', '鸡胸肉、花生米、干辣椒', 22.00, 1),
('清蒸鲈鱼', '热菜', '鲜美嫩滑，营养丰富', '鲈鱼、葱丝、姜丝', 38.00, 1),
('西红柿鸡蛋汤', '汤品', '酸甜可口，开胃下饭', '西红柿、鸡蛋、香菜', 12.00, 1),
('紫菜蛋花汤', '汤品', '清淡鲜美，老少皆宜', '紫菜、鸡蛋、葱花', 10.00, 1),
('凉拌黄瓜', '凉菜', '清爽解腻，简单易做', '黄瓜、蒜泥、香油', 8.00, 1),
('拍黄瓜', '凉菜', '酸辣爽口，开胃解腻', '黄瓜、辣椒、醋', 8.00, 1),
('蛋炒饭', '主食', '简单美味，营养均衡', '米饭、鸡蛋、葱花', 15.00, 1),
('扬州炒饭', '主食', '扬州名菜，色香味俱全', '米饭、鸡蛋、火腿、虾仁', 20.00, 1),
('银耳莲子汤', '甜点', '清热润肺，美容养颜', '银耳、莲子、冰糖', 15.00, 1),
('绿豆汤', '甜点', '清热解暑，消暑佳品', '绿豆、冰糖', 10.00, 1),
('可乐', '饮料', '冰爽解渴', '可乐', 5.00, 1),
('雪碧', '饮料', '清爽怡人', '雪碧', 5.00, 1);

-- 插入今日菜单示例数据
INSERT INTO `menu` (`menu_date`, `meal_type`, `recipe_id`, `stock`, `status`) VALUES
(CURDATE(), '午餐', 1, 50, 1),
(CURDATE(), '午餐', 2, 50, 1),
(CURDATE(), '午餐', 3, 30, 1),
(CURDATE(), '午餐', 4, 100, 1),
(CURDATE(), '午餐', 6, 80, 1),
(CURDATE(), '午餐', 8, 60, 1),
(CURDATE(), '午餐', 11, 100, 1),
(CURDATE(), '午餐', 12, 100, 1);

-- 查看表结构
SHOW TABLES;
