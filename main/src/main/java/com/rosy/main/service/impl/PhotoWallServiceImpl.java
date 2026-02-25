package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.PhotoWall;
import com.rosy.main.mapper.PhotoWallMapper;
import com.rosy.main.service.IPhotoWallService;
import org.springframework.stereotype.Service;

@Service
public class PhotoWallServiceImpl extends ServiceImpl<PhotoWallMapper, PhotoWall> implements IPhotoWallService {

    @Override
    public PhotoWall upload(Long activityId, String photoUrl, String description, Long userId) {
        PhotoWall photo = new PhotoWall();
        photo.setActivityId(activityId);
        photo.setUserId(userId);
        photo.setPhotoUrl(photoUrl);
        photo.setDescription(description);
        photo.setLikeCount(0);
        photo.setStatus(1);
        save(photo);
        return photo;
    }

    @Override
    public Page<PhotoWall> pageQuery(Integer page, Integer size, Long activityId) {
        Page<PhotoWall> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<PhotoWall> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(PhotoWall::getActivityId, activityId);
        }
        wrapper.eq(PhotoWall::getStatus, 1);
        wrapper.orderByDesc(PhotoWall::getLikeCount);
        return page(pageObj, wrapper);
    }

    @Override
    public void like(Long photoId) {
        PhotoWall photo = getById(photoId);
        if (photo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "照片不存在");
        }
        photo.setLikeCount(photo.getLikeCount() + 1);
        updateById(photo);
    }
}
