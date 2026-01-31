package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;

/**
 * 评价添加请求
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class EvaluationAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 评分，1-5星
     */
    private Byte rating;

    /**
     * 评价内容
     */
    private String content;
}