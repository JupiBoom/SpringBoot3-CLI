USE `volunteer_service`;

-- 用户表（志愿者和组织者）
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名，最大长度 50',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码，最大长度 100',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号，最大长度 20',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱，最大长度 100',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名，最大长度 50',
    `id_card`     VARCHAR(18)      DEFAULT NULL COMMENT '身份证号，最大长度 18',
    `avatar`      VARCHAR(255)     DEFAULT NULL COMMENT '头像URL，最大长度 255',
    `gender`      TINYINT UNSIGNED DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
    `birth_date`  DATE             DEFAULT NULL COMMENT '出生日期',
    `address`     VARCHAR(255)     DEFAULT NULL COMMENT '地址，最大长度 255',
    `role`        TINYINT UNSIGNED DEFAULT 0 COMMENT '角色：0-志愿者，1-组织者，2-管理员',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `total_hours` DECIMAL(10,2)    DEFAULT 0.00 COMMENT '累计服务时长',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

-- 活动表
DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `title`          VARCHAR(100)     NOT NULL COMMENT '活动标题，最大长度 100',
    `description`    TEXT             DEFAULT NULL COMMENT '活动描述',
    `category`       TINYINT UNSIGNED DEFAULT NULL COMMENT '活动分类：1-环保，2-助老，3-教育，4-医疗',
    `status`         TINYINT UNSIGNED DEFAULT 0 COMMENT '活动状态：0-招募中，1-进行中，2-已完成，3-已取消',
    `start_time`     DATETIME         NOT NULL COMMENT '活动开始时间',
    `end_time`       DATETIME         NOT NULL COMMENT '活动结束时间',
    `location`       VARCHAR(255)     NOT NULL COMMENT '活动地点，最大长度 255',
    `required_count` INT UNSIGNED     DEFAULT 1 COMMENT '需求人数',
    `current_count`  INT UNSIGNED     DEFAULT 0 COMMENT '当前报名人数',
    `contact_name`   VARCHAR(50)      DEFAULT NULL COMMENT '联系人姓名，最大长度 50',
    `contact_phone`  VARCHAR(20)      DEFAULT NULL COMMENT '联系人电话，最大长度 20',
    `images`         TEXT             DEFAULT NULL COMMENT '活动图片URL，多个用逗号分隔',
    `creator_id`     BIGINT UNSIGNED  NOT NULL COMMENT '创建者ID，关联用户表',
    `create_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    INDEX `idx_category` (`category`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_creator_id` (`creator_id`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

-- 报名表
DROP TABLE IF EXISTS `enrollment`;
CREATE TABLE IF NOT EXISTS `enrollment`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id` BIGINT UNSIGNED  NOT NULL COMMENT '活动ID，关联活动表',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '用户ID，关联用户表',
    `status`      TINYINT UNSIGNED DEFAULT 0 COMMENT '报名状态：0-待审核，1-审核通过，2-审核拒绝',
    `apply_time`  DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `audit_time`  DATETIME         DEFAULT NULL COMMENT '审核时间',
    `audit_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID，关联用户表',
    `audit_reason` VARCHAR(255)     DEFAULT NULL COMMENT '审核原因，最大长度 255',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

-- 服务记录表
DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id`   BIGINT UNSIGNED  NOT NULL COMMENT '活动ID，关联活动表',
    `user_id`       BIGINT UNSIGNED  NOT NULL COMMENT '用户ID，关联用户表',
    `check_in_time` DATETIME         DEFAULT NULL COMMENT '签到时间',
    `check_out_time` DATETIME        DEFAULT NULL COMMENT '签出时间',
    `service_hours` DECIMAL(5,2)     DEFAULT 0.00 COMMENT '服务时长（小时）',
    `rating`        TINYINT UNSIGNED DEFAULT NULL COMMENT '服务评价：1-5星',
    `feedback`      TEXT             DEFAULT NULL COMMENT '服务反馈',
    `certificate_id` VARCHAR(50)     DEFAULT NULL COMMENT '服务证明编号，最大长度 50',
    `create_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_certificate_id` (`certificate_id`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

-- 论坛帖子表
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID，关联活动表，可选',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '用户ID，关联用户表',
    `title`       VARCHAR(100)     NOT NULL COMMENT '帖子标题，最大长度 100',
    `content`     TEXT             NOT NULL COMMENT '帖子内容',
    `images`      TEXT             DEFAULT NULL COMMENT '帖子图片URL，多个用逗号分隔',
    `like_count`  INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `view_count`  INT UNSIGNED     DEFAULT 0 COMMENT '浏览数',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

-- 论坛评论表
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE IF NOT EXISTS `forum_comment`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `post_id`     BIGINT UNSIGNED  NOT NULL COMMENT '帖子ID，关联论坛帖子表',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '用户ID，关联用户表',
    `parent_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '父评论ID，关联论坛评论表，可选',
    `content`     TEXT             NOT NULL COMMENT '评论内容',
    `like_count`  INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    INDEX `idx_post_id` (`post_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_parent_id` (`parent_id`)
) COMMENT '论坛评论表' COLLATE = utf8mb4_unicode_ci;

-- 通知表
DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID，关联用户表',
    `title`       VARCHAR(100)     NOT NULL COMMENT '通知标题，最大长度 100',
    `content`     TEXT             NOT NULL COMMENT '通知内容',
    `type`        TINYINT UNSIGNED DEFAULT 0 COMMENT '通知类型：0-系统通知，1-活动提醒，2-报名审核，3-服务评价',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `related_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '关联ID（如活动ID、报名ID等）',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_is_read` (`is_read`),
    INDEX `idx_type` (`type`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;