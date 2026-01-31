package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程讨论视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class CourseDiscussionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 讨论ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 用户ID（可能是学生或教师）
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户类型：1-学生，2-教师
     */
    private Byte userType;

    /**
     * 用户类型描述
     */
    private String userTypeDesc;

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

    /**
     * 父讨论标题
     */
    private String parentTitle;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子讨论列表
     */
    private List<CourseDiscussionVO> children;
}