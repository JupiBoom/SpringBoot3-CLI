package com.rosy.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.exception.BusinessException;
import com.rosy.volunteer.domain.entity.Activity;
import com.rosy.volunteer.domain.entity.ActivityPhoto;
import com.rosy.volunteer.domain.vo.ActivityPhotoVO;
import com.rosy.volunteer.mapper.ActivityMapper;
import com.rosy.volunteer.mapper.ActivityPhotoMapper;
import com.rosy.volunteer.service.IActivityPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityPhotoServiceImpl implements IActivityPhotoService {

    private final ActivityPhotoMapper photoMapper;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadPhoto(Long activityId, String photoUrl, String thumbnailUrl, String description) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        LambdaQueryWrapper<ActivityPhoto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityPhoto::getActivityId, activityId);
        Integer maxSort = photoMapper.selectOne(wrapper.orderByDesc(ActivityPhoto::getSortOrder).last("LIMIT 1"))
                .getSortOrder();
        ActivityPhoto photo = new ActivityPhoto();
        photo.setActivityId(activityId);
        photo.setVolunteerId(1L);
        photo.setPhotoUrl(photoUrl);
        photo.setThumbnailUrl(thumbnailUrl);
        photo.setDescription(description);
        photo.setSortOrder(maxSort == null ? 1 : maxSort + 1);
        photo.setLikeCount(0);
        photo.setCreateTime(LocalDateTime.now());
        photoMapper.insert(photo);
        return photo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePhoto(Long id) {
        ActivityPhoto photo = photoMapper.selectById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }
        photoMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhotoSort(Long activityId, List<Map<String, Object>> sortList) {
        for (Map<String, Object> item : sortList) {
            Long photoId = Long.valueOf(item.get("id").toString());
            Integer sortOrder = Integer.valueOf(item.get("sortOrder").toString());
            ActivityPhoto photo = photoMapper.selectById(photoId);
            if (photo == null || !photo.getActivityId().equals(activityId)) {
                throw new BusinessException("照片不存在或不属于该活动");
            }
            photo.setSortOrder(sortOrder);
            photoMapper.updateById(photo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likePhoto(Long id) {
        ActivityPhoto photo = photoMapper.selectById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }
        photo.setLikeCount(photo.getLikeCount() + 1);
        photoMapper.updateById(photo);
    }

    @Override
    public ActivityPhotoVO getPhotoDetail(Long id) {
        ActivityPhoto photo = photoMapper.selectById(id);
        if (photo == null) {
            throw new BusinessException("照片不存在");
        }
        return convertToVO(photo);
    }

    @Override
    public List<ActivityPhotoVO> getPhotoList(Long activityId, Long uploaderId) {
        LambdaQueryWrapper<ActivityPhoto> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ActivityPhoto::getActivityId, activityId);
        }
        if (uploaderId != null) {
            wrapper.eq(ActivityPhoto::getVolunteerId, uploaderId);
        }
        wrapper.orderByAsc(ActivityPhoto::getSortOrder);
        List<ActivityPhoto> photos = photoMapper.selectList(wrapper);
        return photos.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Object getPhotoPage(Long activityId, Long uploaderId, PageRequest pageRequest) {
        Page<ActivityPhoto> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<ActivityPhoto> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ActivityPhoto::getActivityId, activityId);
        }
        if (uploaderId != null) {
            wrapper.eq(ActivityPhoto::getVolunteerId, uploaderId);
        }
        wrapper.orderByAsc(ActivityPhoto::getSortOrder);
        Page<ActivityPhoto> result = photoMapper.selectPage(page, wrapper);
        List<ActivityPhotoVO> list = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("total", result.getTotal());
        response.put("pageNum", result.getCurrent());
        response.put("pageSize", result.getSize());
        return response;
    }

    @Override
    public List<ActivityPhotoVO> getPhotosByActivityIds(List<Long> activityIds) {
        LambdaQueryWrapper<ActivityPhoto> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ActivityPhoto::getActivityId, activityIds);
        wrapper.orderByAsc(ActivityPhoto::getSortOrder);
        List<ActivityPhoto> photos = photoMapper.selectList(wrapper);
        return photos.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private ActivityPhotoVO convertToVO(ActivityPhoto photo) {
        ActivityPhotoVO vo = new ActivityPhotoVO();
        vo.setId(photo.getId());
        vo.setActivityId(photo.getActivityId());
        vo.setVolunteerId(photo.getVolunteerId());
        vo.setVolunteerName("志愿者");
        vo.setPhotoUrl(photo.getPhotoUrl());
        vo.setDescription(photo.getDescription());
        vo.setSortOrder(photo.getSortOrder());
        vo.setLikeCount(photo.getLikeCount());
        vo.setCreateTime(photo.getCreateTime());
        return vo;
    }
}
