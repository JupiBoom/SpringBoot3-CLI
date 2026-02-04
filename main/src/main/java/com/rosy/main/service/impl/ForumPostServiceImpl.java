package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ForumPostCreateRequest;
import com.rosy.main.domain.dto.ForumPostQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.enums.ForumPostTypeEnum;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.ForumPostMapper;
import com.rosy.main.service.IForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛帖子服务实现类
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public Long createPost(ForumPostCreateRequest request, Long userId) {
        ForumPost post = new ForumPost();
        BeanUtil.copyProperties(request, post);
        post.setUserId(userId);
        post.setStatus(1); // 默认已通过
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setIsTop(0);
        post.setIsEssence(0);

        // 处理图片列表
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            post.setImages(JSONUtil.toJsonStr(request.getImages()));
        }

        boolean saved = this.save(post);
        if (!saved) {
            throw new BusinessException("发布帖子失败");
        }
        return post.getId();
    }

    @Override
    public boolean deletePost(Long id, Long userId) {
        ForumPost post = this.getById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 只能删除自己的帖子（管理员除外）
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        return this.removeById(id);
    }

    @Override
    public ForumPostVO getPostById(Long id) {
        ForumPost post = this.getById(id);
        if (post == null) {
            return null;
        }
        return convertToVO(post);
    }

    @Override
    public Page<ForumPostVO> listPosts(ForumPostQueryRequest request) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();

        // 关键词搜索
        if (StrUtil.isNotBlank(request.getKeyword())) {
            queryWrapper.and(qw -> qw.like("title", request.getKeyword())
                    .or()
                    .like("content", request.getKeyword()));
        }

        // 活动筛选
        if (request.getActivityId() != null) {
            queryWrapper.eq("activity_id", request.getActivityId());
        }

        // 用户筛选
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }

        // 类型筛选
        if (request.getType() != null) {
            queryWrapper.eq("type", request.getType());
        }

        // 置顶筛选
        if (request.getIsTop() != null) {
            queryWrapper.eq("is_top", request.getIsTop());
        }

        // 精华筛选
        if (request.getIsEssence() != null) {
            queryWrapper.eq("is_essence", request.getIsEssence());
        }

        // 状态筛选
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        } else {
            queryWrapper.eq("status", 1); // 默认只显示已通过
        }

        // 排序：先按置顶，再按精华，再按时间
        queryWrapper.orderByDesc("is_top", "is_essence", "create_time");

        Page<ForumPost> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper);

        List<ForumPostVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<ForumPostVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<ForumPostVO> getTopPosts() {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_top", 1)
                .eq("status", 1)
                .orderByDesc("create_time")
                .last("LIMIT 10");

        List<ForumPost> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ForumPostVO> getEssencePosts() {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_essence", 1)
                .eq("status", 1)
                .orderByDesc("create_time")
                .last("LIMIT 10");

        List<ForumPost> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean setTop(Long id, Integer isTop) {
        ForumPost post = this.getById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setIsTop(isTop);
        return this.updateById(post);
    }

    @Override
    public boolean setEssence(Long id, Integer isEssence) {
        ForumPost post = this.getById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setIsEssence(isEssence);
        return this.updateById(post);
    }

    @Override
    public boolean reviewPost(Long id, Integer status) {
        ForumPost post = this.getById(id);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        post.setStatus(status);
        return this.updateById(post);
    }

    @Override
    public void incrementViewCount(Long id) {
        baseMapper.incrementViewCount(id);
    }

    @Override
    public List<ForumPostVO> getUserPosts(Long userId) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("is_deleted", 0)
                .orderByDesc("create_time");

        List<ForumPost> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private ForumPostVO convertToVO(ForumPost post) {
        ForumPostVO vo = new ForumPostVO();
        BeanUtil.copyProperties(post, vo);
        vo.setTypeDesc(ForumPostTypeEnum.getDescByCode(post.getType()));

        // 解析图片列表
        if (StrUtil.isNotBlank(post.getImages())) {
            vo.setImages(JSONUtil.toList(post.getImages(), String.class));
        }

        // 加载活动信息
        if (post.getActivityId() != null) {
            Activity activity = activityMapper.selectById(post.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        // 状态描述
        vo.setStatusDesc(post.getStatus() == 0 ? "待审核" : (post.getStatus() == 1 ? "已通过" : "已拒绝"));

        return vo;
    }
}
