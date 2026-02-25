package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.service.IForumPostService;
import org.springframework.stereotype.Service;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    @Override
    public ForumPost create(ForumPostDTO dto, Long userId) {
        ForumPost post = new ForumPost();
        post.setActivityId(dto.getActivityId());
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setReplyCount(0);
        post.setIsTop(0);
        post.setStatus(1);
        save(post);
        return post;
    }

    @Override
    public Page<ForumPost> pageQuery(Integer page, Integer size, Long activityId) {
        Page<ForumPost> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ForumPost::getActivityId, activityId);
        }
        wrapper.eq(ForumPost::getStatus, 1)
               .orderByDesc(ForumPost::getIsTop)
               .orderByDesc(ForumPost::getCreateTime);
        return page(pageObj, wrapper);
    }

    @Override
    public void view(Long postId) {
        ForumPost post = getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        }
        post.setViewCount(post.getViewCount() + 1);
        updateById(post);
    }

    @Override
    public void like(Long postId) {
        ForumPost post = getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        }
        post.setLikeCount(post.getLikeCount() + 1);
        updateById(post);
    }
}
