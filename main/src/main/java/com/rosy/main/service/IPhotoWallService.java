package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.PhotoWall;

public interface IPhotoWallService extends IService<PhotoWall> {
    PhotoWall upload(Long activityId, String photoUrl, String description, Long userId);
    Page<PhotoWall> pageQuery(Integer page, Integer size, Long activityId);
    void like(Long photoId);
}
