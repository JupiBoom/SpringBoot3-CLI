package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 志愿者实体类
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Data
@TableName("volunteer")
public class Volunteer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 志愿者ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别：1-男，2-女
     */
    private Byte gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 总服务时长（分钟）
     */
    private Integer totalServiceDuration;

    /**
     * 服务次数
     */
    private Integer serviceCount;

    /**
     * 状态：0-禁用，1-正常
     */
    private Byte status;

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
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}