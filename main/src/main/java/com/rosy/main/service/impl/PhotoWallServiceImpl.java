package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.PhotoWallCreateRequest;
import com.rosy.main.domain.dto.PhotoWallQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.PhotoWall;
import com.rosy.main.domain.vo.PhotoWallVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.PhotoWallMapper;
import com.rosy.main.service.IPhotoWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 照片墙服务实现类
 */
@Service
public class PhotoWallServiceImpl extends ServiceImpl<PhotoWallMapper, PhotoWall> implements IPhotoWallService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public Long uploadPhoto(PhotoWallCreateRequest request, Long userId) {
        PhotoWall photo = new PhotoWall();
        BeanUtil.copyProperties(request, photo);
        photo.setUserId(userId);
        photo.setStatus(1); // 默认已通过
        photo.setLikeCount(0);
        photo.setIsFeatured(0);

        boolean saved = this.save(photo);
        if (!saved) {
            throw new BusinessException("上传照片失败");
        }
        return photo.getId();
    }

    @Override
    public boolean deletePhoto(Long id, Long userId) {
        PhotoWall photo = this.getById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }

        // 只能删除自己的照片（管理员除外）
        if (!photo.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        return this.removeById(id);
    }

    @Override
    public PhotoWallVO getPhotoById(Long id) {
        PhotoWall photo = this.getById(id);
        if (photo == null) {
            return null;
        }
        return convertToVO(photo);
    }

    @Override
    public Page<PhotoWallVO> listPhotos(PhotoWallQueryRequest request) {
        QueryWrapper<PhotoWall> queryWrapper = new QueryWrapper<>();

        // 活动筛选
        if (request.getActivityId() != null) {
            queryWrapper.eq("activity_id", request.getActivityId());
        }

        // 用户筛选
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }

        // 精选筛选
        if (request.getIsFeatured() != null) {
            queryWrapper.eq("is_featured", request.getIsFeatured());
        }

        // 状态筛选
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        } else {
            queryWrapper.eq("status", 1); // 默认只显示已通过
        }

        // 排序：先按精选，再按时间
        queryWrapper.orderByDesc("is_featured", "create_time");

        Page<PhotoWall> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper);

        List<PhotoWallVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<PhotoWallVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<PhotoWallVO> getFeaturedPhotos(int limit) {
        List<PhotoWall> list = baseMapper.selectFeaturedPhotos(limit);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean setFeatured(Long id, Integer isFeatured) {
        PhotoWall photo = this.getById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }
        photo.setIsFeatured(isFeatured);
        return this.updateById(photo);
    }

    @Override
    public boolean reviewPhoto(Long id, Integer status) {
        PhotoWall photo = this.getById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }
        photo.setStatus(status);
        return this.updateById(photo);
    }

    @Override
    public List<PhotoWallVO> getUserPhotos(Long userId) {
        QueryWrapper<PhotoWall> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("is_deleted", 0)
                .orderByDesc("create_time");

        List<PhotoWall> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PhotoWallVO> getActivityPhotos(Long activityId) {
        QueryWrapper<PhotoWall> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                .eq("status", 1)
                .eq("is_deleted", 0)
                .orderByDesc("create_time");

        List<PhotoWall> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private PhotoWallVO convertToVO(PhotoWall photo) {
        PhotoWallVO vo = new PhotoWallVO();
        BeanUtil.copyProperties(photo, vo);

        // 加载活动信息
        if (photo.getActivityId() != null) {
            Activity activity = activityMapper.selectById(photo.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        // 状态描述
        vo.setStatusDesc(photo.getStatus() == 0 ? "待审核" : (photo.getStatus() == 1 ? "已通过" : "已拒绝"));

        return vo;
    }
}
