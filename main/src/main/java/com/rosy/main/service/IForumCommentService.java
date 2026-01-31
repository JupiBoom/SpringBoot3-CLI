package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ForumComment;

import java.util.List;

/**
 * 论坛评论Service接口
 */
public interface IForumCommentService extends IService<ForumComment> {

    /**
     * 分页查询论坛评论
     *
     * @param page    分页参数
     * @param postId  帖子ID（可选）
     * @param userId  用户ID（可选）
     * @param parentId 父评论ID（可选）
     * @return 论坛评论分页数据
     */
    Page<ForumComment> getForumCommentPage(Page<ForumComment> page, Long postId, Long userId, Long parentId);

    /**
     * 获取帖子的所有评论（包括回复）
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<ForumComment> getCommentsByPostId(Long postId);

    /**
     * 增加评论点赞数
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    boolean increaseLikeCount(Long commentId);

    /**
     * 减少评论点赞数
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    boolean decreaseLikeCount(Long commentId);
}