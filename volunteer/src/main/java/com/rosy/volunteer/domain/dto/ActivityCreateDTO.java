package com.rosy.volunteer.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class ActivityCreateDTO {
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 200, message = "活动标题不能超过200个字符")
    private String title;
    @Size(max = 2000, message = "活动描述不能超过2000个字符")
    private String description;
    @NotNull(message = "活动分类不能为空")
    private Long categoryId;
    @NotNull(message = "活动时间不能为空")
    @Future(message = "活动时间必须是未来时间")
    private LocalDateTime activityTime;
    @NotBlank(message = "活动地点不能为空")
    @Size(max = 200, message = "活动地点不能超过200个字符")
    private String location;
    @NotNull(message = "需求人数不能为空")
    @Min(value = 1, message = "需求人数至少为1人")
    @Max(value = 1000, message = "需求人数不能超过1000人")
    private Integer requiredVolunteers;
    private String contactName;
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String contactPhone;
    @Size(max = 500, message = "封面图片URL不能超过500个字符")
    private String coverImage;
    @Min(value = 1, message = "预计时长至少为1分钟")
    @Max(value = 480, message = "预计时长不能超过8小时")
    private Integer maxDuration;
}
