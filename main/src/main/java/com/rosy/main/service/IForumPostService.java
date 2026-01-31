package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ForumPost;

/**
 * 论坛帖子Service接口
 */
public interface IForumPostService extends IService<ForumPost> {

    /**
     * 分页查询论坛帖子
     *
     * @param page       分页参数
     * @param activityId 关联活动ID（可选）
     * @param userId     用户ID（可选）
     * @param title      帖子标题（可选）
     * @return 论坛帖子分页数据
     */
    Page<ForumPost> getForumPostPage(Page<ForumPost> page, Long activityId, Long userId, String title);

    /**
     * 增加帖子浏览量
     *
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean increaseViewCount(Long postId);

    /**
     * 增加帖子点赞数
     *
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean increaseLikeCount(Long postId);

    /**
     * 减少帖子点赞数
     *
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean decreaseLikeCount(Long postId);
}