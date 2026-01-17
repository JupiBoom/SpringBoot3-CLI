package com.rosy.repair.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("repair_notification")
public class RepairNotification {

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }


    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long orderId;

    private Integer type;

    private String content;

    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}