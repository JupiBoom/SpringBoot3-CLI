package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.PostTypeEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.post.PostQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Post;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.PostVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.PostMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IPostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public PostVO getPostVO(Post post) {
        if (post == null) {
            return null;
        }
        PostVO vo = BeanUtil.copyProperties(post, PostVO.class);

        PostTypeEnum typeEnum = PostTypeEnum.getByCode(post.getType());
        if (typeEnum != null) {
            vo.setTypeDesc(typeEnum.getDesc());
        }

        if (post.getActivityId() != null) {
            Activity activity = activityMapper.selectById(post.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        if (post.getUserId() != null) {
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                vo.setUserAvatar(user.getAvatar());
            }
        }

        return vo;
    }

    @Override
    public LambdaQueryWrapper<Post> getQueryWrapper(PostQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Post::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getActivityId(), Post::getActivityId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), Post::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getType(), Post::getType);

        if (queryRequest.getKeyword() != null && !queryRequest.getKeyword().isEmpty()) {
            queryWrapper.and(w -> w
                    .like(Post::getTitle, queryRequest.getKeyword())
                    .or()
                    .like(Post::getContent, queryRequest.getKeyword()));
        }

        queryWrapper.eq(Post::getStatus, 1);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Post::getCreateTime);

        return queryWrapper;
    }

    @Override
    public boolean incrementViewCount(Long postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getId, postId)
                .setSql("view_count = view_count + 1");
        return this.update(updateWrapper);
    }

    @Override
    public boolean incrementLikeCount(Long postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getId, postId)
                .setSql("like_count = like_count + 1");
        return this.update(updateWrapper);
    }
}
