package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 论坛评论VO
 */
@Data
public class ForumCommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论者ID
     */
    private Long userId;

    /**
     * 评论者姓名
     */
    private String userName;

    /**
     * 评论者头像
     */
    private String userAvatar;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 父评论者姓名
     */
    private String parentUserName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子评论列表
     */
    private List<ForumCommentVO> children;
}
