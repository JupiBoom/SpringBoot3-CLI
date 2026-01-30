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

-- 直播间表
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `name`         VARCHAR(100)     NOT NULL COMMENT '直播间名称',
    `description`  VARCHAR(500)     DEFAULT NULL COMMENT '直播间描述',
    `cover_image`  VARCHAR(255)     DEFAULT NULL COMMENT '封面图片',
    `status`       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    `current_item_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解的商品ID',
    `creator_id`   BIGINT UNSIGNED  NOT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `start_time`   DATETIME         DEFAULT NULL COMMENT '开始直播时间',
    `end_time`     DATETIME         DEFAULT NULL COMMENT '结束直播时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0-未删除，1-已删除'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

-- 直播间商品表
DROP TABLE IF EXISTS `live_room_item`;
CREATE TABLE IF NOT EXISTS `live_room_item`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `item_id`      BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `selling_points` VARCHAR(1000) DEFAULT NULL COMMENT '商品卖点',
    `sort_order`   INT             NOT NULL DEFAULT 0 COMMENT '排序顺序',
    `status`       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_live_item` (`live_room_id`, `item_id`)
) COMMENT '直播间商品表' COLLATE = utf8mb4_unicode_ci;

-- 直播间讲解记录表
DROP TABLE IF EXISTS `live_room_explanation`;
CREATE TABLE IF NOT EXISTS `live_room_explanation`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `item_id`      BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `start_time`   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始讲解时间',
    `end_time`     DATETIME         DEFAULT NULL COMMENT '结束讲解时间',
    `create_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '直播间讲解记录表' COLLATE = utf8mb4_unicode_ci;

-- 直播间数据表
DROP TABLE IF EXISTS `live_room_data`;
CREATE TABLE IF NOT EXISTS `live_room_data`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `audience_count` INT             NOT NULL DEFAULT 0 COMMENT '当前观众人数',
    `peak_audience` INT             NOT NULL DEFAULT 0 COMMENT '峰值观众人数',
    `total_orders`  INT             NOT NULL DEFAULT 0 COMMENT '总订单数',
    `total_sales`   DECIMAL(10,2)   NOT NULL DEFAULT 0.00 COMMENT '总销售额',
    `update_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '直播间数据表' COLLATE = utf8mb4_unicode_ci;

-- 直播间销售数据表
DROP TABLE IF EXISTS `live_room_sales`;
CREATE TABLE IF NOT EXISTS `live_room_sales`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `item_id`      BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `order_count`  INT             NOT NULL DEFAULT 0 COMMENT '订单数',
    `sales_amount` DECIMAL(10,2)   NOT NULL DEFAULT 0.00 COMMENT '销售额',
    `create_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '直播间销售数据表' COLLATE = utf8mb4_unicode_ci;

-- 直播间观众数据表
DROP TABLE IF EXISTS `live_room_audience`;
CREATE TABLE IF NOT EXISTS `live_room_audience`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `user_id`      BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID',
    `enter_time`   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '进入时间',
    `leave_time`   DATETIME         DEFAULT NULL COMMENT '离开时间',
    `stay_duration` INT             DEFAULT NULL COMMENT '停留时长(秒)',
    `is_purchase`  TINYINT UNSIGNED DEFAULT 0 COMMENT '是否购买：0-否，1-是'
) COMMENT '直播间观众数据表' COLLATE = utf8mb4_unicode_ci;

-- 直播间商品排行榜
DROP TABLE IF EXISTS `live_room_item_rank`;
CREATE TABLE IF NOT EXISTS `live_room_item_rank`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `item_id`      BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `sales_amount` DECIMAL(10,2)   NOT NULL DEFAULT 0.00 COMMENT '销售额',
    `order_count`  INT             NOT NULL DEFAULT 0 COMMENT '订单数',
    `rank`         INT             NOT NULL DEFAULT 0 COMMENT '排名',
    `update_time`  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_live_item_rank` (`live_room_id`, `item_id`)
) COMMENT '直播间商品排行榜' COLLATE = utf8mb4_unicode_ci;

