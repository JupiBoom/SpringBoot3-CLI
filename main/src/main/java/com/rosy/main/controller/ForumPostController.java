package com.rosy.main.controller;

import com.rosy.main.common.BaseResponse;
import com.rosy.main.common.ResultUtils;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.dto.req.ForumPostAddRequest;
import com.rosy.main.dto.req.ForumPostUpdateRequest;
import com.rosy.main.dto.req.ForumPostQueryRequest;
import com.rosy.main.dto.req.PostImageAddRequest;
import com.rosy.main.service.IForumPostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum-post")
@RequiredArgsConstructor
@Validated
public class ForumPostController {

    private final IForumPostService forumPostService;

    @PostMapping
    public BaseResponse<Long> addForumPost(@Valid @RequestBody ForumPostAddRequest request) {
        Long id = forumPostService.addForumPost(request);
        return ResultUtils.success(id);
    }

    @PutMapping
    public BaseResponse<Void> updateForumPost(@Valid @RequestBody ForumPostUpdateRequest request) {
        forumPostService.updateForumPost(request);
        return ResultUtils.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteForumPost(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        forumPostService.deleteForumPost(id);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public BaseResponse<ForumPostVO> getForumPostVO(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        ForumPostVO forumPostVO = forumPostService.getForumPostVO(id);
        return ResultUtils.success(forumPostVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<ForumPostVO>> listForumPostVO(@Valid ForumPostQueryRequest request) {
        List<ForumPostVO> forumPostVOList = forumPostService.listForumPostVO(request);
        return ResultUtils.success(forumPostVOList);
    }

    @PostMapping("/{id}/view")
    public BaseResponse<Void> incrementViewCount(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        forumPostService.incrementViewCount(id);
        return ResultUtils.success();
    }

    @PostMapping("/{id}/like")
    public BaseResponse<Void> toggleLike(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        forumPostService.toggleLike(id);
        return ResultUtils.success();
    }

    @PostMapping("/{postId}/images")
    public BaseResponse<Void> uploadPostImage(
            @PathVariable @NotNull(message = "帖子ID不能为空") Long postId,
            @Valid @RequestBody PostImageAddRequest request) {
        forumPostService.uploadPostImage(postId, request);
        return ResultUtils.success();
    }

    @DeleteMapping("/images/{imageId}")
    public BaseResponse<Void> deletePostImage(@PathVariable @NotNull(message = "图片ID不能为空") Long imageId) {
        forumPostService.deletePostImage(imageId);
        return ResultUtils.success();
    }

    @GetMapping("/activity/{activityId}/photo-wall")
    public BaseResponse<List<String>> getActivityPhotoWall(
            @PathVariable @NotNull(message = "活动ID不能为空") Long activityId) {
        List<String> photoWall = forumPostService.getActivityPhotoWall(activityId);
        return ResultUtils.success(photoWall);
    }

    @GetMapping("/hot")
    public BaseResponse<List<ForumPostVO>> getHotPosts(
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) {
        List<ForumPostVO> hotPosts = forumPostService.getHotPosts(limit);
        return ResultUtils.success(hotPosts);
    }

    @GetMapping("/user/{userId}")
    public BaseResponse<List<ForumPostVO>> getVolunteerPosts(
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<ForumPostVO> volunteerPosts = forumPostService.getVolunteerPosts(userId);
        return ResultUtils.success(volunteerPosts);
    }
}