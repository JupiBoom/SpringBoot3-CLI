USE `volunteer_platform`;

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `title`            VARCHAR(200)     NOT NULL COMMENT '活动标题',
    `description`      TEXT             NULL COMMENT '活动描述',
    `category`         TINYINT UNSIGNED NOT NULL COMMENT '分类：1-环保 2-助老 3-教育 4-医疗',
    `location`         VARCHAR(500)     NOT NULL COMMENT '活动地点',
    `start_time`       DATETIME         NOT NULL COMMENT '开始时间',
    `end_time`         DATETIME         NOT NULL COMMENT '结束时间',
    `required_num`     INT UNSIGNED     NOT NULL COMMENT '需求人数',
    `registered_num`   INT UNSIGNED     DEFAULT 0 COMMENT '已报名人数',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：1-招募中 2-进行中 3-已完成',
    `contact_person`   VARCHAR(100)     NULL COMMENT '联系人',
    `contact_phone`    VARCHAR(20)      NULL COMMENT '联系电话',
    `requirements`     TEXT             NULL COMMENT '报名要求',
    `creator_id`       BIGINT UNSIGNED  NULL COMMENT '创建者ID',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`       BIGINT UNSIGNED  NULL COMMENT '更新者ID',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    INDEX idx_category (`category`),
    INDEX idx_status (`status`),
    INDEX idx_start_time (`start_time`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `registration`;
CREATE TABLE IF NOT EXISTS `registration`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `user_name`        VARCHAR(100)     NULL COMMENT '用户姓名',
    `user_phone`       VARCHAR(20)      NULL COMMENT '用户电话',
    `reason`           TEXT             NULL COMMENT '报名理由',
    `status`           TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待审核 1-审核通过 2-审核拒绝',
    `reviewer_id`      BIGINT UNSIGNED  NULL COMMENT '审核人ID',
    `review_time`      DATETIME         NULL COMMENT '审核时间',
    `review_comment`   VARCHAR(500)     NULL COMMENT '审核备注',
    `check_in_time`    DATETIME         NULL COMMENT '签到时间',
    `check_out_time`   DATETIME         NULL COMMENT '签出时间',
    `service_duration` DECIMAL(5, 2)    DEFAULT 0 COMMENT '服务时长（小时）',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_activity_user (`activity_id`, `user_id`),
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `registration_id`  BIGINT UNSIGNED  NOT NULL COMMENT '报名记录ID',
    `activity_id`      BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `service_date`     DATE             NOT NULL COMMENT '服务日期',
    `start_time`       DATETIME         NULL COMMENT '开始时间',
    `end_time`         DATETIME         NULL COMMENT '结束时间',
    `duration`         DECIMAL(5, 2)    NOT NULL COMMENT '服务时长（小时）',
    `certificate_no`   VARCHAR(100)     NULL COMMENT '服务证明编号',
    `certificate_url`  VARCHAR(500)     NULL COMMENT '服务证明文件路径',
    `generate_time`    DATETIME         NULL COMMENT '证明生成时间',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_registration (`registration_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_service_date (`service_date`),
    INDEX idx_certificate_no (`certificate_no`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `review`;
CREATE TABLE IF NOT EXISTS `review`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `registration_id`  BIGINT UNSIGNED  NOT NULL COMMENT '报名记录ID',
    `rating`           TINYINT UNSIGNED NOT NULL COMMENT '评分：1-5星',
    `content`          TEXT             NULL COMMENT '评价内容',
    `tags`             VARCHAR(200)     NULL COMMENT '标签，逗号分隔',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_registration_user (`registration_id`, `user_id`),
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_rating (`rating`)
) COMMENT '评价表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED  NULL COMMENT '关联活动ID（可选）',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '发布者ID',
    `user_name`        VARCHAR(100)     NULL COMMENT '发布者姓名',
    `title`            VARCHAR(200)     NOT NULL COMMENT '帖子标题',
    `content`          TEXT             NOT NULL COMMENT '帖子内容',
    `cover_image`      VARCHAR(500)     NULL COMMENT '封面图片',
    `view_count`       INT UNSIGNED     DEFAULT 0 COMMENT '浏览次数',
    `like_count`       INT UNSIGNED     DEFAULT 0 COMMENT '点赞次数',
    `reply_count`      INT UNSIGNED     DEFAULT 0 COMMENT '回复次数',
    `is_top`           TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_create_time (`create_time`)
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `forum_reply`;
CREATE TABLE IF NOT EXISTS `forum_reply`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `post_id`          BIGINT UNSIGNED  NOT NULL COMMENT '帖子ID',
    `parent_id`        BIGINT UNSIGNED  DEFAULT 0 COMMENT '父回复ID，0为根回复',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '回复者ID',
    `user_name`        VARCHAR(100)     NULL COMMENT '回复者姓名',
    `content`          TEXT             NOT NULL COMMENT '回复内容',
    `like_count`       INT UNSIGNED     DEFAULT 0 COMMENT '点赞次数',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_post_id (`post_id`),
    INDEX idx_parent_id (`parent_id`)
) COMMENT '论坛回复表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `photo`;
CREATE TABLE IF NOT EXISTS `photo`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`      BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '上传者ID',
    `user_name`        VARCHAR(100)     NULL COMMENT '上传者姓名',
    `photo_url`        VARCHAR(500)     NOT NULL COMMENT '照片URL',
    `thumbnail_url`    VARCHAR(500)     NULL COMMENT '缩略图URL',
    `description`      VARCHAR(500)     NULL COMMENT '照片描述',
    `is_cover`         TINYINT UNSIGNED DEFAULT 0 COMMENT '是否封面：0-否 1-是',
    `like_count`       INT UNSIGNED     DEFAULT 0 COMMENT '点赞次数',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_activity_id (`activity_id`),
    INDEX idx_user_id (`user_id`)
) COMMENT '活动照片表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`          BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
    `activity_id`      BIGINT UNSIGNED  NULL COMMENT '关联活动ID',
    `type`             TINYINT UNSIGNED NOT NULL COMMENT '类型：1-活动提醒 2-审核结果 3-系统通知',
    `title`            VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`          TEXT             NOT NULL COMMENT '通知内容',
    `is_read`          TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
    `read_time`        DATETIME         NULL COMMENT '读取时间',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_user_id (`user_id`),
    INDEX idx_is_read (`is_read`),
    INDEX idx_create_time (`create_time`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `username`         VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`         VARCHAR(255)     NOT NULL COMMENT '密码',
    `real_name`        VARCHAR(100)     NULL COMMENT '真实姓名',
    `phone`            VARCHAR(20)      NULL COMMENT '手机号',
    `email`            VARCHAR(100)     NULL COMMENT '邮箱',
    `avatar`           VARCHAR(500)     NULL COMMENT '头像',
    `gender`           TINYINT UNSIGNED NULL COMMENT '性别：1-男 2-女',
    `birthday`         DATE             NULL COMMENT '生日',
    `address`          VARCHAR(500)     NULL COMMENT '地址',
    `skills`           VARCHAR(500)     NULL COMMENT '技能标签',
    `total_service_hours` DECIMAL(10, 2) DEFAULT 0 COMMENT '累计服务时长',
    `total_activities` INT UNSIGNED     DEFAULT 0 COMMENT '参与活动数',
    `average_rating`   DECIMAL(2, 1)    DEFAULT 0 COMMENT '平均评分',
    `role`             TINYINT UNSIGNED DEFAULT 1 COMMENT '角色：1-志愿者 2-管理员 3-超级管理员',
    `status`           TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_username (`username`),
    UNIQUE KEY uk_phone (`phone`),
    INDEX idx_role (`role`),
    INDEX idx_status (`status`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;
