package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("live_statistics")
public class LiveStatistics implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long liveRoomId;

    private LocalDateTime statTime;

    private Byte statHour;

    private Integer viewerCount;

    private Integer peakViewerCount;

    private Integer newViewerCount;

    private Integer orderCount;

    private BigDecimal salesAmount;

    private BigDecimal conversionRate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
