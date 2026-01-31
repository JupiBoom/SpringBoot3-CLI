package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.resource.CourseResourceAddRequest;
import com.rosy.main.domain.dto.resource.CourseResourceQueryRequest;
import com.rosy.main.domain.dto.resource.CourseResourceUpdateRequest;
import com.rosy.main.domain.entity.CourseResource;
import com.rosy.main.service.ICourseResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-resource")
@Tag(name = "CourseResourceController", description = "课程资源管理")
public class CourseResourceController {

    @Resource
    private ICourseResourceService courseResourceService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "上传课程资源")
    public ApiResponse addCourseResource(@RequestBody CourseResourceAddRequest resourceAddRequest) {
        CourseResource courseResource = new CourseResource();
        BeanUtils.copyProperties(resourceAddRequest, courseResource);
        boolean result = courseResourceService.save(courseResource);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(courseResource.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除课程资源")
    public ApiResponse deleteCourseResource(@RequestBody IdRequest idRequest) {
        boolean result = courseResourceService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新课程资源")
    public ApiResponse updateCourseResource(@RequestBody CourseResourceUpdateRequest resourceUpdateRequest) {
        if (resourceUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseResource courseResource = new CourseResource();
        BeanUtils.copyProperties(resourceUpdateRequest, courseResource);
        boolean result = courseResourceService.updateById(courseResource);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取课程资源")
    public ApiResponse getCourseResourceById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseResource courseResource = courseResourceService.getById(id);
        ThrowUtils.throwIf(courseResource == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(courseResource);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取课程资源列表")
    public ApiResponse listCourseResourceByPage(@RequestBody CourseResourceQueryRequest resourceQueryRequest) {
        long current = resourceQueryRequest.getCurrent();
        long size = resourceQueryRequest.getPageSize();
        Page<CourseResource> resourcePage = courseResourceService.page(new Page<>(current, size),
                courseResourceService.getQueryWrapper(resourceQueryRequest));
        return ApiResponse.success(resourcePage);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取课程的资源列表")
    public ApiResponse getResourcesByCourse(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(courseResourceService.getResourcesByCourse(courseId));
    }

    @PostMapping("/download/{id}")
    @Operation(summary = "下载资源（增加下载次数）")
    public ApiResponse downloadResource(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseResourceService.incrementDownloadCount(id);
        return ApiResponse.success(result);
    }
}
