package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修评价实体类
 */
@Data
@TableName("repair_evaluation")
public class RepairEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long userId;

    private String userName;

    private Long repairmanId;

    private String repairmanName;

    private Integer rating;

    private String content;

    private String images;

    private Integer responseRating;

    private Integer serviceRating;

    private Integer qualityRating;

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
