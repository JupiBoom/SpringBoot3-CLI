package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.LogTag;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.PhotoWallCreateRequest;
import com.rosy.main.domain.dto.PhotoWallQueryRequest;
import com.rosy.main.domain.vo.PhotoWallVO;
import com.rosy.main.service.IPhotoWallService;
import com.rosy.web.controller.main.utils.UserHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 照片墙控制器
 */
@RestController
@RequestMapping("/photo-wall")
@Slf4j
public class PhotoWallController {

    @Autowired
    private IPhotoWallService photoWallService;

    /**
     * 上传照片
     */
    @PostMapping("/upload")
    @ValidateRequest
    @LogTag(value = "上传照片", printResult = true)
    public ApiResponse uploadPhoto(@Valid @RequestBody PhotoWallCreateRequest request) {
        Long userId = UserHolder.getUserId();
        Long photoId = photoWallService.uploadPhoto(request, userId);
        return ApiResponse.success(photoId);
    }

    /**
     * 删除照片
     */
    @PostMapping("/delete")
    @ValidateRequest
    @LogTag(value = "删除照片", printResult = true)
    public ApiResponse deletePhoto(@Valid @RequestBody IdRequest request) {
        Long userId = UserHolder.getUserId();
        boolean result = photoWallService.deletePhoto(request.getId(), userId);
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取照片
     */
    @GetMapping("/get/{id}")
    @LogTag(value = "获取照片")
    public ApiResponse getPhotoById(@PathVariable("id") Long id) {
        PhotoWallVO vo = photoWallService.getPhotoById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 分页查询照片列表
     */
    @PostMapping("/list/page")
    @LogTag(value = "分页查询照片列表")
    public ApiResponse listPhotosByPage(@RequestBody PhotoWallQueryRequest request) {
        Page<PhotoWallVO> page = photoWallService.listPhotos(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取精选照片
     */
    @GetMapping("/featured")
    @LogTag(value = "获取精选照片")
    public ApiResponse getFeaturedPhotos(@RequestParam(defaultValue = "10") int limit) {
        List<PhotoWallVO> list = photoWallService.getFeaturedPhotos(limit);
        return ApiResponse.success(list);
    }

    /**
     * 获取我的照片列表
     */
    @GetMapping("/my-list")
    @LogTag(value = "获取我的照片列表")
    public ApiResponse getMyPhotos() {
        Long userId = UserHolder.getUserId();
        List<PhotoWallVO> list = photoWallService.getUserPhotos(userId);
        return ApiResponse.success(list);
    }

    /**
     * 获取活动的照片列表
     */
    @GetMapping("/activity/{activityId}")
    @LogTag(value = "获取活动照片列表")
    public ApiResponse getActivityPhotos(@PathVariable("activityId") Long activityId) {
        List<PhotoWallVO> list = photoWallService.getActivityPhotos(activityId);
        return ApiResponse.success(list);
    }
}
