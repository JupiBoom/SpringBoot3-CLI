package com.rosy.main.domain.dto.discussion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程讨论更新请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class DiscussionUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 讨论ID
     */
    private Long id;

    /**
     * 讨论标题
     */
    private String title;

    /**
     * 讨论内容
     */
    private String content;
}