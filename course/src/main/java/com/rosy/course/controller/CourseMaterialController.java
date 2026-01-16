package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.entity.CourseMaterial;
import com.rosy.course.service.ICourseMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程资料表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/material")
@RequiredArgsConstructor
@Tag(name = "资料管理", description = "课程资料上传、下载功能")
public class CourseMaterialController {

    private final ICourseMaterialService materialService;

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程资料列表")
    public ApiResponse listByCourse(@PathVariable Long courseId,
                                                       @RequestParam(required = false) String fileType,
                                                       @RequestParam(required = false) Byte status) {
        LambdaQueryWrapper<CourseMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseMaterial::getCourseId, courseId);
        
        if (fileType != null) {
            wrapper.eq(CourseMaterial::getFileType, fileType);
        }
        
        if (status != null) {
            wrapper.eq(CourseMaterial::getStatus, status);
        }
        
        wrapper.orderByDesc(CourseMaterial::getCreateTime);
        
        return ApiResponse.success(materialService.list(wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取资料详情")
    public ApiResponse getById(@PathVariable Long id) {
        CourseMaterial material = materialService.getById(id);
        return ApiResponse.success(material);
    }

    @PostMapping
    @Operation(summary = "上传资料")
    public ApiResponse upload(@RequestBody CourseMaterial material) {
        material.setCreateTime(LocalDateTime.now());
        material.setUpdateTime(LocalDateTime.now());
        material.setDownloadCount(0);
        material.setStatus((byte) 1);
        return ApiResponse.success(materialService.save(material));
    }

    @PutMapping
    @Operation(summary = "更新资料")
    public ApiResponse update(@RequestBody CourseMaterial material) {
        material.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(materialService.updateById(material));
    }

    @PutMapping("/download/{id}")
    @Operation(summary = "下载资料")
    public ApiResponse download(@PathVariable Long id) {
        return ApiResponse.success(materialService.downloadMaterial(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资料")
    public ApiResponse delete(@PathVariable Long id) {
        return ApiResponse.success(materialService.removeById(id));
    }

}