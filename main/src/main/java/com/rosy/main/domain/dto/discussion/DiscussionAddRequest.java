package com.rosy.main.domain.dto.discussion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程讨论添加请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class DiscussionAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 用户ID（可能是学生或教师）
     */
    private Long userId;

    /**
     * 用户类型：1-学生，2-教师
     */
    private Byte userType;

    /**
     * 讨论标题
     */
    private String title;

    /**
     * 讨论内容
     */
    private String content;

    /**
     * 父讨论ID（用于回复）
     */
    private Long parentId;
}