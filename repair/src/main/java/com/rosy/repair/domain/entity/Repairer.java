package com.rosy.repair.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repairer")
public class Repairer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private String phone;

    private String skill;

    private Integer status;

    private Integer orderCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}