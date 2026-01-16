USE `example`;

DROP TABLE IF EXISTS `item`;
CREATE TABLE IF NOT EXISTS `item`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     DEFAULT NULL COMMENT '名称，最大长度 100，可选',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '简介/内容，最大长度 500，可选',
    `type`        TINYINT UNSIGNED DEFAULT NULL COMMENT '类型，值范围为 1-127，可选',
    `status`      TINYINT UNSIGNED DEFAULT NULL COMMENT '状态，0 或 1，可选',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `sort_order`  INT              DEFAULT NULL COMMENT '排序字段，最大长度 50',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT 'item' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '课程ID' PRIMARY KEY,
    `name`        VARCHAR(100) NOT NULL COMMENT '课程名称',
    `credit`      DECIMAL(3, 1) DEFAULT 0.0 COMMENT '学分',
    `teacher_id`  BIGINT UNSIGNED NOT NULL COMMENT '教师ID',
    `classroom`   VARCHAR(50) DEFAULT NULL COMMENT '教室',
    `max_students` INT DEFAULT 100 COMMENT '最大学生数',
    `current_students` INT DEFAULT 0 COMMENT '当前学生数',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-未开始，1-进行中，2-已结束',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '课程表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '学生ID' PRIMARY KEY,
    `student_no`  VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    `name`        VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED DEFAULT 1 COMMENT '性别：0-女，1-男',
    `grade`       VARCHAR(20) DEFAULT NULL COMMENT '年级',
    `major`       VARCHAR(50) DEFAULT NULL COMMENT '专业',
    `class_name`  VARCHAR(50) DEFAULT NULL COMMENT '班级',
    `phone`       VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-离校，1-在校',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '学生表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '教师ID' PRIMARY KEY,
    `teacher_no`  VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
    `name`        VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED DEFAULT 1 COMMENT '性别：0-女，1-男',
    `title`       VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `department`  VARCHAR(100) DEFAULT NULL COMMENT '所属部门',
    `phone`       VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-离职，1-在职',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '教师表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course_student`;
CREATE TABLE IF NOT EXISTS `course_student`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `enroll_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_course_student` (`course_id`, `student_id`)
) COMMENT '课程学生关联表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '考勤ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `attendance_date` DATE NOT NULL COMMENT '考勤日期',
    `status`      TINYINT UNSIGNED NOT NULL COMMENT '考勤状态：0-缺勤，1-出勤，2-请假',
    `remark`      VARCHAR(200) DEFAULT NULL COMMENT '备注',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_course_student_date` (`course_id`, `student_id`, `attendance_date`)
) COMMENT '考勤表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE IF NOT EXISTS `leave_request`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '请假ID' PRIMARY KEY,
    `student_id`  BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `start_date`  DATE NOT NULL COMMENT '开始日期',
    `end_date`    DATE NOT NULL COMMENT '结束日期',
    `reason`      VARCHAR(500) NOT NULL COMMENT '请假原因',
    `status`      TINYINT UNSIGNED DEFAULT 0 COMMENT '审批状态：0-待审批，1-已通过，2-已拒绝',
    `approver_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(200) DEFAULT NULL COMMENT '审批备注',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '请假申请表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '评论ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `user_type`   TINYINT UNSIGNED NOT NULL COMMENT '用户类型：0-学生，1-教师',
    `content`     TEXT NOT NULL COMMENT '评论内容',
    `parent_id`   BIGINT UNSIGNED DEFAULT NULL COMMENT '父评论ID',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '评论表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `homework`;
CREATE TABLE IF NOT EXISTS `homework`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '作业ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200) NOT NULL COMMENT '作业标题',
    `description` TEXT DEFAULT NULL COMMENT '作业描述',
    `deadline`    DATETIME NOT NULL COMMENT '截止时间',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '作业表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `homework_submission`;
CREATE TABLE IF NOT EXISTS `homework_submission`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '提交ID' PRIMARY KEY,
    `homework_id` BIGINT UNSIGNED NOT NULL COMMENT '作业ID',
    `student_id`  BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `content`     TEXT DEFAULT NULL COMMENT '作业内容',
    `file_url`    VARCHAR(500) DEFAULT NULL COMMENT '文件地址',
    `score`       DECIMAL(5, 2) DEFAULT NULL COMMENT '得分',
    `feedback`    VARCHAR(500) DEFAULT NULL COMMENT '反馈',
    `submit_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_homework_student` (`homework_id`, `student_id`)
) COMMENT '作业提交表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `material`;
CREATE TABLE IF NOT EXISTS `material`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '资料ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200) NOT NULL COMMENT '资料标题',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '资料描述',
    `file_url`    VARCHAR(500) NOT NULL COMMENT '文件地址',
    `file_size`   BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
    `file_type`   VARCHAR(50) DEFAULT NULL COMMENT '文件类型',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '资料表' COLLATE = utf8mb4_unicode_ci;

