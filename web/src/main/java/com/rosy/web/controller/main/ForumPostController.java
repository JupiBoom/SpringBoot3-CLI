package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.service.IForumPostService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum-post")
public class ForumPostController {

    @Resource
    private IForumPostService forumPostService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addPost(@RequestParam Long userId, @RequestParam String title, @RequestParam String content, @RequestParam(required = false) Long activityId, @RequestParam(defaultValue = "1") byte type) {
        ForumPost forumPost = new ForumPost();
        forumPost.setUserId(userId);
        forumPost.setTitle(title);
        forumPost.setContent(content);
        forumPost.setActivityId(activityId);
        forumPost.setType(type);
        forumPost.setViewCount(0);
        forumPost.setLikeCount(0);
        forumPost.setCommentCount(0);
        forumPost.setIsTop((byte) 0);
        boolean result = forumPostService.save(forumPost);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(forumPost.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deletePost(@RequestBody IdRequest idRequest) {
        boolean result = forumPostService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/get/vo")
    public ApiResponse getPostVOById(@RequestParam Long id) {
        ForumPost forumPost = forumPostService.getById(id);
        ThrowUtils.throwIf(forumPost == null, ErrorCode.NOT_FOUND_ERROR);
        forumPostService.incrementViewCount(id);
        return ApiResponse.success(forumPostService.getForumPostVO(forumPost));
    }

    @GetMapping("/list/page/vo")
    public ApiResponse listPostVOByPage(@RequestParam(required = false) Long activityId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Byte type, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        var queryWrapper = forumPostService.getQueryWrapper(activityId, userId, type);
        Page<ForumPost> page = forumPostService.page(new Page<>(current, size), queryWrapper);
        Page<ForumPostVO> voPage = PageUtils.convert(page, forumPostService::getForumPostVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/like")
    @ValidateRequest
    public ApiResponse likePost(@RequestParam Long postId) {
        boolean result = forumPostService.incrementLikeCount(postId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/comment")
    @ValidateRequest
    public ApiResponse commentPost(@RequestParam Long postId) {
        boolean result = forumPostService.incrementCommentCount(postId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/top")
    @ValidateRequest
    public ApiResponse setPostTop(@RequestParam Long postId, @RequestParam byte isTop) {
        ForumPost forumPost = forumPostService.getById(postId);
        ThrowUtils.throwIf(forumPost == null, ErrorCode.NOT_FOUND_ERROR);
        forumPost.setIsTop(isTop);
        boolean result = forumPostService.updateById(forumPost);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }
}
