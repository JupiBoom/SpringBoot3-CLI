package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.LogTag;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.ForumCommentCreateRequest;
import com.rosy.main.domain.dto.ForumPostCreateRequest;
import com.rosy.main.domain.dto.ForumPostQueryRequest;
import com.rosy.main.domain.vo.ForumCommentVO;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.service.IForumCommentService;
import com.rosy.main.service.IForumPostService;
import com.rosy.web.controller.main.utils.UserHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论坛控制器
 */
@RestController
@RequestMapping("/forum")
@Slf4j
public class ForumController {

    @Autowired
    private IForumPostService forumPostService;

    @Autowired
    private IForumCommentService forumCommentService;

    // ==================== 帖子相关接口 ====================

    /**
     * 创建帖子
     */
    @PostMapping("/post/create")
    @ValidateRequest
    @LogTag(value = "创建帖子", printResult = true)
    public ApiResponse createPost(@Valid @RequestBody ForumPostCreateRequest request) {
        Long userId = UserHolder.getUserId();
        Long postId = forumPostService.createPost(request, userId);
        return ApiResponse.success(postId);
    }

    /**
     * 删除帖子
     */
    @PostMapping("/post/delete")
    @ValidateRequest
    @LogTag(value = "删除帖子", printResult = true)
    public ApiResponse deletePost(@Valid @RequestBody IdRequest request) {
        Long userId = UserHolder.getUserId();
        boolean result = forumPostService.deletePost(request.getId(), userId);
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取帖子详情
     */
    @GetMapping("/post/get/{id}")
    @LogTag(value = "获取帖子详情")
    public ApiResponse getPostById(@PathVariable("id") Long id) {
        ForumPostVO vo = forumPostService.getPostById(id);
        // 增加浏览次数
        forumPostService.incrementViewCount(id);
        return ApiResponse.success(vo);
    }

    /**
     * 分页查询帖子列表
     */
    @PostMapping("/post/list/page")
    @LogTag(value = "分页查询帖子列表")
    public ApiResponse listPostsByPage(@RequestBody ForumPostQueryRequest request) {
        Page<ForumPostVO> page = forumPostService.listPosts(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取置顶帖子
     */
    @GetMapping("/post/top")
    @LogTag(value = "获取置顶帖子")
    public ApiResponse getTopPosts() {
        List<ForumPostVO> list = forumPostService.getTopPosts();
        return ApiResponse.success(list);
    }

    /**
     * 获取精华帖子
     */
    @GetMapping("/post/essence")
    @LogTag(value = "获取精华帖子")
    public ApiResponse getEssencePosts() {
        List<ForumPostVO> list = forumPostService.getEssencePosts();
        return ApiResponse.success(list);
    }

    /**
     * 获取我的帖子列表
     */
    @GetMapping("/post/my-list")
    @LogTag(value = "获取我的帖子列表")
    public ApiResponse getMyPosts() {
        Long userId = UserHolder.getUserId();
        List<ForumPostVO> list = forumPostService.getUserPosts(userId);
        return ApiResponse.success(list);
    }

    // ==================== 评论相关接口 ====================

    /**
     * 创建评论
     */
    @PostMapping("/comment/create")
    @ValidateRequest
    @LogTag(value = "创建评论", printResult = true)
    public ApiResponse createComment(@Valid @RequestBody ForumCommentCreateRequest request) {
        Long userId = UserHolder.getUserId();
        Long commentId = forumCommentService.createComment(request, userId);
        return ApiResponse.success(commentId);
    }

    /**
     * 删除评论
     */
    @PostMapping("/comment/delete")
    @ValidateRequest
    @LogTag(value = "删除评论", printResult = true)
    public ApiResponse deleteComment(@Valid @RequestBody IdRequest request) {
        Long userId = UserHolder.getUserId();
        boolean result = forumCommentService.deleteComment(request.getId(), userId);
        return ApiResponse.success(result);
    }

    /**
     * 获取帖子的评论列表
     */
    @GetMapping("/comment/list/{postId}")
    @LogTag(value = "获取评论列表")
    public ApiResponse getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<ForumCommentVO> list = forumCommentService.getCommentsByPostId(postId);
        return ApiResponse.success(list);
    }
}
