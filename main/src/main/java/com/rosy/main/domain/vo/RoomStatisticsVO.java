package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private String roomName;

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
}
