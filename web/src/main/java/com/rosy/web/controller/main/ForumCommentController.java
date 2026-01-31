package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.ForumComment;
import com.rosy.main.service.IForumCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论坛评论控制器
 */
@Tag(name = "论坛评论管理", description = "论坛评论相关接口")
@RestController
@RequestMapping("/forum-comment")
public class ForumCommentController {

    @Autowired
    private IForumCommentService forumCommentService;

    @Operation(summary = "创建论坛评论")
    @PostMapping
    public ApiResponse<Long> createForumComment(@RequestBody ForumComment forumComment) {
        // 设置初始点赞数为0
        forumComment.setLikeCount(0);
        boolean result = forumCommentService.save(forumComment);
        if (result) {
            return ApiResponse.success(forumComment.getId());
        } else {
            return ApiResponse.error("创建评论失败");
        }
    }

    @Operation(summary = "根据ID获取评论详情")
    @GetMapping("/{id}")
    public ApiResponse<ForumComment> getForumCommentById(@Parameter(description = "评论ID") @PathVariable Long id) {
        ForumComment forumComment = forumCommentService.getById(id);
        if (forumComment != null) {
            return ApiResponse.success(forumComment);
        } else {
            return ApiResponse.error("评论不存在");
        }
    }

    @Operation(summary = "分页查询论坛评论")
    @GetMapping("/page")
    public ApiResponse<Page<ForumComment>> getForumCommentPage(PageRequest pageRequest,
                                                             @Parameter(description = "帖子ID") @RequestParam(required = false) Long postId,
                                                             @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                             @Parameter(description = "父评论ID") @RequestParam(required = false) Long parentId) {
        Page<ForumComment> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<ForumComment> result = forumCommentService.getForumCommentPage(page, postId, userId, parentId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取帖子的所有评论")
    @GetMapping("/post/{postId}")
    public ApiResponse<List<ForumComment>> getCommentsByPostId(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        List<ForumComment> comments = forumCommentService.getCommentsByPostId(postId);
        return ApiResponse.success(comments);
    }

    @Operation(summary = "更新论坛评论")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateForumComment(@Parameter(description = "评论ID") @PathVariable Long id,
                                                   @RequestBody ForumComment forumComment) {
        forumComment.setId(id);
        boolean result = forumCommentService.updateById(forumComment);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("更新评论失败");
        }
    }

    @Operation(summary = "删除论坛评论")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteForumComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        boolean result = forumCommentService.removeById(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("删除评论失败");
        }
    }

    @Operation(summary = "批量删除论坛评论")
    @DeleteMapping("/batch")
    public ApiResponse<Boolean> deleteForumCommentBatch(@RequestBody List<Long> ids) {
        boolean result = forumCommentService.removeByIds(ids);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("批量删除评论失败");
        }
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/{id}/like")
    public ApiResponse<Boolean> likeComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        boolean result = forumCommentService.increaseLikeCount(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("点赞失败");
        }
    }

    @Operation(summary = "取消点赞评论")
    @DeleteMapping("/{id}/like")
    public ApiResponse<Boolean> unlikeComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        boolean result = forumCommentService.decreaseLikeCount(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("取消点赞失败");
        }
    }
}