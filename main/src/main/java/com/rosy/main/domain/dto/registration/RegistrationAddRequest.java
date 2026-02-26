package com.rosy.main.domain.dto.registration;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegistrationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动ID不能为空")
    @Positive(message = "活动ID必须为正整数")
    private Long activityId;
}
