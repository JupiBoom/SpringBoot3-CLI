package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.dto.ForumReplyDTO;
import com.rosy.main.domain.dto.PhotoDTO;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.domain.vo.ForumReplyVO;
import com.rosy.main.domain.vo.PhotoVO;
import com.rosy.main.service.IForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "活动论坛")
@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final IForumService forumService;

    @Operation(summary = "发布帖子")
    @PostMapping("/post")
    public ApiResponse createPost(@Validated @RequestBody ForumPostDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(forumService.createPost(dto, userId));
    }

    @Operation(summary = "更新帖子")
    @PutMapping("/post/{postId}")
    public ApiResponse updatePost(
            @Parameter(description = "帖子ID") @PathVariable Long postId,
            @Validated @RequestBody ForumPostDTO dto) {
        Long userId = 1L;
        forumService.updatePost(postId, dto, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/post/{postId}")
    public ApiResponse deletePost(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = 1L;
        forumService.deletePost(postId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取帖子详情")
    @GetMapping("/post/{postId}")
    public ApiResponse getPostDetail(
            @Parameter(description = "帖子ID") @PathVariable Long postId) {
        return ApiResponse.success(forumService.getPostDetail(postId));
    }

    @Operation(summary = "分页查询帖子")
    @GetMapping("/post/page")
    public ApiResponse getPostPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        return ApiResponse.success(forumService.getPostPage(pageNum, pageSize, activityId, userId));
    }

    @Operation(summary = "发布回复")
    @PostMapping("/reply")
    public ApiResponse createReply(@Validated @RequestBody ForumReplyDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(forumService.createReply(dto, userId));
    }

    @Operation(summary = "删除回复")
    @DeleteMapping("/reply/{replyId}")
    public ApiResponse deleteReply(
            @Parameter(description = "回复ID") @PathVariable Long replyId) {
        Long userId = 1L;
        forumService.deleteReply(replyId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取帖子回复列表")
    @GetMapping("/reply/post/{postId}")
    public ApiResponse getRepliesByPost(
            @Parameter(description = "帖子ID") @PathVariable Long postId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(forumService.getRepliesByPost(postId, pageNum, pageSize));
    }

    @Operation(summary = "上传照片")
    @PostMapping("/photo")
    public ApiResponse uploadPhoto(@Validated @RequestBody PhotoDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(forumService.uploadPhoto(dto, userId));
    }

    @Operation(summary = "删除照片")
    @DeleteMapping("/photo/{photoId}")
    public ApiResponse deletePhoto(
            @Parameter(description = "照片ID") @PathVariable Long photoId) {
        Long userId = 1L;
        forumService.deletePhoto(photoId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取活动照片列表")
    @GetMapping("/photo/activity/{activityId}")
    public ApiResponse getPhotosByActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(forumService.getPhotosByActivity(activityId, pageNum, pageSize));
    }

    @Operation(summary = "获取照片墙")
    @GetMapping("/photo/wall")
    public ApiResponse getPhotoWall(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        return ApiResponse.success(forumService.getPhotoWall(pageNum, pageSize));
    }
}
