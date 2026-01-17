package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IForumPostService;
import com.rosy.main.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    @Resource
    private IActivityService activityService;

    @Resource
    private IUserService userService;

    @Override
    public ForumPostVO getForumPostVO(ForumPost forumPost) {
        if (forumPost == null) {
            return null;
        }
        ForumPostVO forumPostVO = BeanUtil.copyProperties(forumPost, ForumPostVO.class);
        forumPostVO.setTypeName(getTypeName(forumPost.getType()));

        if (forumPost.getActivityId() != null) {
            Activity activity = activityService.getById(forumPost.getActivityId());
            if (activity != null) {
                forumPostVO.setActivityTitle(activity.getTitle());
            }
        }

        User user = userService.getById(forumPost.getUserId());
        if (user != null) {
            forumPostVO.setUserName(user.getRealName());
            forumPostVO.setUserAvatar(user.getAvatar());
        }

        return forumPostVO;
    }

    @Override
    public LambdaQueryWrapper<ForumPost> getQueryWrapper(Long activityId, Long userId, Byte type) {
        LambdaQueryWrapper<ForumPost> queryWrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            queryWrapper.eq(ForumPost::getActivityId, activityId);
        }
        if (userId != null) {
            queryWrapper.eq(ForumPost::getUserId, userId);
        }
        if (type != null) {
            queryWrapper.eq(ForumPost::getType, type);
        }
        queryWrapper.orderByDesc(ForumPost::getIsTop)
                .orderByDesc(ForumPost::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean incrementViewCount(Long postId) {
        ForumPost forumPost = getById(postId);
        if (forumPost == null) {
            return false;
        }
        forumPost.setViewCount(forumPost.getViewCount() + 1);
        return updateById(forumPost);
    }

    @Override
    public boolean incrementLikeCount(Long postId) {
        ForumPost forumPost = getById(postId);
        if (forumPost == null) {
            return false;
        }
        forumPost.setLikeCount(forumPost.getLikeCount() + 1);
        return updateById(forumPost);
    }

    @Override
    public boolean incrementCommentCount(Long postId) {
        ForumPost forumPost = getById(postId);
        if (forumPost == null) {
            return false;
        }
        forumPost.setCommentCount(forumPost.getCommentCount() + 1);
        return updateById(forumPost);
    }

    private String getTypeName(Byte type) {
        if (type == null) {
            return "";
        }
        return switch (type) {
            case 1 -> "经验分享";
            case 2 -> "活动照片";
            default -> "";
        };
    }
}
