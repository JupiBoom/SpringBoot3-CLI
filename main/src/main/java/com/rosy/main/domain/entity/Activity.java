package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer needPeople;
    private Integer appliedPeople;
    private String status;
    private String contactName;
    private String contactPhone;
    private String requirements;
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
