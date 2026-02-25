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
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '直播间ID' PRIMARY KEY,
    `room_name`       VARCHAR(200)     NOT NULL COMMENT '直播间名称',
    `room_cover`      VARCHAR(500)     DEFAULT NULL COMMENT '直播间封面图片',
    `anchor_name`     VARCHAR(100)     DEFAULT NULL COMMENT '主播名称',
    `anchor_avatar`   VARCHAR(500)     DEFAULT NULL COMMENT '主播头像',
    `room_status`     TINYINT UNSIGNED DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    `scheduled_time`  DATETIME         DEFAULT NULL COMMENT '预计开播时间',
    `start_time`      DATETIME         DEFAULT NULL COMMENT '实际开播时间',
    `end_time`        DATETIME         DEFAULT NULL COMMENT '结束时间',
    `current_product_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解的商品ID',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1',
    INDEX `idx_room_status` (`room_status`),
    INDEX `idx_creator_id` (`creator_id`)
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_product`;
CREATE TABLE IF NOT EXISTS `live_product`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `product_id`      BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `product_name`    VARCHAR(200)     DEFAULT NULL COMMENT '商品名称',
    `product_image`   VARCHAR(500)     DEFAULT NULL COMMENT '商品图片',
    `original_price`  DECIMAL(10, 2)   DEFAULT NULL COMMENT '商品原价',
    `live_price`      DECIMAL(10, 2)   DEFAULT NULL COMMENT '直播价',
    `stock`           INT              DEFAULT 0 COMMENT '库存',
    `sold_count`      INT              DEFAULT 0 COMMENT '已售数量',
    `sort_order`      INT              DEFAULT 0 COMMENT '排序号',
    `is_explaining`   TINYINT UNSIGNED DEFAULT 0 COMMENT '是否正在讲解：0-否，1-是',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_product_id` (`product_id`)
) COMMENT '直播商品关联表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_product_sell_point`;
CREATE TABLE IF NOT EXISTS `live_product_sell_point`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    `live_product_id` BIGINT UNSIGNED  NOT NULL COMMENT '直播商品ID',
    `point_content`   VARCHAR(500)     NOT NULL COMMENT '卖点内容',
    `sort_order`      INT              DEFAULT 0 COMMENT '排序号',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX `idx_live_product_id` (`live_product_id`)
) COMMENT '商品卖点表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_statistics`;
CREATE TABLE IF NOT EXISTS `live_statistics`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `total_order_num` INT              DEFAULT 0 COMMENT '订单数',
    `total_sales`     DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '销售额',
    `total_viewer`    INT              DEFAULT 0 COMMENT '累计观看人数',
    `peak_viewer`     INT              DEFAULT 0 COMMENT '峰值在线人数',
    `avg_online_time` INT              DEFAULT 0 COMMENT '平均在线时长(秒)',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    UNIQUE INDEX `uk_room_id` (`room_id`)
) COMMENT '直播间统计数据表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_audience_retention`;
CREATE TABLE IF NOT EXISTS `live_audience_retention`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `minute_mark`     INT              NOT NULL COMMENT '时间节点(直播开始后第N分钟)',
    `online_count`    INT              DEFAULT 0 COMMENT '在线人数',
    `new_viewer`      INT              DEFAULT 0 COMMENT '新增观众',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_minute_mark` (`minute_mark`)
) COMMENT '观众留存记录表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_product_rank`;
CREATE TABLE IF NOT EXISTS `live_product_rank`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `product_id`      BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `product_name`    VARCHAR(200)     DEFAULT NULL COMMENT '商品名称',
    `sold_count`      INT              DEFAULT 0 COMMENT '销售数量',
    `sales_amount`    DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '销售金额',
    `click_count`     INT              DEFAULT 0 COMMENT '点击次数',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段',
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_sold_count` (`sold_count` DESC)
) COMMENT '商品排行榜表' COLLATE = utf8mb4_unicode_ci;

