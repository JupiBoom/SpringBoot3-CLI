package com.rosy.main.domain.dto.activity;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ActivityAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "活动标题不能为空")
    @Size(max = 100, message = "活动标题长度不能超过100个字符")
    private String title;

    private String description;

    @NotNull(message = "活动分类不能为空")
    @Min(value = 1, message = "活动分类值无效")
    @Max(value = 5, message = "活动分类值无效")
    private Byte category;

    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "活动结束时间不能为空")
    private LocalDateTime endTime;

    @NotBlank(message = "活动地点不能为空")
    @Size(max = 255, message = "活动地点长度不能超过255个字符")
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @NotNull(message = "需求人数不能为空")
    @Min(value = 1, message = "需求人数至少为1")
    private Integer requiredCount;

    private LocalDateTime checkInStart;

    private LocalDateTime checkInEnd;

    private LocalDateTime checkOutStart;

    private LocalDateTime checkOutEnd;

    private Byte autoApprove;

    private String coverImage;

    private String contactName;

    private String contactPhone;
}
