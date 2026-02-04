package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.PhotoWallCreateRequest;
import com.rosy.main.domain.dto.PhotoWallQueryRequest;
import com.rosy.main.domain.entity.PhotoWall;
import com.rosy.main.domain.vo.PhotoWallVO;

import java.util.List;

/**
 * 照片墙服务接口
 */
public interface IPhotoWallService extends IService<PhotoWall> {

    /**
     * 上传照片
     */
    Long uploadPhoto(PhotoWallCreateRequest request, Long userId);

    /**
     * 删除照片
     */
    boolean deletePhoto(Long id, Long userId);

    /**
     * 根据ID获取照片
     */
    PhotoWallVO getPhotoById(Long id);

    /**
     * 分页查询照片列表
     */
    Page<PhotoWallVO> listPhotos(PhotoWallQueryRequest request);

    /**
     * 获取精选照片
     */
    List<PhotoWallVO> getFeaturedPhotos(int limit);

    /**
     * 设置精选
     */
    boolean setFeatured(Long id, Integer isFeatured);

    /**
     * 审核照片
     */
    boolean reviewPhoto(Long id, Integer status);

    /**
     * 获取用户的照片列表
     */
    List<PhotoWallVO> getUserPhotos(Long userId);

    /**
     * 获取活动的照片列表
     */
    List<PhotoWallVO> getActivityPhotos(Long activityId);
}
