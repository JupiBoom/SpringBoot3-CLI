package com.rosy.main.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HomeworkSubmitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 作业ID
     */
    @NotNull(message = "作业ID不能为空")
    @Positive(message = "作业ID必须为正整数")
    private Long homeworkId;

    /**
     * 学生ID
     */
    @NotNull(message = "学生ID不能为空")
    @Positive(message = "学生ID必须为正整数")
    private Long studentId;

    /**
     * 提交内容
     */
    @Size(max = 1000, message = "提交内容长度不能超过1000个字符")
    private String content;

    /**
     * 附件URL
     */
    @Size(max = 500, message = "附件URL长度不能超过500个字符")
    private String fileUrl;
}
