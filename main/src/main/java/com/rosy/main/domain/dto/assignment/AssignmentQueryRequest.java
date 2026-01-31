package com.rosy.main.domain.dto.assignment;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 作业查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 状态：0-未发布，1-已发布，2-已截止
     */
    private Byte status;

    /**
     * 是否只查询已发布的作业
     */
    private Boolean onlyPublished;
}