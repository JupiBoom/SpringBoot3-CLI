USE `example`;

-- 设备类型表
DROP TABLE IF EXISTS `device_type`;
CREATE TABLE IF NOT EXISTS `device_type`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     NOT NULL COMMENT '设备类型名称',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '设备类型描述',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '设备类型表' COLLATE = utf8mb4_unicode_ci;

-- 故障类型表
DROP TABLE IF EXISTS `fault_type`;
CREATE TABLE IF NOT EXISTS `fault_type`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`        VARCHAR(100)     NOT NULL COMMENT '故障类型名称',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '故障类型描述',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '故障类型表' COLLATE = utf8mb4_unicode_ci;

-- 设备报修工单表
DROP TABLE IF EXISTS `repair_order`;
CREATE TABLE IF NOT EXISTS `repair_order`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_no`        VARCHAR(50)      NOT NULL COMMENT '工单号，唯一标识',
    `device_type_id`  BIGINT UNSIGNED  NOT NULL COMMENT '设备类型ID，关联设备类型表',
    `fault_type_id`   BIGINT UNSIGNED  NOT NULL COMMENT '故障类型ID，关联故障类型表',
    `device_location` VARCHAR(200)     NOT NULL COMMENT '设备位置',
    `fault_description` TEXT            NOT NULL COMMENT '故障描述',
    `fault_images`    TEXT             DEFAULT NULL COMMENT '故障照片，JSON格式存储多个图片URL',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '报修状态：0-待处理，1-维修中，2-已完成',
    `priority`        TINYINT UNSIGNED DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高，4-紧急',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '报修用户ID',
    `assigned_to`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '分配给的维修人员ID',
    `assigned_time`   DATETIME         DEFAULT NULL COMMENT '分配时间',
    `accepted_time`   DATETIME         DEFAULT NULL COMMENT '接单时间',
    `completed_time`  DATETIME         DEFAULT NULL COMMENT '完成时间',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '设备报修工单表' COLLATE = utf8mb4_unicode_ci;

-- 维修记录表
DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE IF NOT EXISTS `repair_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_id`        BIGINT UNSIGNED  NOT NULL COMMENT '工单ID，关联报修工单表',
    `repair_content`  TEXT             NOT NULL COMMENT '维修内容记录',
    `repair_images`   TEXT             DEFAULT NULL COMMENT '维修过程照片，JSON格式存储多个图片URL',
    `record_time`     DATETIME         NOT NULL COMMENT '记录时间',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '维修记录表' COLLATE = utf8mb4_unicode_ci;

-- 评价表
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE IF NOT EXISTS `evaluation`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED  NOT NULL COMMENT '工单ID，关联报修工单表',
    `rating`      TINYINT UNSIGNED  NOT NULL COMMENT '评分，1-5星',
    `content`     TEXT             DEFAULT NULL COMMENT '评价内容',
    `user_id`     BIGINT UNSIGNED  NOT NULL COMMENT '评价用户ID',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '评价表' COLLATE = utf8mb4_unicode_ci;

-- 通知表
DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `title`       VARCHAR(200)     NOT NULL COMMENT '通知标题',
    `content`     TEXT             NOT NULL COMMENT '通知内容',
    `type`        TINYINT UNSIGNED DEFAULT 0 COMMENT '通知类型：0-系统通知，1-工单通知，2-评价通知',
    `receiver_id` BIGINT UNSIGNED  NOT NULL COMMENT '接收者ID',
    `is_read`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '通知表' COLLATE = utf8mb4_unicode_ci;

-- 初始化设备类型数据
INSERT INTO `device_type` (`name`, `description`, `creator_id`, `create_time`) VALUES
('电脑', '台式机、笔记本电脑等办公设备', 1, NOW()),
('打印机', '各类打印设备', 1, NOW()),
('空调', '空调设备', 1, NOW()),
('投影仪', '会议投影设备', 1, NOW()),
('网络设备', '路由器、交换机等网络设备', 1, NOW());

-- 初始化故障类型数据
INSERT INTO `fault_type` (`name`, `description`, `creator_id`, `create_time`) VALUES
('无法开机', '设备无法正常启动', 1, NOW()),
('运行缓慢', '设备运行速度慢', 1, NOW()),
('异常噪音', '设备运行时有异常噪音', 1, NOW()),
('连接问题', '设备无法连接网络或其他设备', 1, NOW()),
('功能故障', '设备部分功能无法正常使用', 1, NOW()),
('物理损坏', '设备外观或部件物理损坏', 1, NOW());