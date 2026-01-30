package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("room_statistics")
public class RoomStatistics implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roomId;

    private LocalDate statisticsDate;

    private Integer totalBookings;

    private Integer approvedBookings;

    private Integer rejectedBookings;

    private Integer cancelledBookings;

    private Integer completedBookings;

    private BigDecimal usageHours;

    private BigDecimal occupancyRate;

    private Integer totalCheckIns;

    private Integer actualAttendees;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Byte version;

    @TableLogic
    private Byte isDeleted;
}
