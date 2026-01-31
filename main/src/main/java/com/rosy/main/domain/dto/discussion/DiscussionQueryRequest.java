package com.rosy.main.domain.dto.discussion;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程讨论查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DiscussionQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 用户ID
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
     * 父讨论ID（用于回复）
     */
    private Long parentId;

    /**
     * 是否只查询根讨论（无父讨论）
     */
    private Boolean onlyRoot;
}