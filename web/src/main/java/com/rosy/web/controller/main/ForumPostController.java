package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.service.IForumPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论坛帖子控制器
 */
@Tag(name = "论坛帖子管理", description = "论坛帖子相关接口")
@RestController
@RequestMapping("/forum-post")
public class ForumPostController {

    @Autowired
    private IForumPostService forumPostService;

    @Operation(summary = "创建论坛帖子")
    @PostMapping
    public ApiResponse<Long> createForumPost(@RequestBody ForumPost forumPost) {
        // 设置初始点赞数和浏览数为0
        forumPost.setLikeCount(0);
        forumPost.setViewCount(0);
        boolean result = forumPostService.save(forumPost);
        if (result) {
            return ApiResponse.success(forumPost.getId());
        } else {
            return ApiResponse.error("创建帖子失败");
        }
    }

    @Operation(summary = "根据ID获取帖子详情")
    @GetMapping("/{id}")
    public ApiResponse<ForumPost> getForumPostById(@Parameter(description = "帖子ID") @PathVariable Long id) {
        ForumPost forumPost = forumPostService.getById(id);
        if (forumPost != null) {
            // 增加浏览量
            forumPostService.increaseViewCount(id);
            return ApiResponse.success(forumPost);
        } else {
            return ApiResponse.error("帖子不存在");
        }
    }

    @Operation(summary = "分页查询论坛帖子")
    @GetMapping("/page")
    public ApiResponse<Page<ForumPost>> getForumPostPage(PageRequest pageRequest,
                                                        @Parameter(description = "关联活动ID") @RequestParam(required = false) Long activityId,
                                                        @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                        @Parameter(description = "帖子标题") @RequestParam(required = false) String title) {
        Page<ForumPost> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<ForumPost> result = forumPostService.getForumPostPage(page, activityId, userId, title);
        return ApiResponse.success(result);
    }

    @Operation(summary = "更新论坛帖子")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateForumPost(@Parameter(description = "帖子ID") @PathVariable Long id,
                                                @RequestBody ForumPost forumPost) {
        forumPost.setId(id);
        boolean result = forumPostService.updateById(forumPost);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("更新帖子失败");
        }
    }

    @Operation(summary = "删除论坛帖子")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteForumPost(@Parameter(description = "帖子ID") @PathVariable Long id) {
        boolean result = forumPostService.removeById(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("删除帖子失败");
        }
    }

    @Operation(summary = "批量删除论坛帖子")
    @DeleteMapping("/batch")
    public ApiResponse<Boolean> deleteForumPostBatch(@RequestBody List<Long> ids) {
        boolean result = forumPostService.removeByIds(ids);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("批量删除帖子失败");
        }
    }

    @Operation(summary = "点赞帖子")
    @PostMapping("/{id}/like")
    public ApiResponse<Boolean> likePost(@Parameter(description = "帖子ID") @PathVariable Long id) {
        boolean result = forumPostService.increaseLikeCount(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("点赞失败");
        }
    }

    @Operation(summary = "取消点赞帖子")
    @DeleteMapping("/{id}/like")
    public ApiResponse<Boolean> unlikePost(@Parameter(description = "帖子ID") @PathVariable Long id) {
        boolean result = forumPostService.decreaseLikeCount(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("取消点赞失败");
        }
    }
}