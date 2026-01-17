package com.rosy.main.domain.dto.activity;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ActivityAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "活动标题不能为空")
    @Size(max = 200, message = "活动标题长度不能超过200个字符")
    private String title;

    private String description;

    @NotNull(message = "活动分类不能为空")
    @Min(value = 1, message = "活动分类值不能小于1")
    @Max(value = 4, message = "活动分类值不能大于4")
    private Byte category;

    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;

    @NotBlank(message = "活动地点不能为空")
    @Size(max = 200, message = "活动地点长度不能超过200个字符")
    private String location;

    @NotNull(message = "需求人数不能为空")
    @Positive(message = "需求人数必须为正整数")
    private Integer requiredPeople;
}
