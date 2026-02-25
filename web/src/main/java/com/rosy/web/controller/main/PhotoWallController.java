package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.PhotoWall;
import com.rosy.main.service.IPhotoWallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/photo")
@Tag(name = "照片墙")
public class PhotoWallController {

    @Resource
    private IPhotoWallService photoWallService;

    @PostMapping("/upload")
    @ValidateRequest
    @Operation(summary = "上传照片")
    public ApiResponse upload(@RequestBody PhotoUploadRequest request) {
        PhotoWall photo = photoWallService.upload(
                request.getActivityId(),
                request.getPhotoUrl(),
                request.getDescription(),
                1L);
        return ApiResponse.success(photo);
    }

    @GetMapping("/list")
    @Operation(summary = "获取照片列表")
    public ApiResponse listPhotos(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long activityId) {
        Page<PhotoWall> result = photoWallService.pageQuery(page, size, activityId);
        return ApiResponse.success(result);
    }

    @PostMapping("/like")
    @ValidateRequest
    @Operation(summary = "点赞照片")
    public ApiResponse likePhoto(@RequestBody IdRequest idRequest) {
        photoWallService.like(idRequest.getId());
        return ApiResponse.success(true);
    }

    @Data
    public static class PhotoUploadRequest {
        private Long activityId;
        private String photoUrl;
        private String description;
    }
}
