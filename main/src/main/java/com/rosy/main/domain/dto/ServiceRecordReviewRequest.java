package com.rosy.main.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 服务记录评价请求DTO
 */
@Data
public class ServiceRecordReviewRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @NotNull(message = "记录ID不能为空")
    private Long id;

    /**
     * 服务表现：1-5星评价
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1星")
    @Max(value = 5, message = "评分最高为5星")
    private Integer performance;

    /**
     * 组织者评价
     */
    @Size(max = 500, message = "评价内容长度不能超过500")
    private String organizerComment;
}
