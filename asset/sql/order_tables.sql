USE `example`;

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE IF NOT EXISTS `order`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '订单ID' PRIMARY KEY,
    `order_no`    VARCHAR(50)     DEFAULT NULL COMMENT '订单号',
    `user_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
    `item_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '商品ID',
    `item_name`   VARCHAR(100)     DEFAULT NULL COMMENT '商品名称',
    `quantity`    INT             DEFAULT NULL COMMENT '商品数量',
    `total_amount` DECIMAL(10,2)   DEFAULT NULL COMMENT '订单金额',
    `status`      TINYINT UNSIGNED DEFAULT NULL COMMENT '订单状态',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '订单' COLLATE = utf8mb4_unicode_ci;

-- 通知记录表
DROP TABLE IF EXISTS `notification_record`;
CREATE TABLE IF NOT EXISTS `notification_record`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '记录ID' PRIMARY KEY,
    `order_id`    BIGINT UNSIGNED DEFAULT NULL COMMENT '订单ID',
    `user_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
    `type`        VARCHAR(50)     DEFAULT NULL COMMENT '通知类型',
    `channel`     VARCHAR(50)     DEFAULT NULL COMMENT '通知渠道',
    `content`     TEXT            DEFAULT NULL COMMENT '通知内容',
    `status`      TINYINT UNSIGNED DEFAULT NULL COMMENT '发送状态',
    `error_msg`   TEXT            DEFAULT NULL COMMENT '错误信息',
    `retry_count` INT             DEFAULT NULL COMMENT '重试次数',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '通知记录' COLLATE = utf8mb4_unicode_ci;

-- 用户通知偏好设置表
DROP TABLE IF EXISTS `user_notification_preference`;
CREATE TABLE IF NOT EXISTS `user_notification_preference`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
    `enable_sms`  TINYINT UNSIGNED DEFAULT NULL COMMENT '是否启用短信通知',
    `enable_wechat` TINYINT UNSIGNED DEFAULT NULL COMMENT '是否启用微信通知',
    `enable_email` TINYINT UNSIGNED DEFAULT NULL COMMENT '是否启用邮件通知',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '用户通知偏好设置' COLLATE = utf8mb4_unicode_ci;