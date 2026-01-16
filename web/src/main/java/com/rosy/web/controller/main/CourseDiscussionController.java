package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.DiscussionRequest;
import com.rosy.main.domain.dto.ReplyRequest;
import com.rosy.main.domain.entity.CourseDiscussion;
import com.rosy.main.service.ICourseDiscussionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程讨论区前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/discussion")
public class CourseDiscussionController {
    @Resource
    ICourseDiscussionService courseDiscussionService;

    /**
     * 创建讨论帖子
     */
    @PostMapping("/create")
    @ValidateRequest
    public ApiResponse createDiscussion(@RequestBody DiscussionRequest request) {
        ThrowUtils.throwIf(request.getCourseId() == null || request.getCourseId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getUserId() == null || request.getUserId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getUserType() == null || (request.getUserType() != 1 && request.getUserType() != 2), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getContent() == null || request.getContent().trim().isEmpty(), ErrorCode.PARAMS_ERROR);

        boolean result = courseDiscussionService.createDiscussion(
                request.getCourseId(),
                request.getUserId(),
                request.getUserType(),
                request.getContent()
        );
        return ApiResponse.success(result);
    }

    /**
     * 回复讨论
     */
    @PostMapping("/reply")
    @ValidateRequest
    public ApiResponse createReply(@RequestBody ReplyRequest request) {
        ThrowUtils.throwIf(request.getCourseId() == null || request.getCourseId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getUserId() == null || request.getUserId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getUserType() == null || (request.getUserType() != 1 && request.getUserType() != 2), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getParentId() == null || request.getParentId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getContent() == null || request.getContent().trim().isEmpty(), ErrorCode.PARAMS_ERROR);

        boolean result = courseDiscussionService.createReply(
                request.getCourseId(),
                request.getUserId(),
                request.getUserType(),
                request.getParentId(),
                request.getContent()
        );
        return ApiResponse.success(result);
    }

    /**
     * 点赞讨论
     */
    @PostMapping("/like")
    public ApiResponse likeDiscussion(@RequestBody IdRequest request) {
        ThrowUtils.throwIf(request.getId() == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = courseDiscussionService.likeDiscussion(request.getId());
        return ApiResponse.success(result);
    }

    /**
     * 获取课程的所有讨论（按时间倒序）
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse getCourseDiscussions(@PathVariable Long courseId) {
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        List<CourseDiscussion> discussions = courseDiscussionService.lambdaQuery()
                .eq(CourseDiscussion::getCourseId, courseId)
                .isNull(CourseDiscussion::getParentId)
                .orderByDesc(CourseDiscussion::getCreateTime)
                .list();
        return ApiResponse.success(discussions);
    }

    /**
     * 获取帖子的回复列表
     */
    @GetMapping("/replies/{parentId}")
    public ApiResponse getReplies(@PathVariable Long parentId) {
        ThrowUtils.throwIf(parentId == null || parentId <= 0, ErrorCode.PARAMS_ERROR);
        List<CourseDiscussion> replies = courseDiscussionService.lambdaQuery()
                .eq(CourseDiscussion::getParentId, parentId)
                .orderByAsc(CourseDiscussion::getCreateTime)
                .list();
        return ApiResponse.success(replies);
    }

    /**
     * 获取讨论详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse getDiscussionById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        CourseDiscussion discussion = courseDiscussionService.getById(id);
        ThrowUtils.throwIf(discussion == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(discussion);
    }

    /**
     * 删除讨论
     */
    @PostMapping("/delete")
    public ApiResponse deleteDiscussion(@RequestBody IdRequest request) {
        ThrowUtils.throwIf(request.getId() == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = courseDiscussionService.removeById(request.getId());
        return ApiResponse.success(result);
    }
}
