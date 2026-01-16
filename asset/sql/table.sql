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
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `title`       VARCHAR(200)     NOT NULL COMMENT '直播间标题',
    `description` VARCHAR(500)     DEFAULT NULL COMMENT '直播间描述',
    `status`      TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    `cover_url`   VARCHAR(500)     DEFAULT NULL COMMENT '直播间封面URL',
    `stream_url`  VARCHAR(500)     DEFAULT NULL COMMENT '直播流URL',
    `anchor_id`   BIGINT UNSIGNED  NOT NULL COMMENT '主播ID',
    `start_time`  DATETIME         DEFAULT NULL COMMENT '开始时间',
    `end_time`    DATETIME         DEFAULT NULL COMMENT '结束时间',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_room_product`;
CREATE TABLE IF NOT EXISTS `live_room_product`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id` BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `product_id`   BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200)    NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500)    DEFAULT NULL COMMENT '商品图片URL',
    `product_price` DECIMAL(10,2)  NOT NULL COMMENT '商品价格',
    `selling_point` VARCHAR(500)   DEFAULT NULL COMMENT '商品卖点',
    `sort_order`   INT              DEFAULT 0 COMMENT '排序',
    `status`       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0-未上架，1-已上架，2-讲解中',
    `order_count`  INT UNSIGNED     DEFAULT 0 COMMENT '订单数',
    `sales_amount` DECIMAL(10,2)    DEFAULT 0.00 COMMENT '销售额',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间商品关联表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_room_data`;
CREATE TABLE IF NOT EXISTS `live_room_data`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`   BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `record_time`    DATETIME         NOT NULL COMMENT '记录时间',
    `viewer_count`   INT UNSIGNED     DEFAULT 0 COMMENT '当前观众人数',
    `total_orders`   INT UNSIGNED     DEFAULT 0 COMMENT '累计订单数',
    `total_sales`    DECIMAL(10,2)    DEFAULT 0.00 COMMENT '累计销售额',
    `new_viewers`    INT UNSIGNED     DEFAULT 0 COMMENT '新增观众数',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间数据统计表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_room_viewer`;
CREATE TABLE IF NOT EXISTS `live_room_viewer`
(
    `id`            BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`  BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `viewer_id`     BIGINT UNSIGNED NOT NULL COMMENT '观众ID',
    `enter_time`    DATETIME         NOT NULL COMMENT '进入时间',
    `leave_time`    DATETIME         DEFAULT NULL COMMENT '离开时间',
    `duration`      INT UNSIGNED     DEFAULT 0 COMMENT '观看时长（秒）',
    `creator_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`   DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`   DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`       TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP乐观锁版本',
    `is_deleted`    TINYINT UNSIGNED DEFAULT NULL COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '观众数据表' COLLATE = utf8mb4_unicode_ci;

