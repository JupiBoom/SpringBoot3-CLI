package com.rosy.main.domain.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会议室ID不能为空")
    private Long roomId;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须是将来时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "结束时间必须是将来时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotBlank(message = "会议主题不能为空")
    private String subject;

    private Integer attendeesCount;

    private String remarks;
}
