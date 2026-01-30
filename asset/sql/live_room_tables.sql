USE `example`;

-- 直播间表
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `title`       VARCHAR(200)     NOT NULL COMMENT '直播间标题',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '直播间描述',
    `cover_image` VARCHAR(500)     DEFAULT NULL COMMENT '直播间封面图URL',
    `stream_url`  VARCHAR(500)     DEFAULT NULL COMMENT '直播流地址',
    `status`      TINYINT UNSIGNED DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    `host_id`     BIGINT UNSIGNED NOT NULL COMMENT '主播ID',
    `start_time`  DATETIME         DEFAULT NULL COMMENT '直播开始时间',
    `end_time`    DATETIME         DEFAULT NULL COMMENT '直播结束时间',
    `current_product_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解的商品ID',
    `viewer_count` INT UNSIGNED DEFAULT 0 COMMENT '当前观众人数',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `sort_order`  INT              DEFAULT NULL COMMENT '排序字段，最大长度 50',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

-- 直播间商品关联表
DROP TABLE IF EXISTS `live_room_product`;
CREATE TABLE IF NOT EXISTS `live_room_product`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `product_id`   BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片URL',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `selling_points` TEXT DEFAULT NULL COMMENT '商品卖点说明',
    `display_order` INT DEFAULT 0 COMMENT '展示顺序',
    `is_current` TINYINT UNSIGNED DEFAULT 0 COMMENT '是否当前讲解商品：0-否，1-是',
    `sales_count` INT UNSIGNED DEFAULT 0 COMMENT '销售数量',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_product_id` (`product_id`)
) COMMENT '直播间商品关联表' COLLATE = utf8mb4_unicode_ci;

-- 直播间数据统计表
DROP TABLE IF EXISTS `live_room_stats`;
CREATE TABLE IF NOT EXISTS `live_room_stats`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `stats_date` DATE NOT NULL COMMENT '统计日期',
    `total_viewers` INT UNSIGNED DEFAULT 0 COMMENT '总观众人数',
    `peak_viewers` INT UNSIGNED DEFAULT 0 COMMENT '峰值观众人数',
    `avg_view_time` INT DEFAULT 0 COMMENT '平均观看时长（秒）',
    `total_orders` INT UNSIGNED DEFAULT 0 COMMENT '总订单数',
    `total_sales` DECIMAL(12,2) DEFAULT 0.00 COMMENT '总销售额',
    `conversion_rate` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '转化率（订单数/观众数）',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    UNIQUE KEY `uk_room_date` (`live_room_id`, `stats_date`),
    INDEX `idx_stats_date` (`stats_date`)
) COMMENT '直播间数据统计表' COLLATE = utf8mb4_unicode_ci;

-- 直播间观众记录表
DROP TABLE IF EXISTS `live_room_viewer`;
CREATE TABLE IF NOT EXISTS `live_room_viewer`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `user_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID，可为空（匿名用户）',
    `session_id`  VARCHAR(100) NOT NULL COMMENT '会话ID，用于识别匿名用户',
    `join_time`   DATETIME NOT NULL COMMENT '进入直播间时间',
    `leave_time`  DATETIME DEFAULT NULL COMMENT '离开直播间时间',
    `total_duration` INT DEFAULT 0 COMMENT '观看总时长（秒）',
    `is_online`   TINYINT UNSIGNED DEFAULT 1 COMMENT '是否在线：0-离线，1-在线',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_session_id` (`session_id`),
    INDEX `idx_join_time` (`join_time`)
) COMMENT '直播间观众记录表' COLLATE = utf8mb4_unicode_ci;

-- 直播间订单记录表
DROP TABLE IF EXISTS `live_room_order`;
CREATE TABLE IF NOT EXISTS `live_room_order`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `order_id`    BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    `product_id`  BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `quantity`    INT UNSIGNED NOT NULL COMMENT '购买数量',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `user_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '购买用户ID',
    `order_time`  DATETIME NOT NULL COMMENT '下单时间',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID，关联用户表，必须为正整数，可选',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间，可选',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID，关联用户表，必须为正整数，可选',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间，可选',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_order_time` (`order_time`)
) COMMENT '直播间订单记录表' COLLATE = utf8mb4_unicode_ci;