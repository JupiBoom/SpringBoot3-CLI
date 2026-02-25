USE `example`;

-- 直播间表
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '直播间ID' PRIMARY KEY,
    `title`           VARCHAR(200)     NOT NULL COMMENT '直播间标题',
    `cover_image`     VARCHAR(500)     DEFAULT NULL COMMENT '直播间封面图URL',
    `streamer_id`     BIGINT UNSIGNED  NOT NULL COMMENT '主播ID',
    `streamer_name`   VARCHAR(100)     DEFAULT NULL COMMENT '主播名称',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束，3-已禁播',
    `start_time`      DATETIME         DEFAULT NULL COMMENT '直播开始时间',
    `end_time`        DATETIME         DEFAULT NULL COMMENT '直播结束时间',
    `room_desc`       VARCHAR(1000)    DEFAULT NULL COMMENT '直播间简介',
    `current_item_id` BIGINT UNSIGNED  DEFAULT NULL COMMENT '当前讲解商品ID',
    `viewer_count`    INT UNSIGNED     DEFAULT 0 COMMENT '当前观众人数',
    `total_viewers`   INT UNSIGNED     DEFAULT 0 COMMENT '累计观看人数',
    `like_count`      INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `order_count`     INT UNSIGNED     DEFAULT 0 COMMENT '订单数',
    `sales_amount`    DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '销售额',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

-- 直播间商品关联表
DROP TABLE IF EXISTS `live_room_item`;
CREATE TABLE IF NOT EXISTS `live_room_item`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `room_id`      BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `item_id`      BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `item_name`    VARCHAR(200)     DEFAULT NULL COMMENT '商品名称（冗余）',
    `item_price`   DECIMAL(10, 2)   DEFAULT NULL COMMENT '商品售价（冗余）',
    `item_image`   VARCHAR(500)     DEFAULT NULL COMMENT '商品主图（冗余）',
    `selling_points` VARCHAR(1000)  DEFAULT NULL COMMENT '商品卖点，JSON数组格式',
    `sort_order`   INT              DEFAULT 0 COMMENT '排序顺序',
    `status`       TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `sales_count`  INT UNSIGNED     DEFAULT 0 COMMENT '直播期间销量',
    `sales_amount` DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '直播期间销售额',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY `uk_room_item` (`room_id`, `item_id`)
) COMMENT '直播间商品关联表' COLLATE = utf8mb4_unicode_ci;

-- 直播间订单表
DROP TABLE IF EXISTS `live_order`;
CREATE TABLE IF NOT EXISTS `live_order`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '订单ID' PRIMARY KEY,
    `order_no`        VARCHAR(64)      NOT NULL COMMENT '订单编号',
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `item_id`         BIGINT UNSIGNED  NOT NULL COMMENT '商品ID',
    `user_id`         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
    `quantity`        INT UNSIGNED     DEFAULT 1 COMMENT '购买数量',
    `unit_price`      DECIMAL(10, 2)   NOT NULL COMMENT '单价',
    `total_amount`    DECIMAL(12, 2)   NOT NULL COMMENT '订单总金额',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
    `pay_time`        DATETIME         DEFAULT NULL COMMENT '支付时间',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT 0 COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_item_id` (`item_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) COMMENT '直播间订单表' COLLATE = utf8mb4_unicode_ci;

-- 直播间观众数据表（用于记录观众进出和留存分析）
DROP TABLE IF EXISTS `live_viewer_stats`;
CREATE TABLE IF NOT EXISTS `live_viewer_stats`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `room_id`     BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `user_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '用户ID（未登录为NULL）',
    `enter_time`  DATETIME         NOT NULL COMMENT '进入时间',
    `leave_time`  DATETIME         DEFAULT NULL COMMENT '离开时间',
    `stay_duration` INT UNSIGNED   DEFAULT 0 COMMENT '停留时长（秒）',
    `is_login`    TINYINT UNSIGNED DEFAULT 0 COMMENT '是否登录用户：0-否，1-是',
    `ip_address`  VARCHAR(64)      DEFAULT NULL COMMENT 'IP地址',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    KEY `idx_room_id` (`room_id`),
    KEY `idx_enter_time` (`enter_time`)
) COMMENT '直播间观众数据统计表' COLLATE = utf8mb4_unicode_ci;

-- 直播间实时数据快照表（用于记录每分钟的数据变化）
DROP TABLE IF EXISTS `live_room_snapshot`;
CREATE TABLE IF NOT EXISTS `live_room_snapshot`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `room_id`         BIGINT UNSIGNED  NOT NULL COMMENT '直播间ID',
    `snapshot_time`   DATETIME         NOT NULL COMMENT '快照时间',
    `viewer_count`    INT UNSIGNED     DEFAULT 0 COMMENT '当前观众数',
    `total_viewers`   INT UNSIGNED     DEFAULT 0 COMMENT '累计观看人数',
    `like_count`      INT UNSIGNED     DEFAULT 0 COMMENT '点赞数',
    `order_count`     INT UNSIGNED     DEFAULT 0 COMMENT '订单数',
    `sales_amount`    DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '销售额',
    `new_orders`      INT UNSIGNED     DEFAULT 0 COMMENT '新增订单数',
    `new_sales`       DECIMAL(12, 2)   DEFAULT 0.00 COMMENT '新增销售额',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    KEY `idx_room_id_time` (`room_id`, `snapshot_time`)
) COMMENT '直播间数据快照表' COLLATE = utf8mb4_unicode_ci;
