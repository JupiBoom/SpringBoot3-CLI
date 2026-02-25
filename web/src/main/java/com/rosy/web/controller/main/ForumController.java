package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.service.IForumPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
@Tag(name = "活动论坛")
public class ForumController {

    @Resource
    private IForumPostService forumPostService;

    @PostMapping("/post/add")
    @ValidateRequest
    @Operation(summary = "发布帖子")
    public ApiResponse createPost(@RequestBody ForumPostDTO dto) {
        ForumPost post = forumPostService.create(dto, 1L);
        return ApiResponse.success(post);
    }

    @GetMapping("/post/list")
    @Operation(summary = "获取帖子列表")
    public ApiResponse listPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long activityId) {
        Page<ForumPost> result = forumPostService.pageQuery(page, size, activityId);
        return ApiResponse.success(result);
    }

    @GetMapping("/post/get")
    @Operation(summary = "获取帖子详情")
    public ApiResponse getPost(Long id) {
        ForumPost post = forumPostService.getById(id);
        if (post != null) {
            forumPostService.view(id);
        }
        return ApiResponse.success(post);
    }

    @PostMapping("/post/like")
    @ValidateRequest
    @Operation(summary = "点赞帖子")
    public ApiResponse likePost(@RequestBody IdRequest idRequest) {
        forumPostService.like(idRequest.getId());
        return ApiResponse.success(true);
    }
}
