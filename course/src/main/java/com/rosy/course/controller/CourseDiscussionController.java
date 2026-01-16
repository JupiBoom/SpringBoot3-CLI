package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.entity.CourseDiscussion;
import com.rosy.course.service.ICourseDiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程讨论表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/discussion")
@RequiredArgsConstructor
@Tag(name = "课程讨论区", description = "课程讨论、评论功能")
public class CourseDiscussionController {

    private final ICourseDiscussionService courseDiscussionService;

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程讨论列表")
    public ApiResponse listByCourse(@PathVariable Long courseId,
                                                       @RequestParam(required = false) Long parentId) {
        LambdaQueryWrapper<CourseDiscussion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseDiscussion::getCourseId, courseId);
        
        if (parentId != null) {
            wrapper.eq(CourseDiscussion::getParentId, parentId);
        } else {
            wrapper.isNull(CourseDiscussion::getParentId);
        }
        
        wrapper.orderByDesc(CourseDiscussion::getCreateTime);
        
        return ApiResponse.success(courseDiscussionService.list(wrapper));
    }

    @PostMapping
    @Operation(summary = "发表评论")
    public ApiResponse create(@RequestBody CourseDiscussion discussion) {
        discussion.setCreateTime(LocalDateTime.now());
        discussion.setUpdateTime(LocalDateTime.now());
        discussion.setLikeCount(0);
        return ApiResponse.success(courseDiscussionService.save(discussion));
    }

    @PutMapping("/like/{id}")
    @Operation(summary = "点赞评论")
    public ApiResponse likeComment(@PathVariable Long id,
                                           @RequestParam Long studentId) {
        return ApiResponse.success(courseDiscussionService.likeComment(id, studentId));
    }

    @PutMapping
    @Operation(summary = "更新评论")
    public ApiResponse update(@RequestBody CourseDiscussion discussion) {
        discussion.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(courseDiscussionService.updateById(discussion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public ApiResponse delete(@PathVariable Long id) {
        return ApiResponse.success(courseDiscussionService.removeById(id));
    }

}