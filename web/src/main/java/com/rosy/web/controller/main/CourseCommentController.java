package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.comment.CourseCommentAddRequest;
import com.rosy.main.domain.dto.comment.CourseCommentQueryRequest;
import com.rosy.main.domain.dto.comment.CourseCommentUpdateRequest;
import com.rosy.main.domain.entity.CourseComment;
import com.rosy.main.service.ICourseCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-comment")
@Tag(name = "CourseCommentController", description = "课程评论管理")
public class CourseCommentController {

    @Resource
    private ICourseCommentService courseCommentService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建课程评论")
    public ApiResponse addCourseComment(@RequestBody CourseCommentAddRequest commentAddRequest) {
        CourseComment courseComment = new CourseComment();
        BeanUtils.copyProperties(commentAddRequest, courseComment);
        boolean result = courseCommentService.save(courseComment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(courseComment.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除课程评论")
    public ApiResponse deleteCourseComment(@RequestBody IdRequest idRequest) {
        boolean result = courseCommentService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新课程评论")
    public ApiResponse updateCourseComment(@RequestBody CourseCommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseComment courseComment = new CourseComment();
        BeanUtils.copyProperties(commentUpdateRequest, courseComment);
        boolean result = courseCommentService.updateById(courseComment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取课程评论")
    public ApiResponse getCourseCommentById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseComment courseComment = courseCommentService.getById(id);
        ThrowUtils.throwIf(courseComment == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(courseComment);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取课程评论列表")
    public ApiResponse listCourseCommentByPage(@RequestBody CourseCommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        Page<CourseComment> commentPage = courseCommentService.page(new Page<>(current, size),
                courseCommentService.getQueryWrapper(commentQueryRequest));
        return ApiResponse.success(commentPage);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取课程的评论列表")
    public ApiResponse getCommentsByCourse(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(courseCommentService.getCommentsByCourse(courseId));
    }
}
