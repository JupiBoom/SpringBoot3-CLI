package com.rosy.volunteer.service;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.vo.ActivityPhotoVO;

import java.util.List;
import java.util.Map;

public interface IActivityPhotoService {
    Long uploadPhoto(Long activityId, String photoUrl, String thumbnailUrl, String description);
    void deletePhoto(Long id);
    void updatePhotoSort(Long activityId, List<Map<String, Object>> sortList);
    void likePhoto(Long id);
    ActivityPhotoVO getPhotoDetail(Long id);
    List<ActivityPhotoVO> getPhotoList(Long activityId, Long uploaderId);
    Object getPhotoPage(Long activityId, Long uploaderId, PageRequest pageRequest);
    List<ActivityPhotoVO> getPhotosByActivityIds(List<Long> activityIds);
}
