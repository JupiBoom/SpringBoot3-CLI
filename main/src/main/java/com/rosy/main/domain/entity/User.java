package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，最大长度 50
     */
    private String username;

    /**
     * 密码，最大长度 100
     */
    private String password;

    /**
     * 手机号，最大长度 20
     */
    private String phone;

    /**
     * 邮箱，最大长度 100
     */
    private String email;

    /**
     * 真实姓名，最大长度 50
     */
    private String realName;

    /**
     * 身份证号，最大长度 18
     */
    private String idCard;

    /**
     * 头像URL，最大长度 255
     */
    private String avatar;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 地址，最大长度 255
     */
    private String address;

    /**
     * 角色：0-志愿者，1-组织者，2-管理员
     */
    private Integer role;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 累计服务时长
     */
    private BigDecimal totalHours;

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