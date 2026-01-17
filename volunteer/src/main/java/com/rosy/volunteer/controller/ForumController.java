package com.rosy.volunteer.controller;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ForumPostCreateDTO;
import com.rosy.volunteer.domain.vo.ForumPostDetailVO;
import com.rosy.volunteer.domain.vo.ForumPostListVO;
import com.rosy.volunteer.service.IForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "论坛管理")
@RestController
@RequestMapping("/api/volunteer/forum")
@RequiredArgsConstructor
public class ForumController {

    private final IForumService forumService;

    @Operation(summary = "创建帖子")
    @PostMapping("/post")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Long> createPost(@RequestBody ForumPostCreateDTO dto) {
        Long id = forumService.createPost(dto);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "更新帖子")
    @PutMapping("/post/{id}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String content = request.get("content");
        forumService.updatePost(id, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/post/{id}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        forumService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取帖子详情")
    @GetMapping("/post/{id}")
    public ResponseEntity<ForumPostDetailVO> getPostDetail(@PathVariable Long id) {
        ForumPostDetailVO vo = forumService.getPostDetail(id);
        return ResponseEntity.ok(vo);
    }

    @Operation(summary = "点赞帖子")
    @PostMapping("/post/{id}/like")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        forumService.likePost(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取帖子列表")
    @GetMapping("/post/list")
    public ResponseEntity<List<ForumPostListVO>> getPostList(
            @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        List<ForumPostListVO> list = forumService.getPostList(activityId, status, keyword);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "分页获取帖子")
    @GetMapping("/post/page")
    public ResponseEntity<Map<String, Object>> getPostPage(
            @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pageNum);
        pageRequest.setPageSize(pageSize);
        Map<String, Object> result = (Map<String, Object>) forumService.getPostPage(activityId, status, keyword, pageRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "添加评论")
    @PostMapping("/post/{postId}/comment")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> addComment(
            @PathVariable Long postId,
            @Parameter(description = "父评论ID") @RequestParam(required = false) Long parentId,
            @RequestBody Map<String, String> request) {
        String content = request.get("content");
        forumService.addComment(postId, parentId, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/comment/{id}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        forumService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/comment/{id}/like")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> likeComment(@PathVariable Long id) {
        forumService.likeComment(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取评论列表")
    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<Object>> getComments(
            @PathVariable Long postId,
            @Parameter(description = "父评论ID") @RequestParam(required = false) Long parentId) {
        List<Object> comments = forumService.getComments(postId, parentId);
        return ResponseEntity.ok(comments);
    }
}
