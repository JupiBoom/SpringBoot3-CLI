package com.rosy.main.domain.dto.activity;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ActivityUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    @Positive(message = "ID必须为正整数")
    private Long id;

    @Size(max = 200, message = "活动标题长度不能超过200个字符")
    private String title;

    private String description;

    @Min(value = 1, message = "活动分类值不能小于1")
    @Max(value = 4, message = "活动分类值不能大于4")
    private Byte category;

    @Min(value = 0, message = "活动状态值不能小于0")
    @Max(value = 2, message = "活动状态值不能大于2")
    private Byte status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Size(max = 200, message = "活动地点长度不能超过200个字符")
    private String location;

    @Positive(message = "需求人数必须为正整数")
    private Integer requiredPeople;
}
