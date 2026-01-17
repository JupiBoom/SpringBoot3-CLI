package com.rosy.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.exception.BusinessException;
import com.rosy.volunteer.domain.dto.ForumPostCreateDTO;
import com.rosy.volunteer.domain.entity.Activity;
import com.rosy.volunteer.domain.entity.ForumComment;
import com.rosy.volunteer.domain.entity.ForumPost;
import com.rosy.volunteer.domain.enums.PostStatusEnum;
import com.rosy.volunteer.domain.vo.ForumCommentVO;
import com.rosy.volunteer.domain.vo.ForumPostDetailVO;
import com.rosy.volunteer.domain.vo.ForumPostListVO;
import com.rosy.volunteer.mapper.ActivityMapper;
import com.rosy.volunteer.mapper.ForumCommentMapper;
import com.rosy.volunteer.mapper.ForumPostMapper;
import com.rosy.volunteer.service.IForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements IForumService {

    private final ForumPostMapper postMapper;
    private final ForumCommentMapper commentMapper;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(ForumPostCreateDTO dto) {
        if (dto.getActivityId() != null) {
            Activity activity = activityMapper.selectById(dto.getActivityId());
            if (activity == null) {
                throw new BusinessException("活动不存在");
            }
        }
        ForumPost post = new ForumPost();
        post.setActivityId(dto.getActivityId());
        post.setVolunteerId(1L);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setStatus(PostStatusEnum.NORMAL.getValue());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePost(Long id, String content) {
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setContent(content);
        post.setUpdateTime(LocalDateTime.now());
        postMapper.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setStatus(PostStatusEnum.DELETED.getValue());
        post.setUpdateTime(LocalDateTime.now());
        postMapper.updateById(post);
    }

    @Override
    public ForumPostDetailVO getPostDetail(Long id) {
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (post.getStatus().equals(PostStatusEnum.DELETED.getValue())) {
            throw new BusinessException("帖子已被删除");
        }
        incrementViewCount(id);
        ForumPostDetailVO vo = new ForumPostDetailVO();
        vo.setId(post.getId());
        vo.setActivityId(post.getActivityId());
        if (post.getActivityId() != null) {
            Activity activity = activityMapper.selectById(post.getActivityId());
            vo.setActivityTitle(activity.getTitle());
        }
        vo.setVolunteerId(post.getVolunteerId());
        vo.setVolunteerName("志愿者");
        vo.setVolunteerAvatar("/avatar/default.jpg");
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setStatus(post.getStatus());
        vo.setStatusName(PostStatusEnum.getByValue(post.getStatus()).getName());
        vo.setViewCount(post.getViewCount());
        vo.setLikeCount(post.getLikeCount());
        vo.setCommentCount(post.getCommentCount());
        vo.setCreateTime(post.getCreateTime());
        vo.setUpdateTime(post.getUpdateTime());
        vo.setComments(getComments(id, null).stream()
                .map(c -> (ForumCommentVO) c)
                .collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        ForumPost post = postMapper.selectById(id);
        if (post != null) {
            post.setViewCount(post.getViewCount() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long id) {
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setLikeCount(post.getLikeCount() + 1);
        postMapper.updateById(post);
    }

    @Override
    public List<ForumPostListVO> getPostList(Long activityId, Integer status, String keyword) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(ForumPost::getStatus, PostStatusEnum.DELETED.getValue());
        if (activityId != null) {
            wrapper.eq(ForumPost::getActivityId, activityId);
        }
        if (status != null) {
            wrapper.eq(ForumPost::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ForumPost::getTitle, keyword).or().like(ForumPost::getContent, keyword));
        }
        wrapper.orderByDesc(ForumPost::getCreateTime);
        List<ForumPost> posts = postMapper.selectList(wrapper);
        return posts.stream().map(post -> {
            ForumPostListVO vo = new ForumPostListVO();
            vo.setId(post.getId());
            vo.setActivityId(post.getActivityId());
            if (post.getActivityId() != null) {
                Activity activity = activityMapper.selectById(post.getActivityId());
                vo.setActivityTitle(activity.getTitle());
            }
            vo.setVolunteerId(post.getVolunteerId());
            vo.setVolunteerName("志愿者");
            vo.setVolunteerAvatar("/avatar/default.jpg");
            vo.setTitle(post.getTitle());
            vo.setStatus(post.getStatus());
            vo.setStatusName(PostStatusEnum.getByValue(post.getStatus()).getName());
            vo.setViewCount(post.getViewCount());
            vo.setLikeCount(post.getLikeCount());
            vo.setCommentCount(post.getCommentCount());
            vo.setCreateTime(post.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Object getPostPage(Long activityId, Integer status, String keyword, PageRequest pageRequest) {
        Page<ForumPost> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(ForumPost::getStatus, PostStatusEnum.DELETED.getValue());
        if (activityId != null) {
            wrapper.eq(ForumPost::getActivityId, activityId);
        }
        if (status != null) {
            wrapper.eq(ForumPost::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(ForumPost::getTitle, keyword).or().like(ForumPost::getContent, keyword));
        }
        wrapper.orderByDesc(ForumPost::getCreateTime);
        Page<ForumPost> result = postMapper.selectPage(page, wrapper);
        List<ForumPostListVO> list = result.getRecords().stream().map(post -> {
            ForumPostListVO vo = new ForumPostListVO();
            vo.setId(post.getId());
            vo.setActivityId(post.getActivityId());
            if (post.getActivityId() != null) {
                Activity activity = activityMapper.selectById(post.getActivityId());
                vo.setActivityTitle(activity.getTitle());
            }
            vo.setVolunteerId(post.getVolunteerId());
            vo.setVolunteerName("志愿者");
            vo.setVolunteerAvatar("/avatar/default.jpg");
            vo.setTitle(post.getTitle());
            vo.setStatus(post.getStatus());
            vo.setStatusName(PostStatusEnum.getByValue(post.getStatus()).getName());
            vo.setViewCount(post.getViewCount());
            vo.setLikeCount(post.getLikeCount());
            vo.setCommentCount(post.getCommentCount());
            vo.setCreateTime(post.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("total", result.getTotal());
        response.put("pageNum", result.getCurrent());
        response.put("pageSize", result.getSize());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(Long postId, Long parentId, String content) {
        ForumPost post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (parentId != null) {
            ForumComment parent = commentMapper.selectById(parentId);
            if (parent == null || !parent.getPostId().equals(postId)) {
                throw new BusinessException("父评论不存在或不匹配");
            }
        }
        ForumComment comment = new ForumComment();
        comment.setPostId(postId);
        comment.setParentId(parentId);
        comment.setVolunteerId(1L);
        comment.setContent(content);
        comment.setLikeCount(0);
        comment.setCreateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        post.setCommentCount(post.getCommentCount() + 1);
        postMapper.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        ForumComment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        commentMapper.deleteById(id);
        ForumPost post = postMapper.selectById(comment.getPostId());
        if (post != null) {
            post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
            postMapper.updateById(post);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(Long id) {
        ForumComment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentMapper.updateById(comment);
    }

    @Override
    public List<Object> getComments(Long postId, Long parentId) {
        LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumComment::getPostId, postId);
        wrapper.eq(parentId != null, ForumComment::getParentId, parentId);
        wrapper.orderByAsc(ForumComment::getCreateTime);
        List<ForumComment> comments = commentMapper.selectList(wrapper);
        return comments.stream().map(comment -> {
            ForumCommentVO vo = new ForumCommentVO();
            vo.setId(comment.getId());
            vo.setPostId(comment.getPostId());
            vo.setVolunteerId(comment.getVolunteerId());
            vo.setVolunteerName("志愿者");
            vo.setVolunteerAvatar("/avatar/default.jpg");
            vo.setContent(comment.getContent());
            vo.setCreateTime(comment.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }
}
