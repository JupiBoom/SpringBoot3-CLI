package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.comment.CourseCommentQueryRequest;
import com.rosy.main.domain.entity.CourseComment;
import com.rosy.main.mapper.CourseCommentMapper;
import com.rosy.main.service.ICourseCommentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCommentServiceImpl extends ServiceImpl<CourseCommentMapper, CourseComment> implements ICourseCommentService {

    @Override
    public Long addComment(CourseComment comment) {
        this.save(comment);
        return comment.getId();
    }

    @Override
    public Boolean updateComment(CourseComment comment) {
        return this.updateById(comment);
    }

    @Override
    public Boolean deleteComment(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public CourseComment getCommentById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<CourseComment> listComments(int current, int size, Long courseId, Long parentId) {
        Page<CourseComment> page = new Page<>(current, size);
        LambdaQueryWrapper<CourseComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, CourseComment::getCourseId, courseId);
        wrapper.eq(parentId != null, CourseComment::getParentId, parentId);
        wrapper.orderByDesc(CourseComment::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public LambdaQueryWrapper<CourseComment> getQueryWrapper(CourseCommentQueryRequest queryRequest) {
        LambdaQueryWrapper<CourseComment> wrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return wrapper;
        }
        Long id = queryRequest.getId();
        Long courseId = queryRequest.getCourseId();
        Long studentId = queryRequest.getStudentId();
        Long parentId = queryRequest.getParentId();

        wrapper.eq(ObjectUtils.isNotEmpty(id), CourseComment::getId, id);
        wrapper.eq(ObjectUtils.isNotEmpty(courseId), CourseComment::getCourseId, courseId);
        wrapper.eq(ObjectUtils.isNotEmpty(studentId), CourseComment::getStudentId, studentId);
        wrapper.eq(ObjectUtils.isNotEmpty(parentId), CourseComment::getParentId, parentId);

        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        if (StringUtils.isNotBlank(sortField)) {
            wrapper.orderBy(SqlUtils.validSortField(sortField),
                    sortOrder.equals("ascending"),
                    CourseComment::getCreateTime);
        }
        return wrapper;
    }

    @Override
    public List<CourseComment> getCommentsByCourse(Long courseId) {
        LambdaQueryWrapper<CourseComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseComment::getCourseId, courseId);
        wrapper.orderByDesc(CourseComment::getCreateTime);
        return this.list(wrapper);
    }
}