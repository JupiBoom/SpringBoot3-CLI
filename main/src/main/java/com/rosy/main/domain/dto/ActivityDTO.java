package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Schema(description = "活动DTO")
public class ActivityDTO {

    @Schema(description = "活动ID")
    private Long id;

    @Schema(description = "活动标题")
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 200, message = "活动标题不能超过200字符")
    private String title;

    @Schema(description = "活动描述")
    private String description;

    @Schema(description = "分类：1-环保 2-助老 3-教育 4-医疗")
    @NotNull(message = "活动分类不能为空")
    @Min(value = 1, message = "分类值必须在1-4之间")
    @Max(value = 4, message = "分类值必须在1-4之间")
    private Integer category;

    @Schema(description = "活动地点")
    @NotBlank(message = "活动地点不能为空")
    @Size(max = 500, message = "活动地点不能超过500字符")
    private String location;

    @Schema(description = "开始时间")
    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须是将来时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "需求人数")
    @NotNull(message = "需求人数不能为空")
    @Min(value = 1, message = "需求人数至少为1人")
    private Integer requiredNum;

    @Schema(description = "联系人")
    @Size(max = 100, message = "联系人姓名不能超过100字符")
    private String contactPerson;

    @Schema(description = "联系电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(description = "报名要求")
    private String requirements;
}
