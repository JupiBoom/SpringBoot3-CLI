package com.rosy.main.domain.dto.homework;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HomeworkUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long courseId;

    @Size(max = 100, message = "作业标题长度不能超过100个字符")
    private String title;

    @Size(max = 1000, message = "作业描述长度不能超过1000个字符")
    private String description;

    private LocalDateTime deadline;

    private Integer fullScore;
}
