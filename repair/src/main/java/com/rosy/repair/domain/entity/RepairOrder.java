package com.rosy.repair.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repair_order")
public class RepairOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private String deviceType;

    private String location;

    private String faultType;

    private String description;

    private String images;

    private Integer status;

    private Integer priority;

    private Long repairerId;

    private Integer assignType;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private Integer orderCount;

    private String repairRecord;

    private String result;

    private Integer star;

    private String evaluation;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
