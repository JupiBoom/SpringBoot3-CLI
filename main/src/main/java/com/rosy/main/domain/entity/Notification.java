package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知实体类
 */
@Data
@TableName("notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID，关联用户表
     */
    private Long userId;

    /**
     * 通知标题，最大长度 100
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型：0-系统通知，1-活动提醒，2-报名审核，3-服务评价
     */
    private Integer type;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer isRead;

    /**
     * 关联ID（如活动ID、报名ID等）
     */
    private Long relatedId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除字段，0 或 1
     */
    @TableLogic
    private Integer isDeleted;
}