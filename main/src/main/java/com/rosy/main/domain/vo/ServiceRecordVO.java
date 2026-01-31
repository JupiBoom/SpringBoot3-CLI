package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "服务记录VO")
public class ServiceRecordVO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "报名记录ID")
    private Long registrationId;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动标题")
    private String activityTitle;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "服务日期")
    private LocalDate serviceDate;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "服务时长（小时）")
    private BigDecimal duration;

    @Schema(description = "服务证明编号")
    private String certificateNo;

    @Schema(description = "服务证明文件路径")
    private String certificateUrl;

    @Schema(description = "证明生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
