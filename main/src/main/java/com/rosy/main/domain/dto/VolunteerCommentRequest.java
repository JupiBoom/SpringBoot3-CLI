package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 志愿者评价请求DTO
 */
@Data
public class VolunteerCommentRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @NotNull(message = "记录ID不能为空")
    private Long id;

    /**
     * 志愿者评价
     */
    @Size(max = 500, message = "评价内容长度不能超过500")
    private String volunteerComment;

    /**
     * 服务内容
     */
    @Size(max = 1000, message = "服务内容长度不能超过1000")
    private String serviceContent;
}
