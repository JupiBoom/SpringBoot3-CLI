-- 社区志愿者服务平台数据库表结构

-- 创建数据库
CREATE DATABASE IF NOT EXISTS community_volunteer_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_volunteer_platform;

-- 1. 活动表
CREATE TABLE IF NOT EXISTS `activity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    `name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `description` TEXT COMMENT '活动描述',
    `activity_time` DATETIME NOT NULL COMMENT '活动时间',
    `location` VARCHAR(200) NOT NULL COMMENT '活动地点',
    `required_people` INT NOT NULL COMMENT '需求人数',
    `registered_people` INT DEFAULT 0 COMMENT '已报名人数',
    `category` TINYINT NOT NULL COMMENT '活动分类：1-环保，2-助老，3-教育，4-医疗',
    `status` TINYINT DEFAULT 1 COMMENT '活动状态：1-招募中，2-进行中，3-已完成',
    `cover_image` VARCHAR(500) COMMENT '活动封面图片',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_category` (`category`),
    INDEX `idx_status` (`status`),
    INDEX `idx_activity_time` (`activity_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 2. 活动报名表
CREATE TABLE IF NOT EXISTS `activity_registration` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报名ID',
    `activity_id` BIGINT NOT NULL COMMENT '活动ID',
    `volunteer_id` BIGINT NOT NULL COMMENT '志愿者ID',
    `volunteer_name` VARCHAR(100) NOT NULL COMMENT '志愿者姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `remark` VARCHAR(500) COMMENT '报名备注',
    `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
    `audit_remark` VARCHAR(500) COMMENT '审核备注',
    `auditor_id` BIGINT COMMENT '审核人ID',
    `audit_time` DATETIME COMMENT '审核时间',
    `is_signed_in` TINYINT DEFAULT 0 COMMENT '是否签到：0-未签到，1-已签到',
    `sign_in_time` DATETIME COMMENT '签到时间',
    `is_signed_out` TINYINT DEFAULT 0 COMMENT '是否签出：0-未签出，1-已签出',
    `sign_out_time` DATETIME COMMENT '签出时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_volunteer_id` (`volunteer_id`),
    INDEX `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名表';

-- 3. 服务记录表
CREATE TABLE IF NOT EXISTS `service_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务记录ID',
    `activity_id` BIGINT NOT NULL COMMENT '活动ID',
    `volunteer_id` BIGINT NOT NULL COMMENT '志愿者ID',
    `volunteer_name` VARCHAR(100) NOT NULL COMMENT '志愿者姓名',
    `activity_name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `activity_category` TINYINT NOT NULL COMMENT '活动分类',
    `sign_in_time` DATETIME NOT NULL COMMENT '签到时间',
    `sign_out_time` DATETIME COMMENT '签出时间',
    `service_duration` INT COMMENT '服务时长（分钟）',
    `rating` TINYINT COMMENT '服务评分（1-5星）',
    `evaluation` TEXT COMMENT '服务评价内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_volunteer_id` (`volunteer_id`),
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务记录表';

-- 4. 论坛帖子表
CREATE TABLE IF NOT EXISTS `forum_post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `title` VARCHAR(300) NOT NULL COMMENT '帖子标题',
    `content` TEXT NOT NULL COMMENT '帖子内容',
    `activity_id` BIGINT COMMENT '关联活动ID',
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `user_name` VARCHAR(100) NOT NULL COMMENT '发布者姓名',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 5. 帖子图片表
CREATE TABLE IF NOT EXISTS `post_image` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `image_url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `image_order` INT DEFAULT 0 COMMENT '图片排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子图片表';

-- 6. 志愿者表
CREATE TABLE IF NOT EXISTS `volunteer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '志愿者ID',
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `gender` TINYINT COMMENT '性别：1-男，2-女',
    `age` INT COMMENT '年龄',
    `avatar` VARCHAR(500) COMMENT '头像',
    `total_service_duration` INT DEFAULT 0 COMMENT '总服务时长（分钟）',
    `service_count` INT DEFAULT 0 COMMENT '服务次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿者表';

-- 7. 通知表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT NOT NULL COMMENT '通知内容',
    `notification_type` TINYINT COMMENT '通知类型：1-活动提醒，2-审核结果，3-系统通知',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 插入测试数据

-- 插入活动分类测试数据
INSERT INTO `activity` (`name`, `description`, `activity_time`, `location`, `required_people`, `registered_people`, `category`, `status`, `cover_image`, `creator_id`) VALUES 
('环保公益活动-公园清洁', '组织志愿者到城市公园进行环境清洁，维护公园整洁', '2026-02-01 09:00:00', '城市中央公园', 50, 0, 1, 1, '/images/activity1.jpg', 1),
('关爱老人活动-社区探访', '组织志愿者到社区养老院探访老人，陪伴聊天', '2026-02-02 14:00:00', '幸福养老院', 30, 0, 2, 1, '/images/activity2.jpg', 1),
('教育帮扶活动-支教助学', '为山区留守儿童提供课外辅导和教育支持', '2026-02-03 08:30:00', '希望小学', 20, 0, 3, 1, '/images/activity3.jpg', 1),
('医疗服务活动-义诊进社区', '组织医疗志愿者为社区居民提供免费义诊服务', '2026-02-04 09:00:00', '社区服务中心', 40, 0, 4, 1, '/images/activity4.jpg', 1);

-- 插入志愿者测试数据
INSERT INTO `volunteer` (`name`, `phone`, `email`, `gender`, `age`, `avatar`, `total_service_duration`, `service_count`) VALUES 
('张三', '13800138001', 'zhangsan@example.com', 1, 25, '/avatars/zhangsan.jpg', 1200, 10),
('李四', '13800138002', 'lisi@example.com', 2, 28, '/avatars/lisi.jpg', 1800, 15),
('王五', '13800138003', 'wangwu@example.com', 1, 30, '/avatars/wangwu.jpg', 2400, 20);