package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "报名记录VO")
public class RegistrationVO {

    @Schema(description = "报名ID")
    private Long id;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动标题")
    private String activityTitle;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户电话")
    private String userPhone;

    @Schema(description = "报名理由")
    private String reason;

    @Schema(description = "状态：0-待审核 1-审核通过 2-审核拒绝")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "签到时间")
    private LocalDateTime checkInTime;

    @Schema(description = "签出时间")
    private LocalDateTime checkOutTime;

    @Schema(description = "服务时长（小时）")
    private BigDecimal serviceDuration;

    @Schema(description = "报名时间")
    private LocalDateTime createTime;

    @Schema(description = "活动开始时间")
    private LocalDateTime activityStartTime;

    @Schema(description = "活动地点")
    private String activityLocation;
}
