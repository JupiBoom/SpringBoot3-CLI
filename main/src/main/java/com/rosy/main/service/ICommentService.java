package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Comment;
import com.rosy.main.domain.vo.CommentVO;

import java.util.List;

public interface ICommentService extends IService<Comment> {

    CommentVO getCommentVO(Comment comment);

    LambdaQueryWrapper<Comment> getQueryWrapper(Long courseId, Long userId, Long parentId);

    boolean addComment(Long courseId, Long userId, Byte userType, String content, Long parentId);

    boolean deleteComment(Long id);

    List<CommentVO> getCommentsByCourse(Long courseId);
}
