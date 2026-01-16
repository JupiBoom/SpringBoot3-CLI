-- 会议室表
CREATE TABLE IF NOT EXISTS `meeting_room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
    `name` VARCHAR(100) NOT NULL COMMENT '会议室名称',
    `location` VARCHAR(255) NOT NULL COMMENT '位置',
    `capacity` INT NOT NULL COMMENT '容量',
    `equipment` TEXT COMMENT '设备（JSON格式）',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-可用，1-不可用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    INDEX `idx_name` (`name`),
    INDEX `idx_location` (`location`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- 会议预约表
CREATE TABLE IF NOT EXISTS `meeting_booking` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `meeting_room_id` BIGINT NOT NULL COMMENT '会议室ID',
    `user_id` BIGINT NOT NULL COMMENT '预约人ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT '预约人姓名',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `reason` VARCHAR(500) NOT NULL COMMENT '事由',
    `participant_count` INT NOT NULL COMMENT '参会人数',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `check_in_count` INT DEFAULT 0 COMMENT '签到人数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_meeting_room_id` (`meeting_room_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_time_range` (`start_time`, `end_time`),
    FOREIGN KEY (`meeting_room_id`) REFERENCES `meeting_room`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议预约表';

-- 会议签到表
CREATE TABLE IF NOT EXISTS `meeting_check_in` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '签到ID',
    `booking_id` BIGINT NOT NULL COMMENT '预约ID',
    `user_id` BIGINT NOT NULL COMMENT '签到人ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT '签到人姓名',
    `check_in_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    PRIMARY KEY (`id`),
    INDEX `idx_booking_id` (`booking_id`),
    INDEX `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_booking_user` (`booking_id`, `user_id`),
    FOREIGN KEY (`booking_id`) REFERENCES `meeting_booking`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议签到表';

-- 会议室使用统计表
CREATE TABLE IF NOT EXISTS `meeting_room_statistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `meeting_room_id` BIGINT NOT NULL COMMENT '会议室ID',
    `statistics_date` DATE NOT NULL COMMENT '统计日期',
    `booking_count` INT DEFAULT 0 COMMENT '预约次数',
    `actual_use_count` INT DEFAULT 0 COMMENT '实际使用次数',
    `total_meeting_duration` INT DEFAULT 0 COMMENT '总会议时长（分钟）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_date` (`meeting_room_id`, `statistics_date`),
    INDEX `idx_meeting_room_id` (`meeting_room_id`),
    INDEX `idx_statistics_date` (`statistics_date`),
    FOREIGN KEY (`meeting_room_id`) REFERENCES `meeting_room`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室使用统计表';