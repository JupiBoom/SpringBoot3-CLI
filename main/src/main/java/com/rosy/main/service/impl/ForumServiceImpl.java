package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.dto.ForumReplyDTO;
import com.rosy.main.domain.dto.PhotoDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.entity.ForumReply;
import com.rosy.main.domain.entity.Photo;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.domain.vo.ForumReplyVO;
import com.rosy.main.domain.vo.PhotoVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.mapper.ForumReplyMapper;
import com.rosy.main.mapper.PhotoMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumService {

    private final ForumReplyMapper forumReplyMapper;
    private final PhotoMapper photoMapper;
    private final ActivityMapper activityMapper;
    private final ServiceRecordMapper serviceRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(ForumPostDTO dto, Long userId) {
        if (dto.getActivityId() != null) {
            Activity activity = activityMapper.selectById(dto.getActivityId());
            if (activity == null) {
                throw new BusinessException("活动不存在");
            }

            ServiceRecord serviceRecord = serviceRecordMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ServiceRecord>()
                            .eq(ServiceRecord::getActivityId, dto.getActivityId())
                            .eq(ServiceRecord::getUserId, userId)
                            .isNotNull(ServiceRecord::getCheckOutTime));
            if (serviceRecord == null) {
                throw new BusinessException("您未完成该活动的服务，无法分享经验");
            }
        }

        ForumPost post = BeanUtil.copyProperties(dto, ForumPost.class);
        post.setUserId(userId);
        post.setViewCount(0);
        post.setReplyCount(0);
        post.setCreatedTime(LocalDateTime.now());
        save(post);

        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updatePost(Long postId, ForumPostDTO dto, Long userId) {
        ForumPost post = getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权修改他人的帖子");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUpdatedTime(LocalDateTime.now());
        updateById(post);

        return postId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long postId, Long userId) {
        ForumPost post = getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人的帖子");
        }

        removeById(postId);

        forumReplyMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ForumReply>()
                        .eq(ForumReply::getPostId, postId));
    }

    @Override
    public ForumPostVO getPostDetail(Long postId) {
        ForumPost post = getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        post.setViewCount(post.getViewCount() + 1);
        updateById(post);

        return BeanUtil.copyProperties(post, ForumPostVO.class);
    }

    @Override
    public Page<ForumPostVO> getPostPage(Integer pageNum, Integer pageSize, Long activityId, Long userId) {
        Page<ForumPost> page = new Page<>(pageNum, pageSize);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ForumPost> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ForumPost>()
                .orderByDesc(ForumPost::getCreatedTime);

        if (activityId != null) {
            wrapper.eq(ForumPost::getActivityId, activityId);
        }
        if (userId != null) {
            wrapper.eq(ForumPost::getUserId, userId);
        }

        Page<ForumPost> result = page(page, wrapper);
        return result.convert(p -> BeanUtil.copyProperties(p, ForumPostVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReply(ForumReplyDTO dto, Long userId) {
        ForumPost post = getById(dto.getPostId());
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        ForumReply reply = BeanUtil.copyProperties(dto, ForumReply.class);
        reply.setUserId(userId);
        reply.setCreatedTime(LocalDateTime.now());
        forumReplyMapper.insert(reply);

        post.setReplyCount(post.getReplyCount() + 1);
        updateById(post);

        return reply.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReply(Long replyId, Long userId) {
        ForumReply reply = forumReplyMapper.selectById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }

        if (!reply.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人的回复");
        }

        ForumPost post = getById(reply.getPostId());
        if (post != null) {
            post.setReplyCount(Math.max(0, post.getReplyCount() - 1));
            updateById(post);
        }

        forumReplyMapper.deleteById(replyId);
    }

    @Override
    public Page<ForumReplyVO> getRepliesByPost(Long postId, Integer pageNum, Integer pageSize) {
        Page<ForumReply> page = new Page<>(pageNum, pageSize);
        Page<ForumReply> result = forumReplyMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ForumReply>()
                        .eq(ForumReply::getPostId, postId)
                        .orderByAsc(ForumReply::getCreatedTime));
        return result.convert(r -> BeanUtil.copyProperties(r, ForumReplyVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadPhoto(PhotoDTO dto, Long userId) {
        Activity activity = activityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        ServiceRecord serviceRecord = serviceRecordMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ServiceRecord>()
                        .eq(ServiceRecord::getActivityId, dto.getActivityId())
                        .eq(ServiceRecord::getUserId, userId)
                        .isNotNull(ServiceRecord::getCheckOutTime));
        if (serviceRecord == null) {
            throw new BusinessException("您未完成该活动的服务，无法上传照片");
        }

        Photo photo = BeanUtil.copyProperties(dto, Photo.class);
        photo.setUserId(userId);
        photo.setCreatedTime(LocalDateTime.now());
        photoMapper.insert(photo);

        return photo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePhoto(Long photoId, Long userId) {
        Photo photo = photoMapper.selectById(photoId);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }

        if (!photo.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人的照片");
        }

        photoMapper.deleteById(photoId);
    }

    @Override
    public Page<PhotoVO> getPhotosByActivity(Long activityId, Integer pageNum, Integer pageSize) {
        Page<Photo> page = new Page<>(pageNum, pageSize);
        Page<Photo> result = photoMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Photo>()
                        .eq(Photo::getActivityId, activityId)
                        .orderByDesc(Photo::getCreatedTime));
        return result.convert(p -> BeanUtil.copyProperties(p, PhotoVO.class));
    }

    @Override
    public Page<PhotoVO> getPhotoWall(Integer pageNum, Integer pageSize) {
        Page<Photo> page = new Page<>(pageNum, pageSize);
        Page<Photo> result = photoMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Photo>()
                        .orderByDesc(Photo::getCreatedTime));
        return result.convert(p -> BeanUtil.copyProperties(p, PhotoVO.class));
    }
}
