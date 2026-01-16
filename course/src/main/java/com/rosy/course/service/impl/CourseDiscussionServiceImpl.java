package com.rosy.course.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.CourseDiscussion;
import com.rosy.course.mapper.CourseDiscussionMapper;
import com.rosy.course.service.ICourseDiscussionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程讨论表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class CourseDiscussionServiceImpl extends ServiceImpl<CourseDiscussionMapper, CourseDiscussion> implements ICourseDiscussionService {

    @Override
    public boolean likeComment(Long id, Long userId) {
        CourseDiscussion discussion = this.getById(id);
        if (ObjectUtil.isNull(discussion)) {
            return false;
        }
        
        discussion.setLikeCount(discussion.getLikeCount() + 1);
        return this.updateById(discussion);
    }

}