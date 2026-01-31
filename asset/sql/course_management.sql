USE `example`;

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '学生ID' PRIMARY KEY,
    `student_no`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '学号',
    `name`          VARCHAR(50)  NOT NULL COMMENT '姓名',
    `gender`        TINYINT UNSIGNED DEFAULT NULL COMMENT '性别：0-女，1-男',
    `phone`         VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`         VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `major`         VARCHAR(100)     DEFAULT NULL COMMENT '专业',
    `class_name`    VARCHAR(50)      DEFAULT NULL COMMENT '班级',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_student_no (`student_no`),
    INDEX idx_name (`name`)
) COMMENT '学生表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '教师ID' PRIMARY KEY,
    `teacher_no`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '工号',
    `name`          VARCHAR(50)  NOT NULL COMMENT '姓名',
    `gender`        TINYINT UNSIGNED DEFAULT NULL COMMENT '性别：0-女，1-男',
    `phone`         VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`         VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `department`  VARCHAR(100)     DEFAULT NULL COMMENT '部门/学院',
    `title`         VARCHAR(50)      DEFAULT NULL COMMENT '职称',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_teacher_no (`teacher_no`),
    INDEX idx_name (`name`)
) COMMENT '教师表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '课程ID' PRIMARY KEY,
    `course_code`   VARCHAR(50)  NOT NULL UNIQUE COMMENT '课程代码',
    `course_name`   VARCHAR(100) NOT NULL COMMENT '课程名称',
    `credits`         DECIMAL(3, 1)  DEFAULT NULL COMMENT '学分',
    `teacher_id`    BIGINT UNSIGNED DEFAULT NULL COMMENT '授课教师ID',
    `classroom`     VARCHAR(100)     DEFAULT NULL COMMENT '上课教室',
    `course_type`     TINYINT UNSIGNED DEFAULT NULL COMMENT '课程类型：1-必修课，2-选修课',
    `semester`       VARCHAR(50)      DEFAULT NULL COMMENT '学期',
    `week_day`      TINYINT UNSIGNED DEFAULT NULL COMMENT '星期几：1-7',
    `start_time`   TIME             DEFAULT NULL COMMENT '开始时间',
    `end_time`     TIME             DEFAULT NULL COMMENT '结束时间',
    `max_students` INT UNSIGNED    DEFAULT NULL COMMENT '最大选课人数',
    `description`  VARCHAR(500)     DEFAULT NULL COMMENT '课程简介',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-关闭，1-开启',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_course_code (`course_code`),
    INDEX idx_teacher_id (`teacher_id`),
    INDEX idx_semester (`semester`)
) COMMENT '课程表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course_student`;
CREATE TABLE IF NOT EXISTS `course_student`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '选课记录ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`   BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `score`          DECIMAL(5, 2)     DEFAULT NULL COMMENT '成绩',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '选课状态：0-退课，1-已选',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE KEY uk_course_student (`course_id`, `student_id`),
    INDEX idx_course_id (`course_id`),
    INDEX idx_student_id (`student_id`)
) COMMENT '学生选课表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '考勤记录ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`   BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `attend_date`   DATE             NOT NULL COMMENT '考勤日期',
    `status`        TINYINT UNSIGNED DEFAULT 0 COMMENT '考勤状态：0-正常，1-迟到，2-缺勤，3-请假',
    `sign_time`   DATETIME         DEFAULT NULL COMMENT '签到时间',
    `remark`        VARCHAR(200)     DEFAULT NULL COMMENT '备注',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE KEY uk_course_student_date (`course_id`, `student_id`, `attend_date`),
    INDEX idx_course_id (`course_id`),
    INDEX idx_student_id (`student_id`),
    INDEX idx_attend_date (`attend_date`)
) COMMENT '考勤记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `leave_application`;
CREATE TABLE IF NOT EXISTS `leave_application`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '请假申请ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`   BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `leave_type`    TINYINT UNSIGNED DEFAULT NULL COMMENT '请假类型：1-事假，2-病假，3-其他',
    `start_date`    DATE             NOT NULL COMMENT '开始日期',
    `end_date`      DATE             NOT NULL COMMENT '结束日期',
    `reason`        VARCHAR(500)     DEFAULT NULL COMMENT '请假原因',
    `proof_url`     VARCHAR(255)     DEFAULT NULL COMMENT '证明材料URL',
    `approve_status` TINYINT UNSIGNED DEFAULT 0 COMMENT '审批状态：0-待审批，1-已通过，2-已拒绝',
    `approver_id`   BIGINT UNSIGNED DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME         DEFAULT NULL COMMENT '审批时间',
    `approve_note` VARCHAR(200)     DEFAULT NULL COMMENT '审批意见',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_course_id (`course_id`),
    INDEX idx_student_id (`student_id`),
    INDEX idx_approve_status (`approve_status`)
) COMMENT '请假申请表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course_comment`;
CREATE TABLE IF NOT EXISTS `course_comment`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '评论ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `user_id`      BIGINT UNSIGNED NOT NULL COMMENT '评论用户ID',
    `user_type`     TINYINT UNSIGNED DEFAULT NULL COMMENT '用户类型：1-学生，2-教师',
    `parent_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '父评论ID',
    `content`       VARCHAR(1000)    NOT NULL COMMENT '评论内容',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_course_id (`course_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_parent_id (`parent_id`)
) COMMENT '课程评论表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `homework`;
CREATE TABLE IF NOT EXISTS `homework`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '作业ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `title`         VARCHAR(100) NOT NULL COMMENT '作业标题',
    `description`  VARCHAR(1000)    DEFAULT NULL COMMENT '作业描述',
    `attachment_url` VARCHAR(255)     DEFAULT NULL COMMENT '附件URL',
    `deadline`    DATETIME         DEFAULT NULL COMMENT '截止时间',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-关闭，1-开启',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_course_id (`course_id`)
) COMMENT '作业表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `homework_submission`;
CREATE TABLE IF NOT EXISTS `homework_submission`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '作业提交ID' PRIMARY KEY,
    `homework_id`   BIGINT UNSIGNED NOT NULL COMMENT '作业ID',
    `student_id`   BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `content`       VARCHAR(2000)    DEFAULT NULL COMMENT '提交内容',
    `attachment_url` VARCHAR(255)     DEFAULT NULL COMMENT '提交附件URL',
    `score`          DECIMAL(5, 2)     DEFAULT NULL COMMENT '得分',
    `feedback`      VARCHAR(500)     DEFAULT NULL COMMENT '教师评语',
    `submit_time` DATETIME         DEFAULT NULL COMMENT '提交时间',
    `status`        TINYINT UNSIGNED DEFAULT 0 COMMENT '提交状态：0-未提交，1-已提交，2-已批改',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '发布时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE KEY uk_homework_student (`homework_id`, `student_id`),
    INDEX idx_homework_id (`homework_id`),
    INDEX idx_student_id (`student_id`)
) COMMENT '作业提交表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `course_resource`;
CREATE TABLE IF NOT EXISTS `course_resource`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '资源ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `resource_name` VARCHAR(100) NOT NULL COMMENT '资源名称',
    `resource_type` TINYINT UNSIGNED DEFAULT NULL COMMENT '资源类型：1-课件，2-视频，3-文档，4-其他',
    `file_url`      VARCHAR(255)     DEFAULT NULL COMMENT '文件URL',
    `file_size`     BIGINT UNSIGNED DEFAULT NULL COMMENT '文件大小(字节)',
    `description`  VARCHAR(500)     DEFAULT NULL COMMENT '资源描述',
    `status`        TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `download_count` INT UNSIGNED DEFAULT 0 COMMENT '下载次数',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX idx_course_id (`course_id`)
) COMMENT '课程资源表' COLLATE = utf8mb4_unicode_ci;