-- 课程管理系统数据库设计
USE `example`;

-- 1. 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '课程ID',
    `course_name` VARCHAR(100)     NOT NULL COMMENT '课程名称',
    `credits`     DECIMAL(3,1)     NOT NULL COMMENT '学分',
    `teacher_id`  BIGINT UNSIGNED  NOT NULL COMMENT '教师ID',
    `classroom`   VARCHAR(50)      NOT NULL COMMENT '教室',
    `start_time`  DATETIME         NOT NULL COMMENT '上课时间',
    `end_time`    DATETIME         NOT NULL COMMENT '下课时间',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '课程描述',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-已结束，1-进行中',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '课程表' COLLATE = utf8mb4_unicode_ci;

-- 2. 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '学生ID',
    `student_no`  VARCHAR(50)      NOT NULL UNIQUE COMMENT '学号',
    `name`        VARCHAR(50)      NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED NOT NULL COMMENT '性别：0-女，1-男',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `major`       VARCHAR(100)     DEFAULT NULL COMMENT '专业',
    `grade`       VARCHAR(20)      DEFAULT NULL COMMENT '年级',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '学生表' COLLATE = utf8mb4_unicode_ci;

-- 3. 教师表
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '教师ID',
    `teacher_no`  VARCHAR(50)      NOT NULL UNIQUE COMMENT '教师编号',
    `name`        VARCHAR(50)      NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED NOT NULL COMMENT '性别：0-女，1-男',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `title`       VARCHAR(50)      DEFAULT NULL COMMENT '职称',
    `department`  VARCHAR(100)     DEFAULT NULL COMMENT '院系',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '教师表' COLLATE = utf8mb4_unicode_ci;

-- 4. 学生选课表
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE IF NOT EXISTS `student_course`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '选课记录ID',
    `student_id`  BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `score`       DECIMAL(5,2)     DEFAULT NULL COMMENT '成绩',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-已退课，1-在读',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_student_course` (`student_id`, `course_id`)
) COMMENT '学生选课表' COLLATE = utf8mb4_unicode_ci;

-- 5. 考勤表
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '考勤ID',
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `attendance_date` DATE         NOT NULL COMMENT '考勤日期',
    `status`      TINYINT UNSIGNED NOT NULL COMMENT '考勤状态：0-缺席，1-出勤，2-请假',
    `leave_type`  TINYINT UNSIGNED DEFAULT NULL COMMENT '请假类型：1-事假，2-病假，3-其他',
    `leave_reason` VARCHAR(500)    DEFAULT NULL COMMENT '请假原因',
    `leave_status` TINYINT UNSIGNED DEFAULT 0 COMMENT '请假审批状态：0-待审批，1-已批准，2-已拒绝',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_attendance` (`course_id`, `student_id`, `attendance_date`)
) COMMENT '考勤表' COLLATE = utf8mb4_unicode_ci;

-- 6. 课程讨论表
DROP TABLE IF EXISTS `course_discussion`;
CREATE TABLE IF NOT EXISTS `course_discussion`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '讨论ID',
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '用户ID（学生/教师）',
    `user_type`   TINYINT UNSIGNED NOT NULL COMMENT '用户类型：1-学生，2-教师',
    `content`     TEXT             NOT NULL COMMENT '讨论内容',
    `parent_id`   BIGINT UNSIGNED  DEFAULT 0 COMMENT '父级ID，0表示主帖',
    `reply_count` INT UNSIGNED     DEFAULT 0 COMMENT '回复数',
    `like_count`  INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    KEY `idx_course_id` (`course_id`),
    KEY `idx_parent_id` (`parent_id`)
) COMMENT '课程讨论表' COLLATE = utf8mb4_unicode_ci;

-- 7. 作业表
DROP TABLE IF EXISTS `homework`;
CREATE TABLE IF NOT EXISTS `homework`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '作业ID',
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '作业标题',
    `description` TEXT             DEFAULT NULL COMMENT '作业描述',
    `deadline`    DATETIME         NOT NULL COMMENT '截止时间',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-已关闭，1-进行中',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '作业表' COLLATE = utf8mb4_unicode_ci;

-- 8. 作业提交表
DROP TABLE IF EXISTS `homework_submit`;
CREATE TABLE IF NOT EXISTS `homework_submit`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '提交ID',
    `homework_id` BIGINT UNSIGNED  NOT NULL COMMENT '作业ID',
    `student_id`  BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `content`     TEXT             DEFAULT NULL COMMENT '作业内容',
    `file_url`    VARCHAR(500)     DEFAULT NULL COMMENT '附件URL',
    `submit_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `score`       DECIMAL(5,2)     DEFAULT NULL COMMENT '评分',
    `feedback`    TEXT             DEFAULT NULL COMMENT '评语',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-未提交，1-已提交，2-已批改',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_homework_student` (`homework_id`, `student_id`)
) COMMENT '作业提交表' COLLATE = utf8mb4_unicode_ci;

