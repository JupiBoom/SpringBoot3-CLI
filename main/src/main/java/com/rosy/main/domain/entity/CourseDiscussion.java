package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程讨论表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@TableName("course_discussion")
public class CourseDiscussion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 讨论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}