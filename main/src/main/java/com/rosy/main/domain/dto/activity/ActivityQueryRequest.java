package com.rosy.main.domain.dto.activity;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @Min(value = 1, message = "活动分类值无效")
    @Max(value = 5, message = "活动分类值无效")
    private Byte category;

    @Min(value = 1, message = "活动状态值无效")
    @Max(value = 4, message = "活动状态值无效")
    private Byte status;

    @Positive(message = "组织者ID必须为正整数")
    private Long organizerId;

    private LocalDateTime startTimeBegin;

    private LocalDateTime startTimeEnd;

    private String location;
}
