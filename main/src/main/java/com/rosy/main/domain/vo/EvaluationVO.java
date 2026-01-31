package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评价视图对象
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class EvaluationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 工单号
     */
    private String orderNo;

    /**
     * 评分，1-5星
     */
    private Byte rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价用户ID
     */
    private Long userId;

    /**
     * 评价用户名称
     */
    private String userName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}