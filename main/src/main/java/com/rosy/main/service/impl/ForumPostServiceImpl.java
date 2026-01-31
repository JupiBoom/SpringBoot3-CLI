package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.service.IForumPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 论坛帖子Service实现类
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    @Override
    public Page<ForumPost> getForumPostPage(Page<ForumPost> page, Long activityId, Long userId, String title) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (activityId != null) {
            queryWrapper.eq("activity_id", activityId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean increaseViewCount(Long postId) {
        ForumPost post = this.getById(postId);
        if (post == null) {
            return false;
        }
        
        post.setViewCount(post.getViewCount() + 1);
        return this.updateById(post);
    }

    @Override
    public boolean increaseLikeCount(Long postId) {
        ForumPost post = this.getById(postId);
        if (post == null) {
            return false;
        }
        
        post.setLikeCount(post.getLikeCount() + 1);
        return this.updateById(post);
    }

    @Override
    public boolean decreaseLikeCount(Long postId) {
        ForumPost post = this.getById(postId);
        if (post == null || post.getLikeCount() <= 0) {
            return false;
        }
        
        post.setLikeCount(post.getLikeCount() - 1);
        return this.updateById(post);
    }
}