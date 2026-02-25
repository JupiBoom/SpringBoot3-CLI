USE `volunteer_db`;

CREATE DATABASE IF NOT EXISTS `volunteer_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `volunteer_db`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `username`     VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`     VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name`    VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `phone`        VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`        VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`       VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `gender`       TINYINT UNSIGNED DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    `age`          INT          DEFAULT NULL COMMENT '年龄',
    `skills`       VARCHAR(500) DEFAULT NULL COMMENT '技能标签',
    `total_hours`  DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计服务时长(小时)',
    `role`         TINYINT UNSIGNED DEFAULT 1 COMMENT '角色：1-志愿者 2-管理员 3-超管',
    `status`       TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_username (`username`),
    INDEX idx_phone (`phone`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `title`            VARCHAR(100) NOT NULL COMMENT '活动标题',
    `description`      TEXT COMMENT '活动描述',
    `category`         VARCHAR(20)  NOT NULL COMMENT '活动分类：ENVIRONMENTAL-环保 ELDERLY-助老 EDUCATION-教育 MEDICAL-医疗',
    `location`         VARCHAR(200) NOT NULL COMMENT '活动地点',
    `start_time`       DATETIME     NOT NULL COMMENT '开始时间',
    `end_time`         DATETIME     NOT NULL COMMENT '结束时间',
    `need_people`      INT UNSIGNED DEFAULT 0 COMMENT '需求人数',
    `applied_people`   INT UNSIGNED DEFAULT 0 COMMENT '已报名人数',
    `status`           VARCHAR(20)  DEFAULT 'RECRUITING' COMMENT '状态：RECRUITING-招募中 ONGOING-进行中 COMPLETED-已完成 CANCELLED-已取消',
    `contact_name`     VARCHAR(50)  DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone`    VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `requirements`     TEXT COMMENT '报名要求',
    `creator_id`       BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_category (`category`),
    INDEX idx_status (`status`),
    INDEX idx_start_time (`start_time`),
    INDEX idx_creator_id (`creator_id`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `registration`;
CREATE TABLE IF NOT EXISTS `registration`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `status`           VARCHAR(20) DEFAULT 'PENDING' COMMENT '报名状态：PENDING-待审核 APPROVED-已通过 REJECTED-已拒绝 CANCELLED-已取消',
    `audit_type`       VARCHAR(20) DEFAULT 'AUTO' COMMENT '审核方式：AUTO-自动 MANUAL-人工',
    `audit_id`         BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
    `audit_time`       DATETIME DEFAULT NULL COMMENT '审核时间',
    `audit_reason`     VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    `reminded`         TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已提醒：0-否 1-是',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE KEY uk_activity_user (`activity_id`, `user_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `registration_id`  BIGINT UNSIGNED NOT NULL COMMENT '报名ID',
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `check_in_time`    DATETIME DEFAULT NULL COMMENT '签到时间',
    `check_out_time`   DATETIME DEFAULT NULL COMMENT '签出时间',
    `service_hours`    DECIMAL(10,2) DEFAULT 0.00 COMMENT '服务时长(小时)',
    `rating`           TINYINT UNSIGNED DEFAULT NULL COMMENT '服务评价：1-5星',
    `comment`          VARCHAR(1000) DEFAULT NULL COMMENT '评价内容',
    `certificate_generated` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已生成证书：0-否 1-是',
    `certificate_url`  VARCHAR(500) DEFAULT NULL COMMENT '证书路径',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_registration_id (`registration_id`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED DEFAULT NULL COMMENT '关联活动ID',
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',
    `title`            VARCHAR(100) NOT NULL COMMENT '帖子标题',
    `content`          TEXT NOT NULL COMMENT '帖子内容',
    `view_count`       INT UNSIGNED DEFAULT 0 COMMENT '浏览次数',
    `like_count`       INT UNSIGNED DEFAULT 0 COMMENT '点赞次数',
    `reply_count`      INT UNSIGNED DEFAULT 0 COMMENT '回复次数',
    `is_top`           TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`)
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_reply`;
CREATE TABLE IF NOT EXISTS `forum_reply`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `post_id`          BIGINT UNSIGNED NOT NULL COMMENT '帖子ID',
    `parent_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '父回复ID',
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '回复者ID',
    `content`          VARCHAR(2000) NOT NULL COMMENT '回复内容',
    `like_count`       INT UNSIGNED DEFAULT 0 COMMENT '点赞次数',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_post_id (`post_id`),
    INDEX idx_user_id (`user_id`)
) COMMENT '论坛回复表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `photo_wall`;
CREATE TABLE IF NOT EXISTS `photo_wall`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED DEFAULT NULL COMMENT '关联活动ID',
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '上传者ID',
    `photo_url`        VARCHAR(500) NOT NULL COMMENT '照片URL',
    `description`      VARCHAR(200) DEFAULT NULL COMMENT '照片描述',
    `like_count`       INT UNSIGNED DEFAULT 0 COMMENT '点赞次数',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`)
) COMMENT '照片墙表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`          BIGINT UNSIGNED NOT NULL COMMENT '接收用户ID',
    `type`             VARCHAR(20) NOT NULL COMMENT '通知类型：ACTIVITY-活动通知 SYSTEM-系统消息',
    `title`            VARCHAR(100) DEFAULT NULL COMMENT '通知标题',
    `content`          VARCHAR(500) NOT NULL COMMENT '通知内容',
    `related_id`       BIGINT UNSIGNED DEFAULT NULL COMMENT '关联ID',
    `is_read`          TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
    `create_time`      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_user_id (`user_id`),
    INDEX idx_is_read (`is_read`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `role`, `status`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800000000', 3, 1),
('volunteer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', '13800000001', 1, 1),
('volunteer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四', '13800000002', 1, 1);

