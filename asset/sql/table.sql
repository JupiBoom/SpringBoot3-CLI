USE `example`;

DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `name`            VARCHAR(100)     NOT NULL COMMENT '直播间名称，最大长度 100',
    `description`     VARCHAR(500)     DEFAULT NULL COMMENT '直播间简介，最大长度 500',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '直播间状态：0-未开播，1-直播中，2-已结束',
    `cover_url`       VARCHAR(500)     DEFAULT NULL COMMENT '封面图URL',
    `stream_url`      VARCHAR(500)     DEFAULT NULL COMMENT '推流地址',
    `current_goods_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解商品ID',
    `creator_id`      BIGINT UNSIGNED  NOT NULL COMMENT '创建者ID，关联用户表',
    `create_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `start_time`      DATETIME         DEFAULT NULL COMMENT '开播时间',
    `end_time`        DATETIME         DEFAULT NULL COMMENT '结束时间',
    `update_time`     DATETIME         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_goods`;
CREATE TABLE IF NOT EXISTS `live_goods`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`   BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `goods_id`       BIGINT UNSIGNED NOT NULL COMMENT '商品ID，关联商品表',
    `goods_name`     VARCHAR(100)    NOT NULL COMMENT '商品名称',
    `price`          DECIMAL(10, 2)  NOT NULL COMMENT '直播售价',
    `original_price` DECIMAL(10, 2)  DEFAULT NULL COMMENT '原价',
    `stock`          INT UNSIGNED    DEFAULT 0 COMMENT '库存',
    `sell_count`     INT UNSIGNED    DEFAULT 0 COMMENT '已售数量',
    `sort_order`     INT             DEFAULT 0 COMMENT '排序字段',
    `slogan`         VARCHAR(500)    DEFAULT NULL COMMENT '商品卖点',
    `cover_url`      VARCHAR(500)    DEFAULT NULL COMMENT '商品封面图',
    `status`         TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT 'MP逻辑删除字段，0 或 1'
) COMMENT '直播间商品表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_data`;
CREATE TABLE IF NOT EXISTS `live_data`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`   BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `view_count`     INT UNSIGNED    DEFAULT 0 COMMENT '累计观众人数',
    `peak_view_count` INT UNSIGNED   DEFAULT 0 COMMENT '峰值观众人数',
    `current_view_count` INT UNSIGNED DEFAULT 0 COMMENT '当前在线人数',
    `order_count`    INT UNSIGNED    DEFAULT 0 COMMENT '订单数',
    `sales_amount`   DECIMAL(12, 2)  DEFAULT 0.00 COMMENT '销售额',
    `conversion_rate` DECIMAL(5, 2)  DEFAULT 0.00 COMMENT '转化率',
    `like_count`     INT UNSIGNED    DEFAULT 0 COMMENT '点赞数',
    `comment_count`  INT UNSIGNED    DEFAULT 0 COMMENT '评论数',
    `share_count`    INT UNSIGNED    DEFAULT 0 COMMENT '分享数',
    `create_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '统计时间',
    `update_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '直播间数据表' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_goods_rank`;
CREATE TABLE IF NOT EXISTS `live_goods_rank`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`   BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `goods_id`       BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `goods_name`     VARCHAR(100)    NOT NULL COMMENT '商品名称',
    `sell_count`     INT UNSIGNED    DEFAULT 0 COMMENT '销量',
    `sales_amount`   DECIMAL(12, 2)  DEFAULT 0.00 COMMENT '销售额',
    `rank`           INT UNSIGNED    DEFAULT 0 COMMENT '排名',
    `create_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '直播间商品排行榜' COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `live_user_retention`;
CREATE TABLE IF NOT EXISTS `live_user_retention`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID，必须为正整数' PRIMARY KEY,
    `live_room_id`   BIGINT UNSIGNED NOT NULL COMMENT '直播间ID',
    `enter_time`     DATETIME        NOT NULL COMMENT '观众进入时间',
    `leave_time`     DATETIME        DEFAULT NULL COMMENT '观众离开时间',
    `user_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID，可选（匿名用户）',
    `duration`       INT UNSIGNED    DEFAULT 0 COMMENT '停留时长（秒）',
    `create_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '观众留存记录表' COLLATE = utf8mb4_unicode_ci;

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

