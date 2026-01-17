USE `volunteer_platform`;

DROP TABLE IF EXISTS `activity_category`;
CREATE TABLE IF NOT EXISTS `activity_category`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '分类ID' PRIMARY KEY,
    `name`        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `icon`        VARCHAR(200) DEFAULT NULL COMMENT '分类图标',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `sort_order`  INT UNSIGNED DEFAULT 0 COMMENT '排序',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    UNIQUE KEY `uk_name` (`name`)
) COMMENT '活动分类表' COLLATE = utf8mb4_unicode_ci;

INSERT INTO `activity_category` (`name`, `description`, `status`) VALUES 
('环保', '环境保护相关活动', 1),
('助老', '帮助老年人相关活动', 1),
('教育', '教育支持相关活动', 1),
('医疗', '医疗健康相关活动', 1);

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '活动ID' PRIMARY KEY,
    `title`             VARCHAR(200)  NOT NULL COMMENT '活动标题',
    `description`       TEXT          DEFAULT NULL COMMENT '活动描述',
    `category_id`       BIGINT UNSIGNED NOT NULL COMMENT '活动分类ID',
    `activity_time`     DATETIME      NOT NULL COMMENT '活动时间',
    `location`          VARCHAR(200)  NOT NULL COMMENT '活动地点',
    `required_volunteers` INT UNSIGNED NOT NULL COMMENT '需求人数',
    `registered_count`  INT UNSIGNED DEFAULT 0 COMMENT '已报名人数',
    `status`            TINYINT UNSIGNED NOT NULL COMMENT '活动状态 1-招募中 2-进行中 3-已完成',
    `contact_name`      VARCHAR(50)   DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone`     VARCHAR(20)   DEFAULT NULL COMMENT '联系人电话',
    `cover_image`       VARCHAR(500)  DEFAULT NULL COMMENT '封面图片',
    `max_duration`      INT UNSIGNED  DEFAULT NULL COMMENT '预计时长(分钟)',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    KEY `idx_category_id` (`category_id`),
    KEY `idx_activity_time` (`activity_time`),
    KEY `idx_status` (`status`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `activity_registration`;
CREATE TABLE IF NOT EXISTS `activity_registration`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '报名ID' PRIMARY KEY,
    `activity_id`       BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `volunteer_id`      BIGINT UNSIGNED NOT NULL COMMENT '志愿者ID',
    `real_name`         VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    `phone`             VARCHAR(20)  NOT NULL COMMENT '联系电话',
    `email`             VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `age`               TINYINT UNSIGNED DEFAULT NULL COMMENT '年龄',
    `gender`            TINYINT UNSIGNED DEFAULT NULL COMMENT '性别 1-男 2-女',
    `skills`            VARCHAR(500) DEFAULT NULL COMMENT '个人技能',
    `status`            TINYINT UNSIGNED DEFAULT 1 COMMENT '审核状态 1-待审核 2-已通过 3-已拒绝 4-已取消',
    `reviewer_id`       BIGINT UNSIGNED DEFAULT NULL COMMENT '审核人ID',
    `review_time`       DATETIME     DEFAULT NULL COMMENT '审核时间',
    `review_note`       VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    `is_checked_in`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否签到 0-未签到 1-已签到',
    `check_in_time`     DATETIME     DEFAULT NULL COMMENT '签到时间',
    `is_checked_out`    TINYINT UNSIGNED DEFAULT 0 COMMENT '是否签出 0-未签出 1-已签出',
    `check_out_time`    DATETIME     DEFAULT NULL COMMENT '签出时间',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    UNIQUE KEY `uk_activity_volunteer` (`activity_id`, `volunteer_id`),
    KEY `idx_volunteer_id` (`volunteer_id`),
    KEY `idx_status` (`status`)
) COMMENT '活动报名表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '服务记录ID' PRIMARY KEY,
    `activity_id`       BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `volunteer_id`      BIGINT UNSIGNED NOT NULL COMMENT '志愿者ID',
    `registration_id`   BIGINT UNSIGNED NOT NULL COMMENT '报名ID',
    `service_date`      DATE         NOT NULL COMMENT '服务日期',
    `check_in_time`     DATETIME     NOT NULL COMMENT '签到时间',
    `check_out_time`    DATETIME     DEFAULT NULL COMMENT '签出时间',
    `duration`          INT UNSIGNED DEFAULT 0 COMMENT '服务时长(分钟)',
    `rating`            TINYINT UNSIGNED DEFAULT NULL COMMENT '服务评价 1-5星',
    `comment`           TEXT         DEFAULT NULL COMMENT '评价内容',
    `comment_time`      DATETIME     DEFAULT NULL COMMENT '评价时间',
    `certificate_url`   VARCHAR(500) DEFAULT NULL COMMENT '服务证明URL',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_volunteer_id` (`volunteer_id`),
    KEY `idx_service_date` (`service_date`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '帖子ID' PRIMARY KEY,
    `title`             VARCHAR(200)  NOT NULL COMMENT '帖子标题',
    `content`           TEXT          NOT NULL COMMENT '帖子内容',
    `activity_id`       BIGINT UNSIGNED DEFAULT NULL COMMENT '关联活动ID',
    `author_id`         BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    `author_name`       VARCHAR(50)  NOT NULL COMMENT '作者姓名',
    `author_avatar`     VARCHAR(500) DEFAULT NULL COMMENT '作者头像',
    `view_count`        INT UNSIGNED DEFAULT 0 COMMENT '浏览量',
    `like_count`        INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    `comment_count`     INT UNSIGNED DEFAULT 0 COMMENT '评论数',
    `status`            TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1-正常 2-精华 3-置顶 4-已删除',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    KEY `idx_author_id` (`author_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_status` (`status`)
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE IF NOT EXISTS `forum_comment`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '评论ID' PRIMARY KEY,
    `post_id`           BIGINT UNSIGNED NOT NULL COMMENT '帖子ID',
    `parent_id`         BIGINT UNSIGNED DEFAULT 0 COMMENT '父评论ID 0-一级评论',
    `author_id`         BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    `author_name`       VARCHAR(50)  NOT NULL COMMENT '作者姓名',
    `author_avatar`     VARCHAR(500) DEFAULT NULL COMMENT '作者头像',
    `content`           TEXT          NOT NULL COMMENT '评论内容',
    `like_count`        INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    `status`            TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1-正常 2-已删除',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    KEY `idx_post_id` (`post_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_author_id` (`author_id`)
) COMMENT '论坛评论表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `activity_photo`;
CREATE TABLE IF NOT EXISTS `activity_photo`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '照片ID' PRIMARY KEY,
    `activity_id`       BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
    `uploader_id`       BIGINT UNSIGNED NOT NULL COMMENT '上传者ID',
    `uploader_name`     VARCHAR(50)  NOT NULL COMMENT '上传者姓名',
    `photo_url`         VARCHAR(500) NOT NULL COMMENT '照片URL',
    `thumbnail_url`     VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
    `description`       VARCHAR(500) DEFAULT NULL COMMENT '照片描述',
    `like_count`        INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    `sort_order`        INT UNSIGNED DEFAULT 0 COMMENT '排序',
    `status`            TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1-正常 2-已删除',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_uploader_id` (`uploader_id`)
) COMMENT '活动照片表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `volunteer_user`;
CREATE TABLE IF NOT EXISTS `volunteer_user`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '用户ID' PRIMARY KEY,
    `username`          VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`          VARCHAR(200) NOT NULL COMMENT '密码(加密)',
    `real_name`         VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `phone`             VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `email`             VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`            VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `gender`            TINYINT UNSIGNED DEFAULT NULL COMMENT '性别 1-男 2-女',
    `age`               TINYINT UNSIGNED DEFAULT NULL COMMENT '年龄',
    `address`           VARCHAR(500) DEFAULT NULL COMMENT '地址',
    `skills`            VARCHAR(500) DEFAULT NULL COMMENT '个人技能',
    `total_service_hours` DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计服务时长(小时)',
    `activity_count`    INT UNSIGNED DEFAULT 0 COMMENT '参与活动次数',
    `role`              TINYINT UNSIGNED DEFAULT 1 COMMENT '角色 1-普通志愿者 2-管理员 3-活动组织者',
    `status`            TINYINT UNSIGNED DEFAULT 1 COMMENT '状态 1-正常 2-禁用',
    `creator_id`        BIGINT UNSIGNED NOT NULL COMMENT '创建者ID',
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) COMMENT '志愿者用户表' COLLATE = utf8mb4_unicode_ci;

INSERT INTO `volunteer_user` (`username`, `password`, `real_name`, `phone`, `role`, `status`, `creator_id`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EK', '系统管理员', '13800138000', 2, 1, 1),
('organizer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EK', '活动组织者', '13800138001', 3, 1, 1),
('volunteer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EK', '志愿者小王', '13800138002', 1, 1, 1);
