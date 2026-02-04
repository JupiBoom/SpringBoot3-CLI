package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ForumCommentCreateRequest;
import com.rosy.main.domain.entity.ForumComment;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumCommentVO;
import com.rosy.main.mapper.ForumCommentMapper;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.service.IForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 论坛评论服务实现类
 */
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment> implements IForumCommentService {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Override
    public Long createComment(ForumCommentCreateRequest request, Long userId) {
        // 检查帖子是否存在
        ForumPost post = forumPostMapper.selectById(request.getPostId());
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 检查父评论是否存在
        if (request.getParentId() != null) {
            ForumComment parentComment = this.getById(request.getParentId());
            if (parentComment == null) {
                throw new BusinessException("回复的评论不存在");
            }
        }

        ForumComment comment = new ForumComment();
        BeanUtil.copyProperties(request, comment);
        comment.setUserId(userId);
        comment.setStatus(1); // 默认已通过
        comment.setLikeCount(0);

        boolean saved = this.save(comment);
        if (!saved) {
            throw new BusinessException("发布评论失败");
        }

        // 增加帖子评论数
        forumPostMapper.incrementCommentCount(request.getPostId());

        return comment.getId();
    }

    @Override
    public boolean deleteComment(Long id, Long userId) {
        ForumComment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        // 只能删除自己的评论（管理员除外）
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        boolean removed = this.removeById(id);
        if (removed) {
            // 减少帖子评论数
            forumPostMapper.decrementCommentCount(comment.getPostId());
        }

        return removed;
    }

    @Override
    public List<ForumCommentVO> getCommentsByPostId(Long postId) {
        List<ForumComment> allComments = baseMapper.selectByPostId(postId);

        // 转换为VO
        List<ForumCommentVO> voList = allComments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 构建评论树
        Map<Long, ForumCommentVO> commentMap = voList.stream()
                .collect(Collectors.toMap(ForumCommentVO::getId, c -> c));

        List<ForumCommentVO> rootComments = new ArrayList<>();

        for (ForumCommentVO vo : voList) {
            if (vo.getParentId() == null) {
                rootComments.add(vo);
            } else {
                ForumCommentVO parent = commentMap.get(vo.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        }

        return rootComments;
    }

    @Override
    public boolean reviewComment(Long id, Integer status) {
        ForumComment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        comment.setStatus(status);
        return this.updateById(comment);
    }

    @Override
    public ForumCommentVO getCommentById(Long id) {
        ForumComment comment = this.getById(id);
        if (comment == null) {
            return null;
        }
        return convertToVO(comment);
    }

    /**
     * 转换为VO
     */
    private ForumCommentVO convertToVO(ForumComment comment) {
        ForumCommentVO vo = new ForumCommentVO();
        BeanUtil.copyProperties(comment, vo);

        // 状态描述
        vo.setStatusDesc(comment.getStatus() == 0 ? "待审核" : (comment.getStatus() == 1 ? "已通过" : "已拒绝"));

        return vo;
    }
}
