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

-- 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '课程ID' PRIMARY KEY,
    `course_name` VARCHAR(100)     NOT NULL COMMENT '课程名称',
    `credit`      DECIMAL(3,1)      NOT NULL DEFAULT 0.0 COMMENT '学分',
    `teacher_id`  BIGINT UNSIGNED  NOT NULL COMMENT '教师ID',
    `classroom`   VARCHAR(50)      DEFAULT NULL COMMENT '教室',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '课程描述',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '课程表' COLLATE = utf8mb4_unicode_ci;

-- 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '学生ID' PRIMARY KEY,
    `student_no`  VARCHAR(20)       NOT NULL UNIQUE COMMENT '学号',
    `name`        VARCHAR(50)       NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED  DEFAULT NULL COMMENT '性别：0-女，1-男',
    `grade`       VARCHAR(20)       DEFAULT NULL COMMENT '年级',
    `major`       VARCHAR(50)       DEFAULT NULL COMMENT '专业',
    `class_name`  VARCHAR(50)       DEFAULT NULL COMMENT '班级',
    `phone`       VARCHAR(20)       DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(100)      DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '学生表' COLLATE = utf8mb4_unicode_ci;

-- 教师表
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '教师ID' PRIMARY KEY,
    `teacher_no`  VARCHAR(20)       NOT NULL UNIQUE COMMENT '教师编号',
    `name`        VARCHAR(50)       NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED  DEFAULT NULL COMMENT '性别：0-女，1-男',
    `department`  VARCHAR(50)       DEFAULT NULL COMMENT '所属院系',
    `title`       VARCHAR(50)       DEFAULT NULL COMMENT '职称',
    `phone`       VARCHAR(20)       DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(100)      DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '教师表' COLLATE = utf8mb4_unicode_ci;

-- 选课表
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE IF NOT EXISTS `course_selection`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '选课ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `select_time` DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-退选，1-已选',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除',
    UNIQUE KEY `uk_course_student` (`course_id`, `student_id`)
) COMMENT '选课表' COLLATE = utf8mb4_unicode_ci;

-- 考勤表
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '考勤ID' PRIMARY KEY,
    `course_id`     BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`    BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `attendance_date` DATE            NOT NULL COMMENT '考勤日期',
    `status`        TINYINT UNSIGNED  NOT NULL COMMENT '考勤状态：0-缺勤，1-出勤，2-请假',
    `remark`        VARCHAR(200)     DEFAULT NULL COMMENT '备注',
    `creator_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`    TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除',
    UNIQUE KEY `uk_course_student_date` (`course_id`, `student_id`, `attendance_date`)
) COMMENT '考勤表' COLLATE = utf8mb4_unicode_ci;

-- 请假表
DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE IF NOT EXISTS `leave_request`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '请假ID' PRIMARY KEY,
    `student_id`    BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `course_id`     BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `start_date`    DATE             NOT NULL COMMENT '请假开始日期',
    `end_date`      DATE             NOT NULL COMMENT '请假结束日期',
    `reason`        VARCHAR(500)     NOT NULL COMMENT '请假原因',
    `status`        TINYINT UNSIGNED  DEFAULT 0 COMMENT '审批状态：0-待审批，1-已批准，2-已拒绝',
    `approver_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '审批人ID',
    `approve_time`  DATETIME         DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(200)    DEFAULT NULL COMMENT '审批备注',
    `creator_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`    TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '请假表' COLLATE = utf8mb4_unicode_ci;

-- 课程讨论表
DROP TABLE IF EXISTS `course_discussion`;
CREATE TABLE IF NOT EXISTS `course_discussion`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '讨论ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '讨论标题',
    `content`     TEXT             NOT NULL COMMENT '讨论内容',
    `parent_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '父讨论ID，用于回复',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '课程讨论表' COLLATE = utf8mb4_unicode_ci;

-- 作业表
DROP TABLE IF EXISTS `assignment`;
CREATE TABLE IF NOT EXISTS `assignment`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '作业ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '作业标题',
    `description` TEXT             NOT NULL COMMENT '作业描述',
    `due_date`    DATETIME         NOT NULL COMMENT '截止日期',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '作业表' COLLATE = utf8mb4_unicode_ci;

-- 作业提交表
DROP TABLE IF EXISTS `assignment_submission`;
CREATE TABLE IF NOT EXISTS `assignment_submission`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '提交ID' PRIMARY KEY,
    `assignment_id` BIGINT UNSIGNED  NOT NULL COMMENT '作业ID',
    `student_id`    BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `content`       TEXT             DEFAULT NULL COMMENT '提交内容',
    `file_path`     VARCHAR(500)     DEFAULT NULL COMMENT '附件路径',
    `submit_time`   DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `score`         DECIMAL(5,2)     DEFAULT NULL COMMENT '得分',
    `feedback`      VARCHAR(500)     DEFAULT NULL COMMENT '反馈',
    `status`        TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-未提交，1-已提交，2-已批改',
    `creator_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`    BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time`   DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`    TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除',
    UNIQUE KEY `uk_assignment_student` (`assignment_id`, `student_id`)
) COMMENT '作业提交表' COLLATE = utf8mb4_unicode_ci;

-- 课程资料表
DROP TABLE IF EXISTS `course_material`;
CREATE TABLE IF NOT EXISTS `course_material`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '资料ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`       VARCHAR(200)     NOT NULL COMMENT '资料标题',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '资料描述',
    `file_path`   VARCHAR(500)     NOT NULL COMMENT '文件路径',
    `file_type`   VARCHAR(50)      DEFAULT NULL COMMENT '文件类型',
    `file_size`   BIGINT           DEFAULT NULL COMMENT '文件大小(字节)',
    `status`      TINYINT UNSIGNED  DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    `creator_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED   DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`     TINYINT UNSIGNED  DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED  DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '课程资料表' COLLATE = utf8mb4_unicode_ci;

