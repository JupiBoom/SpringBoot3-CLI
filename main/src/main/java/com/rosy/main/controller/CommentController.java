package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.CommentVO;
import com.rosy.main.service.ICommentService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @PostMapping("/add")
    public BaseResponse<Long> addComment(
            @RequestParam Long courseId,
            @RequestParam Long userId,
            @RequestParam Byte userType,
            @RequestParam String content,
            @RequestParam(required = false) Long parentId) {
        if (courseId == null || userId == null || userType == null || content == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = commentService.addComment(courseId, userId, userType, content, parentId);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(1L);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = commentService.deleteComment(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/course")
    public BaseResponse<List<CommentVO>> getCommentsByCourse(@RequestParam Long courseId) {
        if (courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CommentVO> commentVOs = commentService.getCommentsByCourse(courseId);
        return ResultUtils.success(commentVOs);
    }
}
