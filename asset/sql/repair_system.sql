-- 设备报修系统数据库表结构

-- 设备表
CREATE TABLE IF NOT EXISTS `equipment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `equipment_code` VARCHAR(50) NOT NULL COMMENT '设备编码',
    `equipment_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `equipment_type` VARCHAR(50) NOT NULL COMMENT '设备类型',
    `location` VARCHAR(200) NOT NULL COMMENT '设备位置',
    `status` VARCHAR(20) DEFAULT 'normal' COMMENT '设备状态：normal-正常，damaged-损坏，maintenance-维护中',
    `purchase_date` DATE COMMENT '购买日期',
    `warranty_end_date` DATE COMMENT '保修截止日期',
    `manufacturer` VARCHAR(100) COMMENT '生产厂商',
    `model` VARCHAR(100) COMMENT '设备型号',
    `specification` VARCHAR(500) COMMENT '设备规格',
    `remark` VARCHAR(1000) COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_equipment_code` (`equipment_code`),
    KEY `idx_equipment_type` (`equipment_type`),
    KEY `idx_location` (`location`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- 报修工单表
CREATE TABLE IF NOT EXISTS `repair_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '工单编号',
    `user_id` BIGINT NOT NULL COMMENT '报修人ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '报修人姓名',
    `user_phone` VARCHAR(20) COMMENT '报修人电话',
    `equipment_id` BIGINT NOT NULL COMMENT '设备ID',
    `equipment_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `equipment_type` VARCHAR(50) NOT NULL COMMENT '设备类型',
    `location` VARCHAR(200) NOT NULL COMMENT '设备位置',
    `fault_type` VARCHAR(100) NOT NULL COMMENT '故障类型',
    `fault_description` TEXT NOT NULL COMMENT '故障描述',
    `fault_images` TEXT COMMENT '故障图片URL（JSON数组）',
    `priority` VARCHAR(20) DEFAULT 'medium' COMMENT '优先级：low-低，medium-中，high-高，urgent-紧急',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '工单状态：pending-待处理，assigned-已分配，processing-维修中，completed-已完成，cancelled-已取消',
    `repairman_id` BIGINT COMMENT '维修人员ID',
    `repairman_name` VARCHAR(64) COMMENT '维修人员姓名',
    `assignment_time` DATETIME COMMENT '分配时间',
    `accept_time` DATETIME COMMENT '接单时间',
    `start_time` DATETIME COMMENT '开始维修时间',
    `end_time` DATETIME COMMENT '完成时间',
    `response_time` INT COMMENT '响应时长（分钟）',
    `repair_duration` INT COMMENT '维修时长（分钟）',
    `total_cost` DECIMAL(10,2) DEFAULT 0.00 COMMENT '维修费用',
    `remark` VARCHAR(1000) COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_equipment_id` (`equipment_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- 维修记录表
CREATE TABLE IF NOT EXISTS `repair_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '工单ID',
    `record_type` VARCHAR(20) NOT NULL COMMENT '记录类型：accept-接单，process-维修记录，complete-完成，remark-备注',
    `content` TEXT NOT NULL COMMENT '记录内容',
    `images` TEXT COMMENT '相关图片URL（JSON数组）',
    `operator_id` BIGINT NOT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(64) NOT NULL COMMENT '操作人姓名',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表';

-- 维修评价表
CREATE TABLE IF NOT EXISTS `repair_evaluation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` BIGINT NOT NULL COMMENT '工单ID',
    `user_id` BIGINT NOT NULL COMMENT '评价人ID',
    `user_name` VARCHAR(64) NOT NULL COMMENT '评价人姓名',
    `repairman_id` BIGINT NOT NULL COMMENT '维修人员ID',
    `repairman_name` VARCHAR(64) NOT NULL COMMENT '维修人员姓名',
    `rating` TINYINT NOT NULL COMMENT '评分（1-5星）',
    `content` TEXT COMMENT '评价内容',
    `images` TEXT COMMENT '评价图片URL（JSON数组）',
    `response_rating` TINYINT COMMENT '响应速度评分（1-5星）',
    `service_rating` TINYINT COMMENT '服务态度评分（1-5星）',
    `quality_rating` TINYINT COMMENT '维修质量评分（1-5星）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_repairman_id` (`repairman_id`),
    KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修评价表';

-- 维修人员表
CREATE TABLE IF NOT EXISTS `repairman` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(64) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `specialty` VARCHAR(200) COMMENT '擅长维修类型',
    `skill_level` VARCHAR(20) DEFAULT 'junior' COMMENT '技能等级：junior-初级，intermediate-中级，senior-高级，expert-专家',
    `status` VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-活跃，busy-忙碌，vacation-休假，inactive-不活跃',
    `completed_orders` INT DEFAULT 0 COMMENT '已完成工单数量',
    `avg_rating` DECIMAL(3,2) DEFAULT 5.00 COMMENT '平均评分',
    `total_rating` INT DEFAULT 0 COMMENT '总评分次数',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `remark` VARCHAR(1000) COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_skill_level` (`skill_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修人员表';

-- 故障类型配置表
CREATE TABLE IF NOT EXISTS `fault_type_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `equipment_type` VARCHAR(50) NOT NULL COMMENT '设备类型',
    `fault_type` VARCHAR(100) NOT NULL COMMENT '故障类型',
    `description` VARCHAR(500) COMMENT '故障描述',
    `suggested_solution` VARCHAR(1000) COMMENT '建议解决方案',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_equipment_type` (`equipment_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故障类型配置表';

-- 通知记录表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `user_name` VARCHAR(64) COMMENT '接收用户姓名',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT NOT NULL COMMENT '通知内容',
    `type` VARCHAR(20) NOT NULL COMMENT '通知类型：order_created-工单创建，order_assigned-工单分配，order_accepted-工单接单，order_processing-维修中，order_completed-工单完成，order_evaluated-工单评价',
    `related_id` BIGINT COMMENT '关联ID（如工单ID）',
    `related_type` VARCHAR(20) COMMENT '关联类型',
    `status` VARCHAR(20) DEFAULT 'unread' COMMENT '状态：unread-未读，read-已读',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知记录表';
