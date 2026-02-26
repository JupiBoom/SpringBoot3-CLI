package com.rosy.main.domain.dto.servicerecord;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RatingRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "服务记录ID不能为空")
    @Positive(message = "服务记录ID必须为正整数")
    private Long serviceRecordId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Byte rating;

    @Size(max = 500, message = "评价内容长度不能超过500个字符")
    private String comment;
}
