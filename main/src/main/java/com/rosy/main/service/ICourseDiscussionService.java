package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.CourseDiscussion;

public interface ICourseDiscussionService extends IService<CourseDiscussion> {

    boolean createDiscussion(Long courseId, Long userId, Byte userType, String content);

    boolean createReply(Long courseId, Long userId, Byte userType, Long parentId, String content);

    boolean likeDiscussion(Long discussionId);
}
