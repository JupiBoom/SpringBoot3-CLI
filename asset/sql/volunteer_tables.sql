USE `example`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NUL L COMMENT '邮箱',
    `avatar`      VARCHAR(255)     DEFAULT NULL COMMENT '头像URL',
    `gender`      TINYINT UNSIGNED DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
    `birthday`    DATE             DEFAULT NULL COMMENT '生日',
    `address`     VARCHAR(255)     DEFAULT NULL COMMENT '地址',
    `skills`      VARCHAR(500)     DEFAULT NULL COMMENT '技能标签，逗号分隔',
    `intro`       VARCHAR(500)     DEFAULT NULL COMMENT '个人简介',
    `role`        TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '角色：0-管理员，1-志愿者，2-组织者',
    `status`      TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `total_hours` INT UNSIGNED     DEFAULT 0 COMMENT '累计服务时长（小时）',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity` (
    `id`                 BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `title`              VARCHAR(100)     NOT NULL COMMENT '活动标题',
    `description`        TEXT             DEFAULT NULL COMMENT '活动描述',
    `category`           TINYINT UNSIGNED NOT NULL COMMENT '活动分类：1-环保，2-助老，3-教育，4-医疗，5-其他',
    `status`             TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '活动状态：1-招募中，2-进行中，3-已完成，4-已取消',
    `start_time`         DATETIME         NOT NULL COMMENT '活动开始时间',
    `end_time`           DATETIME         NOT NULL COMMENT '活动结束时间',
    `location`           VARCHAR(255)     NOT NULL COMMENT '活动地点',
    `latitude`           DECIMAL(10, 7)   DEFAULT NULL COMMENT '纬度',
    `longitude`          DECIMAL(10, 7)   DEFAULT NULL COMMENT '经度',
    `required_count`     INT UNSIGNED     NOT NULL COMMENT '需求人数',
    `current_count`      INT UNSIGNED     DEFAULT 0 COMMENT '已报名人数',
    `check_in_start`     DATETIME         DEFAULT NULL COMMENT '签到开始时间',
    `check_in_end`       DATETIME         DEFAULT NULL COMMENT '签到结束时间',
    `check_out_start`    DATETIME         DEFAULT NULL COMMENT '签退开始时间',
    `check_out_end`      DATETIME         DEFAULT NULL COMMENT '签退结束时间',
    `auto_approve`       TINYINT UNSIGNED DEFAULT 0 COMMENT '是否自动审核：0-否，1-是',
    `cover_image`        VARCHAR(255)     DEFAULT NULL COMMENT '封面图片URL',
    `contact_name`       VARCHAR(50)      DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone`      VARCHAR(20)      DEFAULT NULL COMMENT '联系电话',
    `organizer_id`       BIGINT UNSIGNED  NOT NULL COMMENT '组织者ID',
    `creator_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`        DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`        DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`            TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`         TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_organizer_id` (`organizer_id`)
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `registration`;
CREATE TABLE IF NOT EXISTS `registration` (
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '志愿者ID',
    `status`          TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '报名状态：0-待审核，1-已通过，2-已拒绝，3-已取消',
    `audit_user_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '审核人ID',
    `audit_time`      DATETIME         DEFAULT NULL COMMENT '审核时间',
    `audit_remark`    VARCHAR(255)     DEFAULT NULL COMMENT '审核备注',
    `check_in_time`   DATETIME         DEFAULT NULL COMMENT '签到时间',
    `check_in_location` VARCHAR(255)   DEFAULT NULL COMMENT '签到位置',
    `check_out_time`  DATETIME         DEFAULT NULL COMMENT '签退时间',
    `check_out_location` VARCHAR(255)  DEFAULT NULL COMMENT '签退位置',
    `reminder_sent`   TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已发送提醒：0-否，1-是',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record` (
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '志愿者ID',
    `registration_id` BIGINT UNSIGNED  NOT NULL COMMENT '报名ID',
    `service_date`    DATE             NOT NULL COMMENT '服务日期',
    `start_time`      DATETIME         NOT NULL COMMENT '服务开始时间',
    `end_time`        DATETIME         NOT NULL COMMENT '服务结束时间',
    `duration_hours`  DECIMAL(5, 2)    NOT NULL COMMENT '服务时长（小时）',
    `rating`          TINYINT UNSIGNED DEFAULT NULL COMMENT '评分：1-5星',
    `comment`         VARCHAR(500)     DEFAULT NULL COMMENT '评价内容',
    `rating_time`     DATETIME         DEFAULT NULL COMMENT '评价时间',
    `certificate_url` VARCHAR(255)     DEFAULT NULL COMMENT '服务证明URL',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_service_date` (`service_date`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `post`;
CREATE TABLE IF NOT EXISTS `post` (
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID',
    `user_id`      BIGINT UNSIGNED  NOT NULL COMMENT '发布者ID',
    `title`        VARCHAR(100)     NOT NULL COMMENT '帖子标题',
    `content`      TEXT             NOT NULL COMMENT '帖子内容',
    `type`         TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '帖子类型：1-经验分享，2-活动讨论',
    `view_count`   INT UNSIGNED     DEFAULT 0 COMMENT '浏览次数',
    `like_count`   INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `is_top`       TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `status`       TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`)
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `photo`;
CREATE TABLE IF NOT EXISTS `photo` (
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `activity_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID',
    `user_id`      BIGINT UNSIGNED  NOT NULL COMMENT '上传者ID',
    `url`          VARCHAR(255)     NOT NULL COMMENT '图片URL',
    `thumbnail_url` VARCHAR(255)    DEFAULT NULL COMMENT '缩略图URL',
    `description`  VARCHAR(255)     DEFAULT NULL COMMENT '图片描述',
    `like_count`   INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `status`       TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) COMMENT '活动照片表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`      BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
    `title`        VARCHAR(100)     NOT NULL COMMENT '通知标题',
    `content`      VARCHAR(500)     NOT NULL COMMENT '通知内容',
    `type`         TINYINT UNSIGNED NOT NULL COMMENT '通知类型：1-活动提醒，2-报名审核，3-系统通知',
    `related_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联ID（活动ID/报名ID等）',
    `is_read`      TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time`    DATETIME         DEFAULT NULL COMMENT '阅读时间',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;
