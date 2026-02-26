package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.photo.PhotoQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Photo;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.PhotoVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.PhotoMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IPhotoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public PhotoVO getPhotoVO(Photo photo) {
        if (photo == null) {
            return null;
        }
        PhotoVO vo = BeanUtil.copyProperties(photo, PhotoVO.class);

        if (photo.getActivityId() != null) {
            Activity activity = activityMapper.selectById(photo.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        if (photo.getUserId() != null) {
            User user = userMapper.selectById(photo.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }

        return vo;
    }

    @Override
    public LambdaQueryWrapper<Photo> getQueryWrapper(PhotoQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Photo::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getActivityId(), Photo::getActivityId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), Photo::getUserId);

        queryWrapper.eq(Photo::getStatus, 1);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Photo::getCreateTime);

        return queryWrapper;
    }

    @Override
    public boolean incrementLikeCount(Long photoId) {
        LambdaUpdateWrapper<Photo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Photo::getId, photoId)
                .setSql("like_count = like_count + 1");
        return this.update(updateWrapper);
    }
}
