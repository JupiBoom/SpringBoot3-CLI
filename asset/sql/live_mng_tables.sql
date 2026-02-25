USE `example`;

-- 直播间表
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE IF NOT EXISTS `live_room`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `title`           VARCHAR(200)     DEFAULT NULL COMMENT '直播间标题',
    `cover_url`       VARCHAR(500)     DEFAULT NULL COMMENT '封面图片URL',
    `anchor_id`       BIGINT UNSIGNED  DEFAULT NULL COMMENT '主播ID',
    `anchor_name`     VARCHAR(100)     DEFAULT NULL COMMENT '主播名称',
    `status`          TINYINT UNSIGNED DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    `start_time`      DATETIME         DEFAULT NULL COMMENT '开播时间',
    `end_time`        DATETIME         DEFAULT NULL COMMENT '结束时间',
    `current_product_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '当前讲解商品ID',
    `viewer_count`    INT UNSIGNED     DEFAULT 0 COMMENT '当前观众人数',
    `total_viewer_count` INT UNSIGNED  DEFAULT 0 COMMENT '累计观众人数',
    `total_sales`     DECIMAL(12,2)    DEFAULT 0.00 COMMENT '总销售额',
    `total_orders`    INT UNSIGNED     DEFAULT 0 COMMENT '总订单数',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) COMMENT '直播间表' COLLATE = utf8mb4_unicode_ci;

-- 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `name`            VARCHAR(200)     DEFAULT NULL COMMENT '商品名称',
    `cover_url`       VARCHAR(500)     DEFAULT NULL COMMENT '商品封面图',
    `price`           DECIMAL(10,2)    DEFAULT NULL COMMENT '商品价格',
    `original_price`  DECIMAL(10,2)    DEFAULT NULL COMMENT '原价',
    `stock`           INT UNSIGNED     DEFAULT 0 COMMENT '库存',
    `sold_count`      INT UNSIGNED     DEFAULT 0 COMMENT '已售数量',
    `selling_points`  VARCHAR(1000)    DEFAULT NULL COMMENT '商品卖点，JSON数组格式',
    `description`     TEXT             DEFAULT NULL COMMENT '商品详情描述',
    `status`          TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) COMMENT '商品表' COLLATE = utf8mb4_unicode_ci;

-- 直播间商品关联表
DROP TABLE IF EXISTS `live_room_product`;
CREATE TABLE IF NOT EXISTS `live_room_product`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '直播间ID',
    `product_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '商品ID',
    `sort_order`      INT              DEFAULT 0 COMMENT '排序',
    `is_explaining`   TINYINT UNSIGNED DEFAULT 0 COMMENT '是否正在讲解：0-否，1-是',
    `explain_start_time` DATETIME      DEFAULT NULL COMMENT '讲解开始时间',
    `creator_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`     DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`         TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`      TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_product_id` (`product_id`)
) COMMENT '直播间商品关联表' COLLATE = utf8mb4_unicode_ci;

-- 销售记录表
DROP TABLE IF EXISTS `sales_record`;
CREATE TABLE IF NOT EXISTS `sales_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '直播间ID',
    `product_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '商品ID',
    `order_no`        VARCHAR(100)     DEFAULT NULL COMMENT '订单号',
    `user_id`         BIGINT UNSIGNED  DEFAULT NULL COMMENT '购买用户ID',
    `quantity`        INT UNSIGNED     DEFAULT 1 COMMENT '购买数量',
    `unit_price`      DECIMAL(10,2)    DEFAULT NULL COMMENT '单价',
    `total_amount`    DECIMAL(12,2)    DEFAULT NULL COMMENT '总金额',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '销售记录表' COLLATE = utf8mb4_unicode_ci;

-- 观众数据记录表
DROP TABLE IF EXISTS `audience_record`;
CREATE TABLE IF NOT EXISTS `audience_record`
(
    `id`              BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '直播间ID',
    `viewer_count`    INT UNSIGNED     DEFAULT 0 COMMENT '当前观众人数',
    `new_viewer_count` INT UNSIGNED    DEFAULT 0 COMMENT '新增观众数',
    `leave_viewer_count` INT UNSIGNED  DEFAULT 0 COMMENT '离开观众数',
    `record_time`     DATETIME         DEFAULT NULL COMMENT '记录时间',
    `create_time`     DATETIME         DEFAULT NULL COMMENT '创建时间',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_record_time` (`record_time`)
) COMMENT '观众数据记录表' COLLATE = utf8mb4_unicode_ci;

-- 直播数据统计表（按小时汇总）
DROP TABLE IF EXISTS `live_statistics`;
CREATE TABLE IF NOT EXISTS `live_statistics`
(
    `id`                BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `live_room_id`      BIGINT UNSIGNED  DEFAULT NULL COMMENT '直播间ID',
    `stat_time`         DATETIME         DEFAULT NULL COMMENT '统计时间点',
    `stat_hour`         TINYINT UNSIGNED DEFAULT NULL COMMENT '统计小时（0-23）',
    `viewer_count`      INT UNSIGNED     DEFAULT 0 COMMENT '该时段平均观众数',
    `peak_viewer_count` INT UNSIGNED     DEFAULT 0 COMMENT '该时段峰值观众数',
    `new_viewer_count`  INT UNSIGNED     DEFAULT 0 COMMENT '该时段新增观众数',
    `order_count`       INT UNSIGNED     DEFAULT 0 COMMENT '该时段订单数',
    `sales_amount`      DECIMAL(12,2)    DEFAULT 0.00 COMMENT '该时段销售额',
    `conversion_rate`   DECIMAL(5,2)     DEFAULT 0.00 COMMENT '转化率（%）',
    `create_time`       DATETIME         DEFAULT NULL COMMENT '创建时间',
    INDEX `idx_live_room_id` (`live_room_id`),
    INDEX `idx_stat_time` (`stat_time`)
) COMMENT '直播数据统计表' COLLATE = utf8mb4_unicode_ci;

-- 商品排行榜视图（按销售额排序）
DROP VIEW IF EXISTS `v_product_ranking`;
CREATE VIEW `v_product_ranking` AS
SELECT 
    lr.id AS live_room_id,
    p.id AS product_id,
    p.name AS product_name,
    p.cover_url,
    p.price,
    COALESCE(SUM(sr.quantity), 0) AS total_sold,
    COALESCE(SUM(sr.total_amount), 0) AS total_sales,
    COUNT(DISTINCT sr.id) AS order_count
FROM live_room lr
LEFT JOIN live_room_product lrp ON lr.id = lrp.live_room_id AND lrp.is_deleted = 0
LEFT JOIN product p ON lrp.product_id = p.id AND p.is_deleted = 0
LEFT JOIN sales_record sr ON sr.product_id = p.id AND sr.live_room_id = lr.id
WHERE lr.is_deleted = 0
GROUP BY lr.id, p.id, p.name, p.cover_url, p.price
ORDER BY total_sales DESC;
