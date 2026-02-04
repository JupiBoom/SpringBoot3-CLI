package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ForumCommentCreateRequest;
import com.rosy.main.domain.entity.ForumComment;
import com.rosy.main.domain.vo.ForumCommentVO;

import java.util.List;

/**
 * 论坛评论服务接口
 */
public interface IForumCommentService extends IService<ForumComment> {

    /**
     * 创建评论
     */
    Long createComment(ForumCommentCreateRequest request, Long userId);

    /**
     * 删除评论
     */
    boolean deleteComment(Long id, Long userId);

    /**
     * 根据帖子ID获取评论列表
     */
    List<ForumCommentVO> getCommentsByPostId(Long postId);

    /**
     * 审核评论
     */
    boolean reviewComment(Long id, Integer status);

    /**
     * 获取评论详情
     */
    ForumCommentVO getCommentById(Long id);
}
