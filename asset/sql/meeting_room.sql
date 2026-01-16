-- 会议室管理系统数据库表结构

-- 会议室表
CREATE TABLE IF NOT EXISTS meeting_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '会议室ID',
    room_name VARCHAR(100) NOT NULL COMMENT '会议室名称',
    location VARCHAR(200) NOT NULL COMMENT '位置',
    capacity INT NOT NULL COMMENT '容量',
    equipment VARCHAR(500) COMMENT '设备（JSON格式存储）',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_by BIGINT COMMENT '创建人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_room_name (room_name),
    INDEX idx_location (location),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- 预约申请表
CREATE TABLE IF NOT EXISTS meeting_reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预约ID',
    room_id BIGINT NOT NULL COMMENT '会议室ID',
    applicant_id BIGINT NOT NULL COMMENT '申请人ID',
    applicant_name VARCHAR(50) NOT NULL COMMENT '申请人姓名',
    meeting_subject VARCHAR(200) NOT NULL COMMENT '会议主题',
    meeting_reason VARCHAR(500) COMMENT '会议事由',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    attendees TEXT COMMENT '参会人员（JSON格式存储）',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待审批，1-已通过，2-已驳回，3-已取消，4-已完成',
    remark VARCHAR(500) COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_room_id (room_id),
    INDEX idx_applicant_id (applicant_id),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    FOREIGN KEY (room_id) REFERENCES meeting_room(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约申请表';

-- 审批记录表
CREATE TABLE IF NOT EXISTS approval_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '审批记录ID',
    reservation_id BIGINT NOT NULL COMMENT '预约ID',
    approver_id BIGINT NOT NULL COMMENT '审批人ID',
    approver_name VARCHAR(50) NOT NULL COMMENT '审批人姓名',
    action TINYINT NOT NULL COMMENT '审批动作：1-通过，2-驳回',
    reason VARCHAR(500) COMMENT '审批原因',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '审批时间',
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_approver_id (approver_id),
    FOREIGN KEY (reservation_id) REFERENCES meeting_reservation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批记录表';

-- 签到记录表
CREATE TABLE IF NOT EXISTS check_in_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '签到记录ID',
    reservation_id BIGINT NOT NULL COMMENT '预约ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) NOT NULL COMMENT '用户姓名',
    check_in_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    check_in_type TINYINT DEFAULT 1 COMMENT '签到类型：1-正常签到，2-迟到签到',
    remark VARCHAR(200) COMMENT '备注',
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_user_id (user_id),
    INDEX idx_check_in_time (check_in_time),
    FOREIGN KEY (reservation_id) REFERENCES meeting_reservation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 通知记录表
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    notification_type TINYINT NOT NULL COMMENT '通知类型：1-审批结果通知，2-会议开始提醒',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    related_id BIGINT COMMENT '关联ID（预约ID）',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读：0-否，1-是',
    read_time DATETIME COMMENT '阅读时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_notification_type (notification_type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知记录表';

-- 会议室使用统计表（可用于统计分析）
CREATE TABLE IF NOT EXISTS meeting_room_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    room_id BIGINT NOT NULL COMMENT '会议室ID',
    statistics_date DATE NOT NULL COMMENT '统计日期',
    total_reservations INT DEFAULT 0 COMMENT '总预约次数',
    completed_reservations INT DEFAULT 0 COMMENT '完成次数',
    cancelled_reservations INT DEFAULT 0 COMMENT '取消次数',
    total_hours DECIMAL(10,2) DEFAULT 0 COMMENT '总使用时长（小时）',
    avg_attendance DECIMAL(5,2) DEFAULT 0 COMMENT '平均参会人数',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_room_date (room_id, statistics_date),
    INDEX idx_room_id (room_id),
    INDEX idx_statistics_date (statistics_date),
    FOREIGN KEY (room_id) REFERENCES meeting_room(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室使用统计表';
