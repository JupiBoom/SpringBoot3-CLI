package com.rosy.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.course.domain.entity.CourseDiscussion;

/**
 * <p>
 * 课程讨论表 Service 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
public interface ICourseDiscussionService extends IService<CourseDiscussion> {

    /**
     * 点赞评论
     */
    boolean likeComment(Long id, Long userId);

}