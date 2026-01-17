package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumPostVO;

public interface IForumPostService extends IService<ForumPost> {

    ForumPostVO getForumPostVO(ForumPost forumPost);

    LambdaQueryWrapper<ForumPost> getQueryWrapper(Long activityId, Long userId, Byte type);

    boolean incrementViewCount(Long postId);

    boolean incrementLikeCount(Long postId);

    boolean incrementCommentCount(Long postId);
}
