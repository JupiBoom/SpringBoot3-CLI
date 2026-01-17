package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.ActivityPhoto;
import com.rosy.main.service.IActivityPhotoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity-photo")
public class ActivityPhotoController {

    @Resource
    private IActivityPhotoService activityPhotoService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addPhoto(@RequestParam Long activityId, @RequestParam String photoUrl, @RequestParam Long uploadUserId, @RequestParam(required = false) Long forumPostId, @RequestParam(required = false) String description) {
        ActivityPhoto activityPhoto = new ActivityPhoto();
        activityPhoto.setActivityId(activityId);
        activityPhoto.setPhotoUrl(photoUrl);
        activityPhoto.setUploadUserId(uploadUserId);
        activityPhoto.setForumPostId(forumPostId);
        activityPhoto.setDescription(description);
        boolean result = activityPhotoService.save(activityPhoto);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(activityPhoto.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deletePhoto(@RequestBody IdRequest idRequest) {
        boolean result = activityPhotoService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    public ApiResponse listPhotos(@RequestParam Long activityId, @RequestParam(required = false) Long forumPostId) {
        var queryWrapper = activityPhotoService.getQueryWrapper(activityId, forumPostId);
        return ApiResponse.success(activityPhotoService.list(queryWrapper));
    }

    @GetMapping("/list/page")
    public ApiResponse listPhotosByPage(@RequestParam Long activityId, @RequestParam(required = false) Long forumPostId, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        var queryWrapper = activityPhotoService.getQueryWrapper(activityId, forumPostId);
        Page<ActivityPhoto> page = activityPhotoService.page(new Page<>(current, size), queryWrapper);
        return ApiResponse.success(page);
    }
}
