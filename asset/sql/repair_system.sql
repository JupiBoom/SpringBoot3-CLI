USE `example`;

DROP TABLE IF EXISTS `repair_evaluation`;
DROP TABLE IF EXISTS `repair_record`;
DROP TABLE IF EXISTS `repair_notification`;
DROP TABLE IF EXISTS `repair_order`;
DROP TABLE IF EXISTS `repair_staff`;

CREATE TABLE IF NOT EXISTS `repair_staff`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '用户ID，关联用户表',
    `staff_name`  VARCHAR(50)      DEFAULT NULL COMMENT '维修人员姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '联系电话',
    `specialty`   VARCHAR(100)     DEFAULT NULL COMMENT '专长（电器/机械/管道等）',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-离线，1-在线，2-忙碌',
    `order_count` INT UNSIGNED     DEFAULT 0 COMMENT '当前接单数量',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) COMMENT '维修人员表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_no`         VARCHAR(50)      DEFAULT NULL COMMENT '工单编号',
    `user_id`          BIGINT UNSIGNED  DEFAULT NULL COMMENT '报修用户ID',
    `device_type`      VARCHAR(50)      DEFAULT NULL COMMENT '设备类型',
    `device_location`  VARCHAR(200)     DEFAULT NULL COMMENT '设备位置',
    `fault_type`       VARCHAR(50)      DEFAULT NULL COMMENT '故障类型',
    `description`      TEXT             DEFAULT NULL COMMENT '故障描述',
    `photo_urls`       TEXT             DEFAULT NULL COMMENT '故障照片URL，多个用逗号分隔',
    `priority`         TINYINT UNSIGNED DEFAULT 2 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `status`           TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待处理，1-已分配，2-维修中，3-待确认，4-已完成，5-已取消',
    `staff_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '维修人员ID',
    `assign_type`      TINYINT UNSIGNED DEFAULT 0 COMMENT '分配方式：0-自动，1-手动',
    `assign_time`      DATETIME         DEFAULT NULL COMMENT '分配时间',
    `accept_time`      DATETIME         DEFAULT NULL COMMENT '接单时间',
    `completed_time`   DATETIME         DEFAULT NULL COMMENT '完成时间',
    `response_minutes` INT UNSIGNED     DEFAULT NULL COMMENT '响应时长（分钟）',
    `creator_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`      DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`          TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_order_no (`order_no`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_staff_id (`staff_id`),
    INDEX idx_status (`status`),
    INDEX idx_create_time (`create_time`)
) COMMENT '报修工单表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '工单ID',
    `staff_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '维修人员ID',
    `action_type` TINYINT UNSIGNED DEFAULT NULL COMMENT '操作类型：1-接单，2-出发，3-到达，4-维修中，5-完成',
    `content`     TEXT             DEFAULT NULL COMMENT '操作内容/记录',
    `photo_urls`  TEXT             DEFAULT NULL COMMENT '维修照片URL，多个用逗号分隔',
    `location`    VARCHAR(200)     DEFAULT NULL COMMENT '当前位置（可选）',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_order_id (`order_id`),
    INDEX idx_staff_id (`staff_id`)
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `repair_evaluation`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '工单ID',
    `user_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '评价用户ID',
    `staff_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '被评价维修人员ID',
    `rating`         TINYINT UNSIGNED DEFAULT NULL COMMENT '评分：1-5星',
    `content`        TEXT             DEFAULT NULL COMMENT '评价内容',
    `reply_content`  TEXT             DEFAULT NULL COMMENT '回复内容',
    `reply_time`     DATETIME         DEFAULT NULL COMMENT '回复时间',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE KEY uk_order_id (`order_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_staff_id (`staff_id`)
) COMMENT '维修评价表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `repair_notification`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '工单ID',
    `user_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '接收用户ID',
    `notify_type`  TINYINT UNSIGNED DEFAULT NULL COMMENT '通知类型：1-工单分配，2-状态变更，3-维修完成，4-评价提醒',
    `title`        VARCHAR(100)     DEFAULT NULL COMMENT '通知标题',
    `content`      VARCHAR(500)     DEFAULT NULL COMMENT '通知内容',
    `is_read`      TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time`    DATETIME         DEFAULT NULL COMMENT '读取时间',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_user_id (`user_id`),
    INDEX idx_order_id (`order_id`),
    INDEX idx_is_read (`is_read`)
) COMMENT '维修通知表' COLLATE = utf8mb4_unicode_ci;
