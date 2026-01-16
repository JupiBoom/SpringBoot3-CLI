package com.rosy.main.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HomeworkGradeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 提交记录ID
     */
    @NotNull(message = "提交记录ID不能为空")
    @Positive(message = "提交记录ID必须为正整数")
    private Long submitId;

    /**
     * 分数
     */
    @NotNull(message = "分数不能为空")
    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Double score;

    /**
     * 反馈内容
     */
    @Size(max = 500, message = "反馈内容长度不能超过500个字符")
    private String feedback;
}
