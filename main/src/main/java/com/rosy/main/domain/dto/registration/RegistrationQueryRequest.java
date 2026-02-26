package com.rosy.main.domain.dto.registration;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "活动ID必须为正整数")
    private Long activityId;

    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @Min(value = 0, message = "状态值无效")
    @Max(value = 3, message = "状态值无效")
    private Byte status;
}
