package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.CourseDiscussion;
import com.rosy.main.mapper.CourseDiscussionMapper;
import com.rosy.main.service.ICourseDiscussionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDiscussionServiceImpl extends ServiceImpl<CourseDiscussionMapper, CourseDiscussion> implements ICourseDiscussionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDiscussion(Long courseId, Long userId, Byte userType, String content) {
        CourseDiscussion discussion = new CourseDiscussion();
        discussion.setCourseId(courseId);
        discussion.setUserId(userId);
        discussion.setUserType(userType);
        discussion.setContent(content);
        discussion.setParentId(0L);
        return this.save(discussion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createReply(Long courseId, Long userId, Byte userType, Long parentId, String content) {
        CourseDiscussion reply = new CourseDiscussion();
        reply.setCourseId(courseId);
        reply.setUserId(userId);
        reply.setUserType(userType);
        reply.setContent(content);
        reply.setParentId(parentId);
        boolean result = this.save(reply);
        if (result) {
            CourseDiscussion parent = this.getById(parentId);
            if (parent != null) {
                parent.setReplyCount(parent.getReplyCount() + 1);
                this.updateById(parent);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeDiscussion(Long discussionId) {
        CourseDiscussion discussion = this.getById(discussionId);
        if (discussion == null) {
            return false;
        }
        discussion.setLikeCount(discussion.getLikeCount() + 1);
        return this.updateById(discussion);
    }
}
