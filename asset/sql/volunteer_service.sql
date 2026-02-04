-- 社区志愿者服务平台数据库脚本
USE `volunteer_service`;

-- ============================================
-- 1. 用户表 (复用或扩展已有用户表)
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '用户ID' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(500)     DEFAULT NULL COMMENT '头像URL',
    `id_card`     VARCHAR(18)      DEFAULT NULL COMMENT '身份证号',
    `address`     VARCHAR(200)     DEFAULT NULL COMMENT '地址',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone`   VARCHAR(20) DEFAULT NULL COMMENT '紧急联系人电话',
    `skills`      VARCHAR(500)     DEFAULT NULL COMMENT '技能特长',
    `experience`  TEXT             DEFAULT NULL COMMENT '志愿服务经历',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    `role`        TINYINT UNSIGNED DEFAULT 1 COMMENT '角色：0-管理员 1-志愿者 2-组织者',
    `total_hours` DECIMAL(10, 2)   DEFAULT 0 COMMENT '累计服务时长（小时）',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 2. 活动表
-- ============================================
DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity`
(
    `id`               BIGINT UNSIGNED AUTO_INCREMENT COMMENT '活动ID' PRIMARY KEY,
    `title`            VARCHAR(200)     NOT NULL COMMENT '活动标题',
    `description`      TEXT             DEFAULT NULL COMMENT '活动描述',
    `category`         TINYINT UNSIGNED NOT NULL COMMENT '活动分类：1-环保 2-助老 3-教育 4-医疗 5-其他',
    `status`           TINYINT UNSIGNED DEFAULT 0 COMMENT '活动状态：0-招募中 1-进行中 2-已完成 3-已取消',
    `start_time`       DATETIME         NOT NULL COMMENT '活动开始时间',
    `end_time`         DATETIME         NOT NULL COMMENT '活动结束时间',
    `registration_start` DATETIME       DEFAULT NULL COMMENT '报名开始时间',
    `registration_end`   DATETIME       DEFAULT NULL COMMENT '报名结束时间',
    `location`         VARCHAR(300)     NOT NULL COMMENT '活动地点',
    `location_detail`  VARCHAR(500)     DEFAULT NULL COMMENT '详细地址',
    `longitude`        DECIMAL(10, 7)   DEFAULT NULL COMMENT '经度',
    `latitude`         DECIMAL(10, 7)   DEFAULT NULL COMMENT '纬度',
    `required_people`  INT UNSIGNED     NOT NULL COMMENT '需求人数',
    `registered_people` INT UNSIGNED    DEFAULT 0 COMMENT '已报名人数',
    `confirmed_people` INT UNSIGNED     DEFAULT 0 COMMENT '已确认人数',
    `contact_name`     VARCHAR(50)      DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone`    VARCHAR(20)      DEFAULT NULL COMMENT '联系人电话',
    `requirements`     VARCHAR(500)     DEFAULT NULL COMMENT '报名要求',
    `materials`        VARCHAR(500)     DEFAULT NULL COMMENT '需要携带的物品',
    `notes`            TEXT             DEFAULT NULL COMMENT '活动备注',
    `cover_image`      VARCHAR(500)     DEFAULT NULL COMMENT '活动封面图',
    `organizer_id`     BIGINT UNSIGNED  NOT NULL COMMENT '组织者ID',
    `organizer_name`   VARCHAR(100)     DEFAULT NULL COMMENT '组织者名称',
    `view_count`       INT UNSIGNED     DEFAULT 0 COMMENT '浏览次数',
    `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          INT UNSIGNED     DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`       TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '活动表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 3. 报名表
-- ============================================
DROP TABLE IF EXISTS `registration`;
CREATE TABLE IF NOT EXISTS `registration`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '报名ID' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '报名状态：0-待审核 1-已通过 2-已拒绝 3-已取消 4-已签到 5-已签出',
    `message`         VARCHAR(500)     DEFAULT NULL COMMENT '报名留言',
    `reject_reason`   VARCHAR(500)     DEFAULT NULL COMMENT '拒绝原因',
    `reviewer_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '审核人ID',
    `review_time`     DATETIME         DEFAULT NULL COMMENT '审核时间',
    `check_in_time`   DATETIME         DEFAULT NULL COMMENT '签到时间',
    `check_out_time`  DATETIME         DEFAULT NULL COMMENT '签出时间',
    `check_in_location` VARCHAR(300)   DEFAULT NULL COMMENT '签到地点',
    `check_out_location` VARCHAR(300)  DEFAULT NULL COMMENT '签出地点',
    `reminder_sent`   TINYINT UNSIGNED DEFAULT 0 COMMENT '提醒是否发送：0-未发送 1-已发送',
    `reminder_time`   DATETIME         DEFAULT NULL COMMENT '提醒发送时间',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         INT UNSIGNED     DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`)
) COMMENT '报名表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 4. 服务记录表
-- ============================================
DROP TABLE IF EXISTS `service_record`;
CREATE TABLE IF NOT EXISTS `service_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '记录ID' PRIMARY KEY,
    `activity_id`     BIGINT UNSIGNED  NOT NULL COMMENT '活动ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `registration_id` BIGINT UNSIGNED  NOT NULL COMMENT '报名ID',
    `service_date`    DATE             NOT NULL COMMENT '服务日期',
    `start_time`      DATETIME         NOT NULL COMMENT '开始时间',
    `end_time`        DATETIME         DEFAULT NULL COMMENT '结束时间',
    `duration_hours`  DECIMAL(5, 2)    DEFAULT 0 COMMENT '服务时长（小时）',
    `service_content` TEXT             DEFAULT NULL COMMENT '服务内容',
    `performance`     TINYINT UNSIGNED DEFAULT NULL COMMENT '服务表现：1-5星评价',
    `organizer_comment` VARCHAR(500)   DEFAULT NULL COMMENT '组织者评价',
    `volunteer_comment` VARCHAR(500)   DEFAULT NULL COMMENT '志愿者评价',
    `certificate_no`  VARCHAR(50)      DEFAULT NULL COMMENT '服务证明编号',
    `certificate_url` VARCHAR(500)     DEFAULT NULL COMMENT '服务证明PDF链接',
    `certificate_generated` TINYINT UNSIGNED DEFAULT 0 COMMENT '证明是否生成：0-未生成 1-已生成',
    `certificate_generate_time` DATETIME DEFAULT NULL COMMENT '证明生成时间',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    UNIQUE KEY `uk_registration` (`registration_id`)
) COMMENT '服务记录表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 5. 活动论坛帖子表
-- ============================================
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE IF NOT EXISTS `forum_post`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '帖子ID' PRIMARY KEY,
    `activity_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID（可选）',
    `user_id`       BIGINT UNSIGNED  NOT NULL COMMENT '发布者ID',
    `title`         VARCHAR(200)     NOT NULL COMMENT '帖子标题',
    `content`       TEXT             NOT NULL COMMENT '帖子内容',
    `type`          TINYINT UNSIGNED DEFAULT 1 COMMENT '帖子类型：1-经验分享 2-活动回顾 3-问题咨询 4-其他',
    `images`        JSON             DEFAULT NULL COMMENT '图片列表JSON数组',
    `view_count`    INT UNSIGNED     DEFAULT 0 COMMENT '浏览次数',
    `like_count`    INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT UNSIGNED     DEFAULT 0 COMMENT '评论数',
    `is_top`        TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
    `is_essence`    TINYINT UNSIGNED DEFAULT 0 COMMENT '是否精华：0-否 1-是',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-待审核 1-已通过 2-已拒绝',
    `create_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '论坛帖子表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 6. 论坛评论表
