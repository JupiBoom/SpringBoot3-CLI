package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修人员实体类
 */
@Data
@TableName("repairman")
public class Repairman implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private String phone;

    private String email;

    private String specialty;

    private String skillLevel;

    private String status;

    private Integer completedOrders;

    private BigDecimal avgRating;

    private Integer totalRating;

    private String avatar;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
