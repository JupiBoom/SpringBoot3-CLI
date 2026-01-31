package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.ForumComment;
import com.rosy.main.mapper.ForumCommentMapper;
import com.rosy.main.service.IForumCommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 论坛评论Service实现类
 */
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment> implements IForumCommentService {

    @Override
    public Page<ForumComment> getForumCommentPage(Page<ForumComment> page, Long postId, Long userId, Long parentId) {
        QueryWrapper<ForumComment> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (postId != null) {
            queryWrapper.eq("post_id", postId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        } else {
            // 如果没有指定父评论ID，则查询顶级评论
            queryWrapper.isNull("parent_id");
        }
        
        // 按创建时间升序排列
        queryWrapper.orderByAsc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ForumComment> getCommentsByPostId(Long postId) {
        QueryWrapper<ForumComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        queryWrapper.orderByAsc("create_time");
        
        return this.list(queryWrapper);
    }

    @Override
    public boolean increaseLikeCount(Long commentId) {
        ForumComment comment = this.getById(commentId);
        if (comment == null) {
            return false;
        }
        
        comment.setLikeCount(comment.getLikeCount() + 1);
        return this.updateById(comment);
    }

    @Override
    public boolean decreaseLikeCount(Long commentId) {
        ForumComment comment = this.getById(commentId);
        if (comment == null || comment.getLikeCount() <= 0) {
            return false;
        }
        
        comment.setLikeCount(comment.getLikeCount() - 1);
        return this.updateById(comment);
    }
}