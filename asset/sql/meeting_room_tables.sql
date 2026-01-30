USE `example`;

-- 用户表（如果不存在）
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `department`  VARCHAR(100)     DEFAULT NULL COMMENT '部门',
    `role`        TINYINT UNSIGNED DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_username` (`username`)
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

-- 会议室表
DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE IF NOT EXISTS `meeting_room`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     NOT NULL COMMENT '会议室名称',
    `location`    VARCHAR(200)     DEFAULT NULL COMMENT '会议室位置',
    `capacity`    INT UNSIGNED     DEFAULT 0 COMMENT '会议室容量',
    `equipment`   VARCHAR(500)     DEFAULT NULL COMMENT '设备信息（投影仪、白板、视频会议设备等）',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '会议室描述',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-停用，1-可用',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_name` (`name`)
) COMMENT '会议室表' COLLATE = utf8mb4_unicode_ci;

-- 预约表
DROP TABLE IF EXISTS `meeting_booking`;
CREATE TABLE IF NOT EXISTS `meeting_booking`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `meeting_title` VARCHAR(200)     NOT NULL COMMENT '会议主题',
    `room_id`       BIGINT UNSIGNED  NOT NULL COMMENT '会议室ID',
    `booker_id`     BIGINT UNSIGNED  NOT NULL COMMENT '预约人ID',
    `start_time`    DATETIME         NOT NULL COMMENT '会议开始时间',
    `end_time`      DATETIME         NOT NULL COMMENT '会议结束时间',
    `reason`        VARCHAR(500)     DEFAULT NULL COMMENT '会议事由',
    `attendees`     VARCHAR(1000)    DEFAULT NULL COMMENT '参会人员（多个人员ID用逗号分隔）',
    `status`        TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消',
    `approve_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '审批人ID',
    `approve_time`  DATETIME         DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500)    DEFAULT NULL COMMENT '审批备注',
    `reminder_sent` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已发送提醒：0-未发送，1-已发送',
    `create_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除字段，0 或 1',
    INDEX `idx_room_time` (`room_id`, `start_time`, `end_time`),
    INDEX `idx_booker` (`booker_id`),
    INDEX `idx_status` (`status`)
) COMMENT '会议预约表' COLLATE = utf8mb4_unicode_ci;

-- 签到表
DROP TABLE IF EXISTS `meeting_checkin`;
CREATE TABLE IF NOT EXISTS `meeting_checkin`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `booking_id`  BIGINT UNSIGNED NOT NULL COMMENT '预约ID',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '签到人ID',
    `checkin_time` DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_booking` (`booking_id`),
    INDEX `idx_user` (`user_id`)
) COMMENT '会议签到表' COLLATE = utf8mb4_unicode_ci;

-- 通知表
DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '接收通知的用户ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`     VARCHAR(1000)    NOT NULL COMMENT '通知内容',
    `type`        TINYINT UNSIGNED DEFAULT 0 COMMENT '通知类型：0-审批结果通知，1-会议开始前通知',
    `related_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '关联ID（预约ID等）',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `read_time`   DATETIME         DEFAULT NULL COMMENT '阅读时间',
    INDEX `idx_user` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_is_read` (`is_read`)
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

-- 会议室使用统计表
DROP TABLE IF EXISTS `room_usage_stats`;
CREATE TABLE IF NOT EXISTS `room_usage_stats`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `room_id`       BIGINT UNSIGNED  NOT NULL COMMENT '会议室ID',
    `stats_date`    DATE             NOT NULL COMMENT '统计日期',
    `total_bookings` INT UNSIGNED    DEFAULT 0 COMMENT '总预约次数',
    `actual_usage`   INT UNSIGNED    DEFAULT 0 COMMENT '实际使用次数（有签到的会议）',
    `total_hours`    DECIMAL(10, 2)   DEFAULT 0.00 COMMENT '总使用时长（小时）',
    `create_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_room_date` (`room_id`, `stats_date`),
    INDEX `idx_stats_date` (`stats_date`)
) COMMENT '会议室使用统计表' COLLATE = utf8mb4_unicode_ci;