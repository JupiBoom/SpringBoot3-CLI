-- 课程管理系统数据库表结构
-- 创建时间：2025-01-16

-- 1. 课程表
CREATE TABLE IF NOT EXISTS `course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    `name` VARCHAR(255) NOT NULL COMMENT '课程名称',
    `credit` INT DEFAULT NULL COMMENT '课程学分',
    `teacher_id` BIGINT DEFAULT NULL COMMENT '教师ID',
    `teacher_name` VARCHAR(100) DEFAULT NULL COMMENT '教师姓名',
    `classroom` VARCHAR(100) DEFAULT NULL COMMENT '教室',
    `description` TEXT COMMENT '课程描述',
    `status` TINYINT DEFAULT 0 COMMENT '课程状态：0-未开始，1-进行中，2-已结束',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id` BIGINT DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_teacher_id` (`teacher_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 2. 考勤记录表
CREATE TABLE IF NOT EXISTS `attendance` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考勤ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `student_name` VARCHAR(100) DEFAULT NULL COMMENT '学生姓名',
    `status` TINYINT DEFAULT 0 COMMENT '考勤状态：0-签到，1-缺勤，2-请假，3-迟到',
    `sign_time` DATETIME DEFAULT NULL COMMENT '签到时间',
    `absent_reason` VARCHAR(500) DEFAULT NULL COMMENT '缺勤原因',
    `leave_start_time` DATETIME DEFAULT NULL COMMENT '请假开始时间',
    `leave_end_time` DATETIME DEFAULT NULL COMMENT '请假结束时间',
    `leave_reason` VARCHAR(500) DEFAULT NULL COMMENT '请假理由',
    `leave_status` TINYINT DEFAULT 0 COMMENT '请假审批状态：0-待审批，1-已批准，2-已拒绝',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_course_id` (`course_id`),
    INDEX `idx_student_id` (`student_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_sign_time` (`sign_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- 3. 课程讨论表
CREATE TABLE IF NOT EXISTS `course_discussion` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '讨论ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `user_name` VARCHAR(100) DEFAULT NULL COMMENT '用户姓名',
    `content` TEXT NOT NULL COMMENT '讨论内容',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级评论ID（用于回复）',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_course_id` (`course_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程讨论表';

-- 4. 作业表
CREATE TABLE IF NOT EXISTS `assignment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '作业ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `title` VARCHAR(255) NOT NULL COMMENT '作业标题',
    `description` TEXT COMMENT '作业描述',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `status` TINYINT DEFAULT 0 COMMENT '作业状态：0-未发布，1-已发布，2-已截止',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_course_id` (`course_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业表';

-- 5. 作业提交表
CREATE TABLE IF NOT EXISTS `assignment_submission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提交ID',
    `assignment_id` BIGINT NOT NULL COMMENT '作业ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `student_name` VARCHAR(100) DEFAULT NULL COMMENT '学生姓名',
    `content` TEXT COMMENT '提交内容',
    `attachment_path` VARCHAR(500) DEFAULT NULL COMMENT '附件路径',
    `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
    `score` INT DEFAULT NULL COMMENT '评分',
    `evaluation` TEXT COMMENT '评价内容',
    `score_time` DATETIME DEFAULT NULL COMMENT '评分时间',
    `scorer_id` BIGINT DEFAULT NULL COMMENT '评分人ID',
    `status` TINYINT DEFAULT 0 COMMENT '提交状态：0-待提交，1-已提交，2-已评分，3-已过期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_assignment_id` (`assignment_id`),
    INDEX `idx_student_id` (`student_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交表';

-- 6. 课程资料表
CREATE TABLE IF NOT EXISTS `course_material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `name` VARCHAR(255) NOT NULL COMMENT '资料名称',
    `description` TEXT COMMENT '资料描述',
    `file_path` VARCHAR(500) DEFAULT NULL COMMENT '文件路径',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) DEFAULT NULL COMMENT '文件类型',
    `download_count` INT DEFAULT 0 COMMENT '下载次数',
    `uploader_id` BIGINT DEFAULT NULL COMMENT '上传者ID',
    `uploader_name` VARCHAR(100) DEFAULT NULL COMMENT '上传者姓名',
    `status` TINYINT DEFAULT 1 COMMENT '资料状态：0-隐藏，1-公开',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` TINYINT DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_course_id` (`course_id`),
    INDEX `idx_uploader_id` (`uploader_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程资料表';

-- 插入测试数据
INSERT INTO `course` (`name`, `credit`, `teacher_id`, `teacher_name`, `classroom`, `description`, `status`, `creator_id`) VALUES 
('高等数学', 4, 1, '李教授', 'A101', '高等数学是理工科专业的基础课程', 1, 1),
('大学英语', 3, 2, '王老师', 'B202', '大学英语课程旨在提高学生的英语综合能力', 1, 1),
('计算机基础', 2, 3, '张老师', 'C303', '计算机基础课程介绍计算机的基本原理和应用', 1, 1);

INSERT INTO `assignment` (`course_id`, `title`, `description`, `deadline`, `status`) VALUES 
(1, '高等数学作业一', '完成第1-5页的习题', DATE_ADD(NOW(), INTERVAL 7 DAY), 1),
(2, '英语作文', '写一篇关于环保的英语作文', DATE_ADD(NOW(), INTERVAL 5 DAY), 1),
(3, '计算机实验报告', '完成计算机基础实验报告', DATE_ADD(NOW(), INTERVAL 3 DAY), 1);

INSERT INTO `course_material` (`course_id`, `name`, `description`, `file_path`, `file_size`, `file_type`, `uploader_id`, `uploader_name`, `status`) VALUES 
(1, '高等数学课件第一章', '高等数学第一章课件PPT', '/uploads/material/math_ch1.ppt', 10485760, 'application/vnd.ms-powerpoint', 1, '李教授', 1),
(2, '英语听力材料', '大学英语听力MP3', '/uploads/material/english_listening.mp3', 5242880, 'audio/mpeg', 2, '王老师', 1),
(3, '计算机基础教材', '计算机基础教材PDF', '/uploads/material/comp_book.pdf', 20971520, 'application/pdf', 3, '张老师', 1);

INSERT INTO `course_discussion` (`course_id`, `user_id`, `user_name`, `content`, `parent_id`, `like_count`) VALUES 
(1, 1001, '张三', '请问这道题的解题思路是什么？', NULL, 5),
(1, 1, '李教授', '这位同学你好，这道题需要用导数的方法来解。', 1, 8),
(2, 1002, '李四', '这个听力材料好难啊！', NULL, 3);