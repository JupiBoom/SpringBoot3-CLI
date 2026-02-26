package com.rosy.main.domain.dto.activity;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ActivityUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动ID不能为空")
    private Long id;

    @Size(max = 100, message = "活动标题长度不能超过100个字符")
    private String title;

    private String description;

    @Min(value = 1, message = "活动分类值无效")
    @Max(value = 5, message = "活动分类值无效")
    private Byte category;

    @Min(value = 1, message = "活动状态值无效")
    @Max(value = 4, message = "活动状态值无效")
    private Byte status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Size(max = 255, message = "活动地点长度不能超过255个字符")
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

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
