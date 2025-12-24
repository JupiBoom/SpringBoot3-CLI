-- 通知记录表
CREATE TABLE `notification_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `notification_id` varchar(64) NOT NULL COMMENT '通知ID',
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `notification_type` varchar(32) NOT NULL COMMENT '通知类型',
  `template_id` varchar(64) NOT NULL COMMENT '通知模板ID',
  `content` text COMMENT '通知内容',
  `target_address` varchar(128) NOT NULL COMMENT '目标地址（手机号/邮箱/微信OpenID）',
  `send_status` varchar(32) NOT NULL COMMENT '发送状态',
  `send_count` int(11) NOT NULL DEFAULT '0' COMMENT '发送次数',
  `last_send_time` datetime DEFAULT NULL COMMENT '最后一次发送时间',
  `first_send_time` datetime NOT NULL COMMENT '首次发送时间',
  `success_time` datetime DEFAULT NULL COMMENT '成功时间',
  `error_msg` text COMMENT '错误信息',
  `retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  `next_retry_time` datetime DEFAULT NULL COMMENT '下次重试时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_notification_id` (`notification_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_notification_type` (`notification_type`),
  KEY `idx_send_status` (`send_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知记录表';

-- 用户通知偏好设置表
CREATE TABLE `user_notification_preference` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `notification_type` varchar(32) NOT NULL COMMENT '通知类型（短信/微信/邮件）',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `channel` varchar(128) NOT NULL COMMENT '通知渠道（手机号/邮箱/微信OpenID）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_notification_type` (`user_id`,`notification_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_notification_type` (`notification_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知偏好设置表';

-- 通知模板表
CREATE TABLE `notification_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` varchar(64) NOT NULL COMMENT '模板ID',
  `template_name` varchar(128) NOT NULL COMMENT '模板名称',
  `notification_type` varchar(32) NOT NULL COMMENT '通知类型',
  `order_status` varchar(32) NOT NULL COMMENT '订单状态',
  `template_content` text NOT NULL COMMENT '模板内容',
  `template_variables` text COMMENT '模板变量（JSON格式）',
  `description` varchar(256) DEFAULT NULL COMMENT '模板描述',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_template_id` (`template_id`),
  KEY `idx_notification_type` (`notification_type`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知模板表';

-- 插入示例数据
-- 通知模板数据
INSERT INTO `notification_template` (`template_id`, `template_name`, `notification_type`, `order_status`, `template_content`, `template_variables`, `description`, `enabled`) VALUES
('sms_pay_success', '短信支付成功通知', 'sms', 'pay_success', '您的订单{{orderId}}已支付成功，商品{{productName}}，金额{{orderAmount}}元。感谢您的购买！', '{"orderId": "订单号", "productName": "商品名称", "orderAmount": "订单金额"}', '短信支付成功通知模板', 1),
('sms_shipped', '短信发货通知', 'sms', 'shipped', '您的订单{{orderId}}已发货，商品{{productName}}，请留意物流信息。', '{"orderId": "订单号", "productName": "商品名称"}', '短信发货通知模板', 1),
('sms_signed', '短信签收通知', 'sms', 'signed', '您的订单{{orderId}}已签收，商品{{productName}}，请查收。', '{"orderId": "订单号", "productName": "商品名称"}', '短信签收通知模板', 1),
('wechat_pay_success', '微信支付成功通知', 'wechat', 'pay_success', '【电商平台】您的订单{{orderId}}已支付成功，商品{{productName}}，金额{{orderAmount}}元。感谢您的购买！', '{"orderId": "订单号", "productName": "商品名称", "orderAmount": "订单金额"}', '微信支付成功通知模板', 1),
('wechat_shipped', '微信发货通知', 'wechat', 'shipped', '【电商平台】您的订单{{orderId}}已发货，商品{{productName}}，请留意物流信息。', '{"orderId": "订单号", "productName": "商品名称"}', '微信发货通知模板', 1),
('wechat_signed', '微信签收通知', 'wechat', 'signed', '【电商平台】您的订单{{orderId}}已签收，商品{{productName}}，请查收。', '{"orderId": "订单号", "productName": "商品名称"}', '微信签收通知模板', 1),
('email_pay_success', '邮件支付成功通知', 'email', 'pay_success', '尊敬的用户，您的订单{{orderId}}已支付成功，商品{{productName}}，金额{{orderAmount}}元。感谢您的购买！', '{"orderId": "订单号", "productName": "商品名称", "orderAmount": "订单金额"}', '邮件支付成功通知模板', 1),
('email_shipped', '邮件发货通知', 'email', 'shipped', '尊敬的用户，您的订单{{orderId}}已发货，商品{{productName}}，请留意物流信息。', '{"orderId": "订单号", "productName": "商品名称"}', '邮件发货通知模板', 1),
('email_signed', '邮件签收通知', 'email', 'signed', '尊敬的用户，您的订单{{orderId}}已签收，商品{{productName}}，请查收。', '{"orderId": "订单号", "productName": "商品名称"}', '邮件签收通知模板', 1);

-- 用户通知偏好设置数据
INSERT INTO `user_notification_preference` (`user_id`, `notification_type`, `enabled`, `channel`) VALUES
('user_1', 'sms', 1, '13800138000'),
('user_1', 'wechat', 1, 'openid_123'),
('user_1', 'email', 1, 'user1@example.com'),
('user_2', 'sms', 1, '13900139000'),
('user_2', 'email', 1, 'user2@example.com');