package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.comment.CourseCommentQueryRequest;
import com.rosy.main.domain.entity.CourseComment;

import java.util.List;

public interface ICourseCommentService extends IService<CourseComment> {

    Long addComment(CourseComment comment);

    Boolean updateComment(CourseComment comment);

    Boolean deleteComment(IdRequest idRequest);

    CourseComment getCommentById(Long id);

    Page<CourseComment> listComments(int current, int size, Long courseId, Long parentId);

    LambdaQueryWrapper<CourseComment> getQueryWrapper(CourseCommentQueryRequest queryRequest);

    List<CourseComment> getCommentsByCourse(Long courseId);
}