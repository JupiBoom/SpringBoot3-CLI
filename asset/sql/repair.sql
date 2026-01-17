-- 设备报修工单表
CREATE TABLE repair_order
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    order_no       VARCHAR(32)        NOT NULL COMMENT '工单编号',
    user_id        BIGINT             NOT NULL COMMENT '用户ID',
    device_type    VARCHAR(64)        NOT NULL COMMENT '设备类型',
    location       VARCHAR(128)       NOT NULL COMMENT '位置',
    fault_type     VARCHAR(64)        NOT NULL COMMENT '故障类型',
    description    TEXT               NULL COMMENT '故障描述',
    images         TEXT               NULL COMMENT '故障照片(JSON格式)',
    status         TINYINT            NOT NULL DEFAULT 0 COMMENT '状态: 0-待处理 1-维修中 2-已完成',
    priority       TINYINT            NOT NULL DEFAULT 0 COMMENT '优先级: 0-普通 1-紧急 2-非常紧急',
    repairer_id    BIGINT             NULL COMMENT '维修人员ID',
    assign_type    TINYINT            NOT NULL DEFAULT 0 COMMENT '分配方式: 0-自动 1-手动',
    assign_time    DATETIME           NULL COMMENT '分配时间',
    accept_time    DATETIME           NULL COMMENT '接单时间',
    complete_time  DATETIME           NULL COMMENT '完成时间',
    repair_record  TEXT               NULL COMMENT '维修记录',
    result         VARCHAR(256)       NULL COMMENT '维修结果',
    star           TINYINT            NULL COMMENT '评分(1-5星)',
    evaluation     TEXT               NULL COMMENT '评价内容',
    create_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted     TINYINT            NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_repairer_id (repairer_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备报修工单表';

-- 维修人员表
CREATE TABLE repairer
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id        BIGINT             NOT NULL COMMENT '用户ID',
    name           VARCHAR(32)        NOT NULL COMMENT '姓名',
    phone          VARCHAR(16)        NULL COMMENT '电话',
    skill          VARCHAR(128)       NULL COMMENT '擅长技能',
    status         TINYINT            NOT NULL DEFAULT 0 COMMENT '状态: 0-空闲 1-忙碌',
    order_count    INT                NOT NULL DEFAULT 0 COMMENT '完成工单数量',
    create_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted     TINYINT            NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    UNIQUE INDEX uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修人员表';

-- 设备类型表
CREATE TABLE device_type
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name           VARCHAR(64)        NOT NULL COMMENT '设备类型名称',
    remark         VARCHAR(256)       NULL COMMENT '备注',
    sort           INT                NOT NULL DEFAULT 0 COMMENT '排序',
    status         TINYINT            NOT NULL DEFAULT 0 COMMENT '状态: 0-启用 1-禁用',
    create_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted     TINYINT            NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    UNIQUE INDEX uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备类型表';

-- 故障类型表
CREATE TABLE fault_type
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name           VARCHAR(64)        NOT NULL COMMENT '故障类型名称',
    device_type_id BIGINT             NOT NULL COMMENT '设备类型ID',
    remark         VARCHAR(256)       NULL COMMENT '备注',
    sort           INT                NOT NULL DEFAULT 0 COMMENT '排序',
    status         TINYINT            NOT NULL DEFAULT 0 COMMENT '状态: 0-启用 1-禁用',
    create_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted     TINYINT            NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    INDEX idx_device_type_id (device_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='故障类型表';

-- 通知消息表
CREATE TABLE repair_notification
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id        BIGINT             NOT NULL COMMENT '用户ID',
    order_id       BIGINT             NOT NULL COMMENT '工单ID',
    type           TINYINT            NOT NULL COMMENT '通知类型: 0-工单创建 1-工单分配 2-工单接单 3-工单完成 4-评价提醒',
    content        VARCHAR(512)       NOT NULL COMMENT '通知内容',
    is_read        TINYINT            NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读 1-已读',
    create_time    DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修通知消息表';

-- 初始化数据
INSERT INTO device_type (name, remark, sort) VALUES ('电脑', '台式机/笔记本', 1);
INSERT INTO device_type (name, remark, sort) VALUES ('打印机', '激光/喷墨打印机', 2);
INSERT INTO device_type (name, remark, sort) VALUES ('复印机', '数码复印机', 3);
INSERT INTO device_type (name, remark, sort) VALUES ('投影仪', '办公投影仪', 4);
INSERT INTO device_type (name, remark, sort) VALUES ('网络设备', '路由器/交换机', 5);

INSERT INTO fault_type (name, device_type_id, sort) VALUES ('无法开机', 1, 1);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('蓝屏死机', 1, 2);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('系统卡顿', 1, 3);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('无法打印', 2, 1);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('打印模糊', 2, 2);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('卡纸', 2, 3);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('复印不清', 3, 1);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('投影模糊', 4, 1);
INSERT INTO fault_type (name, device_type_id, sort) VALUES ('无信号', 5, 1);