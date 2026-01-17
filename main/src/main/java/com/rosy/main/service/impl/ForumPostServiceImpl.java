package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.entity.PostImage;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.dto.req.ForumPostAddRequest;
import com.rosy.main.dto.req.ForumPostUpdateRequest;
import com.rosy.main.dto.req.ForumPostQueryRequest;
import com.rosy.main.dto.req.PostImageAddRequest;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.mapper.PostImageMapper;
import com.rosy.main.service.IForumPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    private final PostImageMapper postImageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addForumPost(ForumPostAddRequest request) {
        ForumPost forumPost = new ForumPost();
        BeanUtil.copyProperties(request, forumPost);
        forumPost.setViewCount(0L);
        forumPost.setLikeCount(0L);
        forumPost.setStatus(1);
        forumPost.setCreateTime(LocalDateTime.now());
        save(forumPost);
        return forumPost.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForumPost(ForumPostUpdateRequest request) {
        ForumPost forumPost = getById(request.getId());
        if (forumPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        if (!forumPost.getUserId().equals(request.getUserId())) {
            throw new RuntimeException("无权限修改该帖子");
        }
        BeanUtil.copyProperties(request, forumPost, "id", "userId", "createTime", "viewCount", "likeCount");
        forumPost.setUpdateTime(LocalDateTime.now());
        updateById(forumPost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForumPost(Long id) {
        removeById(id);
        LambdaQueryWrapper<PostImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostImage::getPostId, id);
        postImageMapper.delete(wrapper);
    }

    @Override
    public ForumPostVO getForumPostVO(Long id) {
        ForumPost forumPost = getById(id);
        if (forumPost == null) {
            return null;
        }
        return toVO(forumPost);
    }

    @Override
    public List<ForumPostVO> listForumPostVO(ForumPostQueryRequest request) {
        LambdaQueryWrapper<ForumPost> wrapper = getQueryWrapper(request);
        Page<ForumPost> page = new Page<>(request.getPageNum(), request.getPageSize());
        page(page, wrapper);
        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        ForumPost forumPost = getById(id);
        if (forumPost != null) {
            forumPost.setViewCount(forumPost.getViewCount() + 1);
            updateById(forumPost);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleLike(Long id) {
        ForumPost forumPost = getById(id);
        if (forumPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        forumPost.setLikeCount(forumPost.getLikeCount() + 1);
        updateById(forumPost);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadPostImage(Long postId, PostImageAddRequest request) {
        ForumPost forumPost = getById(postId);
        if (forumPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        int order = 1;
        LambdaQueryWrapper<PostImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostImage::getPostId, postId)
               .orderByDesc(PostImage::getImageOrder)
               .last("LIMIT 1");
        PostImage lastImage = postImageMapper.selectOne(wrapper);
        if (lastImage != null) {
            order = lastImage.getImageOrder() + 1;
        }
        PostImage postImage = new PostImage();
        postImage.setPostId(postId);
        postImage.setImageUrl(request.getImageUrl());
        postImage.setImageOrder(order);
        postImage.setCreateTime(LocalDateTime.now());
        postImageMapper.insert(postImage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePostImage(Long imageId) {
        postImageMapper.deleteById(imageId);
    }

    @Override
    public List<String> getActivityPhotoWall(Long activityId) {
        LambdaQueryWrapper<ForumPost> postWrapper = new LambdaQueryWrapper<>();
        postWrapper.eq(ForumPost::getActivityId, activityId)
                   .eq(ForumPost::getStatus, 1);
        List<ForumPost> posts = list(postWrapper);
        if (posts.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> postIds = posts.stream()
                                  .map(ForumPost::getId)
                                  .collect(Collectors.toList());
        LambdaQueryWrapper<PostImage> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.in(PostImage::getPostId, postIds)
                   .orderByAsc(PostImage::getImageOrder);
        List<PostImage> images = postImageMapper.selectList(imageWrapper);
        return images.stream()
                     .map(PostImage::getImageUrl)
                     .collect(Collectors.toList());
    }

    @Override
    public List<ForumPostVO> getHotPosts(int limit) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getStatus, 1)
               .orderByDesc(ForumPost::getLikeCount)
               .orderByDesc(ForumPost::getViewCount)
               .last("LIMIT " + limit);
        List<ForumPost> posts = list(wrapper);
        return posts.stream()
                    .map(this::toVO)
                    .collect(Collectors.toList());
    }

    @Override
    public List<ForumPostVO> getVolunteerPosts(Long userId) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getUserId, userId)
               .eq(ForumPost::getStatus, 1)
               .orderByDesc(ForumPost::getCreateTime);
        List<ForumPost> posts = list(wrapper);
        return posts.stream()
                    .map(this::toVO)
                    .collect(Collectors.toList());
    }

    private LambdaQueryWrapper<ForumPost> getQueryWrapper(ForumPostQueryRequest request) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getStatus, 1);
        if (request.getActivityId() != null) {
            wrapper.eq(ForumPost::getActivityId, request.getActivityId());
        }
        if (StrUtil.isNotBlank(request.getKeyword())) {
            wrapper.and(w -> w.like(ForumPost::getTitle, request.getKeyword())
                              .or().like(ForumPost::getContent, request.getKeyword()));
        }
        wrapper.orderByDesc(ForumPost::getCreateTime);
        return wrapper;
    }

    private ForumPostVO toVO(ForumPost forumPost) {
        ForumPostVO vo = new ForumPostVO();
        BeanUtil.copyProperties(forumPost, vo);
        LambdaQueryWrapper<PostImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostImage::getPostId, forumPost.getId())
               .orderByAsc(PostImage::getImageOrder);
        List<PostImage> images = postImageMapper.selectList(wrapper);
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = images.stream()
                                          .map(PostImage::getImageUrl)
                                          .collect(Collectors.toList());
            vo.setImages(imageUrls);
            vo.setCoverImage(images.get(0).getImageUrl());
        }
        return vo;
    }
}