package com.rosy.main.domain.dto.resource;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseResourceUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long courseId;

    @Size(max = 100, message = "资源名称长度不能超过100个字符")
    private String resourceName;

    private Integer resourceType;

    @Size(max = 500, message = "资源路径长度不能超过500个字符")
    private String resourcePath;

    @Size(max = 500, message = "资源描述长度不能超过500个字符")
    private String description;
}
