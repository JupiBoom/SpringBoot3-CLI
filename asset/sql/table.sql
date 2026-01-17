USE `example`;

DROP TABLE IF EXISTS `activity_photo`;
DROP TABLE IF EXISTS `forum_post`;
DROP TABLE IF EXISTS `service_record`;
DROP TABLE IF EXISTS `registration`;
DROP TABLE IF EXISTS `activity`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(500)     DEFAULT NULL COMMENT '头像URL',
    `role`        TINYINT UNSIGNED DEFAULT 2 COMMENT '角色：1-管理员，2-志愿者',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `activity`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `title`           VARCHAR(200)     NOT NULL COMMENT '活动标题',
    `description`     TEXT             DEFAULT NULL COMMENT '活动描述',
    `category`        TINYINT UNSIGNED NOT NULL COMMENT '活动分类：1-环保，2-助老，3-教育，4-医疗',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '活动状态：0-招募中，1-进行中，2-已完成',
    `start_time`      DATETIME         NOT NULL COMMENT '活动开始时间',
    `end_time`        DATETIME         NOT NULL COMMENT '活动结束时间',
    `location`        VARCHAR(200)     NOT NULL COMMENT '活动地点',
    `required_people` INT UNSIGNED     NOT NULL COMMENT '需求人数',
    `current_people`  INT UNSIGNED     DEFAULT 0 COMMENT '当前报名人数',
    `creator_id`      BIGINT UNSIGNED  NOT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_category` (`category`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_time` (`start_time`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `registration`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝',
    `audit_type`      TINYINT UNSIGNED DEFAULT 1 COMMENT '审核方式：1-自动审核，2-人工审核',
    `audit_time`      DATETIME         DEFAULT NULL COMMENT '审核时间',
    `auditor_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '审核人ID',
    `audit_remark`    VARCHAR(500)     DEFAULT NULL COMMENT '审核备注',
    `is_notified`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已通知：0-未通知，1-已通知',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `check_in_time`   DATETIME         DEFAULT NULL COMMENT '签到时间',
    `check_out_time`  DATETIME         DEFAULT NULL COMMENT '签出时间',
    `service_hours`   DECIMAL(5, 2)    DEFAULT 0.00 COMMENT '服务时长（小时）',
    `rating`          TINYINT UNSIGNED DEFAULT NULL COMMENT '评价星级：1-5星',
    `comment`         TEXT             DEFAULT NULL COMMENT '评价内容',
    `certificate_url` VARCHAR(500)     DEFAULT NULL COMMENT '服务证明URL',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_check_in_time` (`check_in_time`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `title`           VARCHAR(200)     NOT NULL COMMENT '帖子标题',
    `content`         TEXT             NOT NULL COMMENT '帖子内容',
    `type`            TINYINT UNSIGNED DEFAULT 1 COMMENT '帖子类型：1-经验分享，2-活动照片',
    `view_count`      INT UNSIGNED     DEFAULT 0 COMMENT '浏览次数',
    `like_count`      INT UNSIGNED     DEFAULT 0 COMMENT '点赞次数',
    `comment_count`   INT UNSIGNED     DEFAULT 0 COMMENT '评论次数',
    `is_top`          TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '活动论坛表' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `activity_photo`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `forum_post_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联论坛帖子ID',
    `photo_url`       VARCHAR(500)     NOT NULL COMMENT '照片URL',
    `description`     VARCHAR(200)     DEFAULT NULL COMMENT '照片描述',
    `upload_user_id`  BIGINT UNSIGNED  NOT NULL COMMENT '上传用户ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_forum_post_id` (`forum_post_id`)
) COMMENT '活动照片表' COLLATE = utf8mb4_unicode_ci;

INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`, `create_time`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', '13800138000', 'admin@example.com', 1, 1, NOW()),
('volunteer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '志愿者张三', '13800138001', 'volunteer1@example.com', 2, 1, NOW()),
('volunteer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '志愿者李四', '13800138002', 'volunteer2@example.com', 2, 1, NOW());