-- 9. 课程资料表
DROP TABLE IF EXISTS `course_material`;
CREATE TABLE IF NOT EXISTS `course_material`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '资料ID',
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '资料标题',
    `file_url`    VARCHAR(500)     NOT NULL COMMENT '文件URL',
    `file_size`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`   VARCHAR(50)      DEFAULT NULL COMMENT '文件类型',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '资料描述',
    `download_count` INT UNSIGNED  DEFAULT 0 COMMENT '下载次数',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '课程资料表' COLLATE = utf8mb4_unicode_ci;

-- 插入测试数据
-- 教师数据
INSERT INTO `teacher` (`teacher_no`, `name`, `gender`, `phone`, `email`, `title`, `department`) VALUES
('T001', '张教授', 1, '13800138001', 'zhang@example.com', '教授', '计算机学院'),
('T002', '李老师', 0, '13800138002', 'li@example.com', '副教授', '电子工程系'),
('T003', '王讲师', 1, '13800138003', 'wang@example.com', '讲师', '数学学院');

-- 学生数据
INSERT INTO `student` (`student_no`, `name`, `gender`, `phone`, `email`, `major`, `grade`) VALUES
('S2021001', '小明', 1, '13900139001', 'xiaoming@example.com', '计算机科学与技术', '2021级'),
('S2021002', '小红', 0, '13900139002', 'xiaohong@example.com', '软件工程', '2021级'),
('S2021003', '小刚', 1, '13900139003', 'xiaogang@example.com', '数据科学与大数据技术', '2021级'),
('S2022001', '小丽', 0, '13900139004', 'xiaoli@example.com', '人工智能', '2022级');

-- 课程数据
INSERT INTO `course` (`course_name`, `credits`, `teacher_id`, `classroom`, `start_time`, `end_time`, `description`, `status`) VALUES
('Java编程基础', 4.0, 1, 'A101', '2024-09-01 08:00:00', '2024-12-31 10:00:00', 'Java编程语言入门课程', 1),
('数据结构', 3.5, 1, 'A102', '2024-09-01 10:00:00', '2024-12-31 12:00:00', '数据结构与算法分析', 1),
('电路原理', 4.0, 2, 'B201', '2024-09-01 14:00:00', '2024-12-31 16:00:00', '电路理论基础', 1),
('高等数学', 5.0, 3, 'C301', '2024-09-01 08:00:00', '2024-12-31 10:00:00', '高等数学A', 1);

-- 学生选课数据
INSERT INTO `student_course` (`student_id`, `course_id`, `status`) VALUES
(1, 1, 1),
(1, 2, 1),
(2, 1, 1),
(2, 3, 1),
(3, 2, 1),
(3, 4, 1),
(4, 4, 1);

-- 考勤数据（最近7天）
INSERT INTO `attendance` (`course_id`, `student_id`, `attendance_date`, `status`, `leave_type`, `leave_reason`, `leave_status`) VALUES
-- Java编程基础 - 小明
(1, 1, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, NULL, NULL, NULL),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 1, NULL, NULL, NULL),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 2, 1, '家里有事', 1),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 1, NULL, NULL, NULL),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 1, NULL, NULL, NULL),
(1, 1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 1, NULL, NULL, NULL),
(1, 1, CURDATE(), 1, NULL, NULL, NULL),
-- Java编程基础 - 小红
(1, 2, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 1, NULL, NULL, NULL),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 0, NULL, NULL, NULL),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1, NULL, NULL, NULL),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 2, 2, '生病请假', 1),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 1, NULL, NULL, NULL),
(1, 2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 1, NULL, NULL, NULL),
(1, 2, CURDATE(), 1, NULL, NULL, NULL);

-- 课程讨论数据
INSERT INTO `course_discussion` (`course_id`, `user_id`, `user_type`, `content`, `parent_id`) VALUES
(1, 1, 1, '请问Java中的HashMap和HashTable有什么区别？', 0),
(1, 1, 2, 'HashMap线程不安全，效率高；HashTable线程安全，效率较低。HashMap可以存储null值，HashTable不可以。', 1),
(1, 2, 1, '明白了，谢谢老师！', 1),
(2, 3, 1, '二叉树的遍历有哪几种方式？', 0),
(2, 1, 2, '主要有前序遍历、中序遍历、后序遍历和层序遍历四种方式。', 4);

-- 作业数据
INSERT INTO `homework` (`course_id`, `title`, `description`, `deadline`, `status`) VALUES
(1, 'Java基础语法作业', '完成教材第1-5章的练习题', '2024-10-01 23:59:59', 1),
(1, '面向对象编程作业', '设计一个学生管理系统', '2024-10-15 23:59:59', 1),
(2, '数据结构实验', '实现链表的增删改查', '2024-10-10 23:59:59', 1);

-- 作业提交数据
INSERT INTO `homework_submit` (`homework_id`, `student_id`, `content`, `score`, `feedback`, `status`) VALUES
(1, 1, '已完成所有练习题', 95.0, '作业完成得很好，继续保持！', 2),
(1, 2, '已完成作业', 88.0, '基础掌握不错，有些细节需要注意', 2),
(2, 1, '正在完成中...', NULL, NULL, 1);

-- 课程资料数据
INSERT INTO `course_material` (`course_id`, `title`, `file_url`, `file_size`, `file_type`, `description`) VALUES
(1, 'Java编程基础课件', '/uploads/material/java_basic.pptx', 1048576, 'pptx', '第1-5章课件'),
(1, 'Java API文档', '/uploads/material/java_api.pdf', 2097152, 'pdf', 'Java官方API文档'),
(2, '数据结构教材', '/uploads/material/ds_book.pdf', 5242880, 'pdf', '数据结构与算法分析教材'),
(2, '实验指导书', '/uploads/material/ds_lab.docx', 1048576, 'docx', '实验课指导书');