-- ============================================
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE IF NOT EXISTS `forum_comment`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '评论ID' PRIMARY KEY,
    `post_id`     BIGINT UNSIGNED  NOT NULL COMMENT '帖子ID',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '评论者ID',
    `parent_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '父评论ID（回复功能）',
    `content`     TEXT             NOT NULL COMMENT '评论内容',
    `like_count`  INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-待审核 1-已通过 2-已拒绝',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '论坛评论表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 7. 照片墙表
-- ============================================
DROP TABLE IF EXISTS `photo_wall`;
CREATE TABLE IF NOT EXISTS `photo_wall`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '照片ID' PRIMARY KEY,
    `activity_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联活动ID',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '上传者ID',
    `title`       VARCHAR(200)     DEFAULT NULL COMMENT '照片标题',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '照片描述',
    `image_url`   VARCHAR(500)     NOT NULL COMMENT '图片URL',
    `thumbnail_url` VARCHAR(500)   DEFAULT NULL COMMENT '缩略图URL',
    `is_featured` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否精选：0-否 1-是',
    `like_count`  INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-待审核 1-已通过 2-已拒绝',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '照片墙表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 8. 系统配置表
-- ============================================
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE IF NOT EXISTS `system_config`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '配置ID' PRIMARY KEY,
    `config_key`  VARCHAR(100)     NOT NULL COMMENT '配置键',
    `config_value` VARCHAR(500)    DEFAULT NULL COMMENT '配置值',
    `description` VARCHAR(200)     DEFAULT NULL COMMENT '配置说明',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_config_key` (`config_key`)
) COMMENT '系统配置表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 9. 通知消息表
-- ============================================
DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '通知ID' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
    `type`        TINYINT UNSIGNED NOT NULL COMMENT '通知类型：1-活动提醒 2-报名结果 3-系统通知',
    `title`       VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`     TEXT             NOT NULL COMMENT '通知内容',
    `related_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联ID（活动ID或报名ID）',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
    `read_time`   DATETIME         DEFAULT NULL COMMENT '阅读时间',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除'
) COMMENT '通知消息表' COLLATE = utf8mb4_unicode_ci;

-- ============================================
-- 创建索引
-- ============================================
CREATE INDEX `idx_activity_status` ON `activity` (`status`);
CREATE INDEX `idx_activity_category` ON `activity` (`category`);
CREATE INDEX `idx_activity_start_time` ON `activity` (`start_time`);
CREATE INDEX `idx_activity_organizer` ON `activity` (`organizer_id`);
CREATE INDEX `idx_registration_activity` ON `registration` (`activity_id`);
CREATE INDEX `idx_registration_user` ON `registration` (`user_id`);
CREATE INDEX `idx_registration_status` ON `registration` (`status`);
CREATE INDEX `idx_service_record_user` ON `service_record` (`user_id`);
CREATE INDEX `idx_service_record_activity` ON `service_record` (`activity_id`);
CREATE INDEX `idx_forum_post_activity` ON `forum_post` (`activity_id`);
CREATE INDEX `idx_forum_post_user` ON `forum_post` (`user_id`);
CREATE INDEX `idx_forum_post_type` ON `forum_post` (`type`);
CREATE INDEX `idx_forum_comment_post` ON `forum_comment` (`post_id`);
CREATE INDEX `idx_photo_wall_activity` ON `photo_wall` (`activity_id`);
CREATE INDEX `idx_notification_user` ON `notification` (`user_id`);
CREATE INDEX `idx_notification_unread` ON `notification` (`user_id`, `is_read`);

-- ============================================
-- 插入默认数据
-- ============================================
-- 系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('site.name', '社区志愿者服务平台', '网站名称'),
('site.logo', '/logo.png', '网站Logo'),
('activity.reminder.minutes', '60', '活动开始前提醒时间（分钟）'),
('certificate.template', 'default', '服务证明模板'),
('auto.approval', 'false', '是否自动审核报名');

-- 管理员账号（密码：admin123）
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '系统管理员', '13800138000', 'admin@example.com', 0, 1);
