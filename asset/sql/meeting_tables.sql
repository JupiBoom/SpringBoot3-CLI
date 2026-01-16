USE `example`;

DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE IF NOT EXISTS `meeting_room`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     NOT NULL COMMENT '会议室名称',
    `location`    VARCHAR(200)     NOT NULL COMMENT '会议室位置',
    `capacity`    INT UNSIGNED     NOT NULL COMMENT '会议室容量（人数）',
    `equipment`   VARCHAR(500)     DEFAULT NULL COMMENT '设备配置（投影仪、白板等）',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '会议室表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `meeting_reservation`;
CREATE TABLE IF NOT EXISTS `meeting_reservation`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `room_id`       BIGINT UNSIGNED  NOT NULL COMMENT '会议室ID',
    `title`         VARCHAR(200)     NOT NULL COMMENT '会议主题',
    `reason`        VARCHAR(500)     DEFAULT NULL COMMENT '事由说明',
    `start_time`    DATETIME         NOT NULL COMMENT '开始时间',
    `end_time`      DATETIME         NOT NULL COMMENT '结束时间',
    `applicant_id`  BIGINT UNSIGNED  NOT NULL COMMENT '申请人ID',
    `status`        TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消',
    `attendees`     INT UNSIGNED     DEFAULT 0 COMMENT '参会人数',
    `creator_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`    TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_room_time` (`room_id`, `start_time`, `end_time`),
    INDEX `idx_applicant` (`applicant_id`)
) COMMENT '会议预约表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `meeting_approval`;
CREATE TABLE IF NOT EXISTS `meeting_approval`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `reservation_id`  BIGINT UNSIGNED  NOT NULL COMMENT '预约ID',
    `approver_id`     BIGINT UNSIGNED  NOT NULL COMMENT '审批人ID',
    `approval_status` TINYINT UNSIGNED NOT NULL COMMENT '审批状态：1-通过，2-驳回',
    `approval_comment` VARCHAR(500)    DEFAULT NULL COMMENT '审批意见',
    `approval_time`   DATETIME         NOT NULL COMMENT '审批时间',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_reservation` (`reservation_id`),
    INDEX `idx_approver` (`approver_id`)
) COMMENT '会议审批表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `meeting_checkin`;
CREATE TABLE IF NOT EXISTS `meeting_checkin`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `reservation_id` BIGINT UNSIGNED  NOT NULL COMMENT '预约ID',
    `user_id`        BIGINT UNSIGNED  NOT NULL COMMENT '签到人ID',
    `checkin_time`   DATETIME         NOT NULL COMMENT '签到时间',
    `checkin_type`   TINYINT UNSIGNED DEFAULT 1 COMMENT '签到类型：1-正常签到，2-迟到',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_reservation` (`reservation_id`),
    INDEX `idx_user` (`user_id`)
) COMMENT '会议签到表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `meeting_notification`;
CREATE TABLE IF NOT EXISTS `meeting_notification`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `reservation_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '预约ID',
    `user_id`        BIGINT UNSIGNED  NOT NULL COMMENT '接收人ID',
    `notification_type` TINYINT UNSIGNED NOT NULL COMMENT '通知类型：1-审批通过，2-审批驳回，3-会议开始提醒，4-会议取消',
    `title`          VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`        VARCHAR(500)     NOT NULL COMMENT '通知内容',
    `is_read`        TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_user` (`user_id`),
    INDEX `idx_reservation` (`reservation_id`)
) COMMENT '会议通知表' COLLATE = utf8mb4_unicode_ci;
