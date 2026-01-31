package com.rosy.main.domain.dto.resource;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseResourceAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "资源名称不能为空")
    @Size(max = 100, message = "资源名称长度不能超过100个字符")
    private String resourceName;

    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    @NotBlank(message = "资源路径不能为空")
    @Size(max = 500, message = "资源路径长度不能超过500个字符")
    private String resourcePath;

    @Size(max = 500, message = "资源描述长度不能超过500个字符")
    private String description;

    @NotNull(message = "上传者ID不能为空")
    private Long uploaderId;
}
