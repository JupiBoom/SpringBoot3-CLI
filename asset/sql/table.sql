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

DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '直播间ID' PRIMARY KEY,
    `room_name`         VARCHAR(200)     NOT NULL COMMENT '直播间名称',
    `room_description`  VARCHAR(1000)    DEFAULT NULL COMMENT '直播间描述',
    `cover_image`       VARCHAR(500)     DEFAULT NULL COMMENT '封面图片URL',
    `anchor_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '主播ID',
    `anchor_name`       VARCHAR(100)     DEFAULT NULL COMMENT '主播名称',
    `status`            TINYINT UNSIGNED DEFAULT 0 COMMENT '直播状态：0-未开播，1-直播中，2-已结束，3-暂停',
    `planned_start_time` DATETIME        DEFAULT NULL COMMENT '计划开播时间',
    `actual_start_time`  DATETIME        DEFAULT NULL COMMENT '实际开播时间',
    `end_time`          DATETIME         DEFAULT NULL COMMENT '结束时间',
    `current_product_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解的商品ID',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_anchor_id` (`anchor_id`),
    INDEX `idx_status` (`status`)
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_product`;
CREATE TABLE IF NOT EXISTS `live_product`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '关联ID' PRIMARY KEY,
    `room_id`           BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `product_id`        BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `product_name`      VARCHAR(200)     DEFAULT NULL COMMENT '商品名称',
    `product_image`     VARCHAR(500)     DEFAULT NULL COMMENT '商品图片URL',
    `original_price`    DECIMAL(10,2)    DEFAULT NULL COMMENT '原价',
    `live_price`        DECIMAL(10,2)    DEFAULT NULL COMMENT '直播价',
    `selling_points`    VARCHAR(1000)    DEFAULT NULL COMMENT '商品卖点，JSON数组格式',
    `sort_order`        INT              DEFAULT 0 COMMENT '展示排序',
    `is_explaining`     TINYINT UNSIGNED DEFAULT 0 COMMENT '是否正在讲解：0-否，1-是',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_explaining` (`is_explaining`)
) COMMENT '直播间商品关联表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_room_stats`;
CREATE TABLE IF NOT EXISTS `live_room_stats`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '统计ID' PRIMARY KEY,
    `room_id`           BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `total_viewers`     BIGINT UNSIGNED  DEFAULT 0 COMMENT '累计观看人数',
    `peak_viewers`      INT UNSIGNED     DEFAULT 0 COMMENT '峰值在线人数',
    `current_viewers`   INT UNSIGNED     DEFAULT 0 COMMENT '当前在线人数',
    `total_orders`      INT UNSIGNED     DEFAULT 0 COMMENT '总订单数',
    `total_sales`       DECIMAL(12,2)    DEFAULT 0.00 COMMENT '总销售额',
    `total_likes`       BIGINT UNSIGNED  DEFAULT 0 COMMENT '总点赞数',
    `total_comments`    INT UNSIGNED     DEFAULT 0 COMMENT '总评论数',
    `total_shares`      INT UNSIGNED     DEFAULT 0 COMMENT '总分享数',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    UNIQUE INDEX `uk_room_id` (`room_id`)
) COMMENT '直播间统计表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_product_stats`;
CREATE TABLE IF NOT EXISTS `live_product_stats`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '统计ID' PRIMARY KEY,
    `room_id`           BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `product_id`        BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `product_name`      VARCHAR(200)     DEFAULT NULL COMMENT '商品名称',
    `click_count`       INT UNSIGNED     DEFAULT 0 COMMENT '点击次数',
    `order_count`       INT UNSIGNED     DEFAULT 0 COMMENT '订单数',
    `sales_amount`      DECIMAL(12,2)    DEFAULT 0.00 COMMENT '销售额',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_order_count` (`order_count` DESC)
) COMMENT '直播商品统计表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_audience_retention`;
CREATE TABLE IF NOT EXISTS `live_audience_retention`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '记录ID' PRIMARY KEY,
    `room_id`           BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `record_time`       DATETIME         DEFAULT NULL COMMENT '记录时间点',
    `minute_offset`     INT UNSIGNED     DEFAULT 0 COMMENT '直播开始后的分钟数',
    `viewer_count`      INT UNSIGNED     DEFAULT 0 COMMENT '当前观众数',
    `creator_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`        BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`       DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`           TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_record_time` (`record_time`)
) COMMENT '观众留存记录表' COLLATE = utf8mb4_unicode_ci;

