USE `example`;

DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_no`        VARCHAR(50)      NOT NULL COMMENT '报修单号',
    `device_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '设备ID',
    `device_type`     VARCHAR(50)      DEFAULT NULL COMMENT '设备类型',
    `location`        VARCHAR(200)     DEFAULT NULL COMMENT '设备位置',
    `fault_type`      VARCHAR(50)      DEFAULT NULL COMMENT '故障类型',
    `fault_desc`      TEXT             DEFAULT NULL COMMENT '故障描述',
    `priority`        TINYINT UNSIGNED DEFAULT 2 COMMENT '优先级：1-紧急，2-普通，3-低',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消',
    `reporter_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '报修人ID',
    `reporter_name`   VARCHAR(50)      DEFAULT NULL COMMENT '报修人姓名',
    `reporter_phone`  VARCHAR(20)      DEFAULT NULL COMMENT '报修人电话',
    `assignee_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '维修人员ID',
    `assign_time`     DATETIME         DEFAULT NULL COMMENT '分配时间',
    `accept_time`     DATETIME         DEFAULT NULL COMMENT '接单时间',
    `complete_time`   DATETIME         DEFAULT NULL COMMENT '完成时间',
    `repair_result`   TEXT             DEFAULT NULL COMMENT '维修结果说明',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_order_no` (`order_no`),
    INDEX `idx_reporter_id` (`reporter_id`),
    INDEX `idx_assignee_id` (`assignee_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '报修单表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `device`;
CREATE TABLE IF NOT EXISTS `device`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `device_no`       VARCHAR(50)      NOT NULL COMMENT '设备编号',
    `device_name`     VARCHAR(100)     NOT NULL COMMENT '设备名称',
    `device_type`     VARCHAR(50)      NOT NULL COMMENT '设备类型',
    `brand`           VARCHAR(50)      DEFAULT NULL COMMENT '品牌',
    `model`           VARCHAR(50)      DEFAULT NULL COMMENT '型号',
    `location`        VARCHAR(200)     DEFAULT NULL COMMENT '设备位置',
    `purchase_date`   DATE             DEFAULT NULL COMMENT '购置日期',
    `warranty_date`   DATE             DEFAULT NULL COMMENT '保修到期日期',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-停用，1-正常，2-维修中',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_device_no` (`device_no`),
    INDEX `idx_device_type` (`device_type`),
    INDEX `idx_location` (`location`)
) COMMENT '设备表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_personnel`;
CREATE TABLE IF NOT EXISTS `repair_personnel`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `name`            VARCHAR(50)      NOT NULL COMMENT '姓名',
    `phone`           VARCHAR(20)      DEFAULT NULL COMMENT '联系电话',
    `specialty`       VARCHAR(100)     DEFAULT NULL COMMENT '专业特长',
    `skill_level`     TINYINT UNSIGNED DEFAULT 1 COMMENT '技能等级：1-初级，2-中级，3-高级',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-离职，1-在职',
    `total_orders`    INT UNSIGNED     DEFAULT 0 COMMENT '总接单数',
    `completed_orders`INT UNSIGNED     DEFAULT 0 COMMENT '已完成订单数',
    `avg_rating`      DECIMAL(3,2)     DEFAULT 0.00 COMMENT '平均评分',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_skill_level` (`skill_level`)
) COMMENT '维修人员表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '报修单ID',
    `record_type`     TINYINT UNSIGNED NOT NULL COMMENT '记录类型：1-接单，2-维修过程，3-完成，4-取消',
    `content`         TEXT             DEFAULT NULL COMMENT '记录内容',
    `photos`          TEXT             DEFAULT NULL COMMENT '照片地址，多个用逗号分隔',
    `operator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '操作人ID',
    `operator_name`   VARCHAR(50)      DEFAULT NULL COMMENT '操作人姓名',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_record_type` (`record_type`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_evaluation`;
CREATE TABLE IF NOT EXISTS `repair_evaluation`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '报修单ID',
    `rating`          TINYINT UNSIGNED NOT NULL COMMENT '评分：1-5星',
    `content`         TEXT             DEFAULT NULL COMMENT '评价内容',
    `evaluator_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '评价人ID',
    `evaluator_name`  VARCHAR(50)      DEFAULT NULL COMMENT '评价人姓名',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_order_id` (`order_id`),
    INDEX `idx_evaluator_id` (`evaluator_id`),
    INDEX `idx_rating` (`rating`)
) COMMENT '评价表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `repair_photo`;
CREATE TABLE IF NOT EXISTS `repair_photo`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '报修单ID',
    `photo_url`       VARCHAR(500)     NOT NULL COMMENT '照片URL',
    `photo_type`      TINYINT UNSIGNED DEFAULT 1 COMMENT '照片类型：1-故障照片，2-维修过程照片，3-完成照片',
    `file_name`       VARCHAR(200)     DEFAULT NULL COMMENT '文件名',
    `file_size`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '文件大小（字节）',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_photo_type` (`photo_type`)
) COMMENT '报修照片表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `device_type`;
CREATE TABLE IF NOT EXISTS `device_type`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `type_code`       VARCHAR(50)      NOT NULL COMMENT '类型编码',
    `type_name`       VARCHAR(100)     NOT NULL COMMENT '类型名称',
    `description`     VARCHAR(500)     DEFAULT NULL COMMENT '描述',
    `sort_order`      INT              DEFAULT 0 COMMENT '排序',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_type_code` (`type_code`),
    INDEX `idx_status` (`status`)
) COMMENT '设备类型表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `fault_type`;
CREATE TABLE IF NOT EXISTS `fault_type`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `type_code`       VARCHAR(50)      NOT NULL COMMENT '类型编码',
    `type_name`       VARCHAR(100)     NOT NULL COMMENT '类型名称',
    `description`     VARCHAR(500)     DEFAULT NULL COMMENT '描述',
    `sort_order`      INT              DEFAULT 0 COMMENT '排序',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_type_code` (`type_code`),
    INDEX `idx_status` (`status`)
) COMMENT '故障类型表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `receiver_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收人ID',
    `receiver_name`   VARCHAR(50)      DEFAULT NULL COMMENT '接收人姓名',
    `title`           VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`         TEXT             DEFAULT NULL COMMENT '通知内容',
    `type`            TINYINT UNSIGNED DEFAULT 1 COMMENT '通知类型：1-报修通知，2-分配通知，3-接单通知，4-完成通知，5-评价通知',
    `related_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联ID（如报修单ID）',
    `is_read`         TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time`       DATETIME         DEFAULT NULL COMMENT '阅读时间',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_receiver_id` (`receiver_id`),
    INDEX `idx_is_read` (`is_read`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;
