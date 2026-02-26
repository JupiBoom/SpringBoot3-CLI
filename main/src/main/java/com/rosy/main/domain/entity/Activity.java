package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Byte category;

    private Byte status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer requiredCount;

    private Integer currentCount;

    private LocalDateTime checkInStart;

    private LocalDateTime checkInEnd;

    private LocalDateTime checkOutStart;

    private LocalDateTime checkOutEnd;

    private Byte autoApprove;

    private String coverImage;

    private String contactName;

    private String contactPhone;

    private Long organizerId;

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Byte version;

    @TableLogic
    private Byte isDeleted;
}
