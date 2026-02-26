package com.rosy.main.domain.dto.servicerecord;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceRecordQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "活动ID必须为正整数")
    private Long activityId;

    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    private LocalDate serviceDateBegin;

    private LocalDate serviceDateEnd;
}
