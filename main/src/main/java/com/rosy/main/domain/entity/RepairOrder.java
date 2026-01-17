package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报修工单实体类
 */
@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private String userName;

    private String userPhone;

    private Long equipmentId;

    private String equipmentName;

    private String equipmentType;

    private String location;

    private String faultType;

    private String faultDescription;

    private String faultImages;

    private String priority;

    private String status;

    private Long repairmanId;

    private String repairmanName;

    private LocalDateTime assignmentTime;

    private LocalDateTime acceptTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer responseTime;

    private Integer repairDuration;

    private BigDecimal totalCost;

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
