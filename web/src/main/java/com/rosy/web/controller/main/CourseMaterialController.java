package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.CourseMaterial;
import com.rosy.main.service.ICourseMaterialService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程资料管理前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/material")
public class CourseMaterialController {
    @Resource
    ICourseMaterialService courseMaterialService;

    /**
     * 上传课程资料
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addMaterial(@RequestBody CourseMaterial material) {
        ThrowUtils.throwIf(material.getCourseId() == null || material.getCourseId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(material.getTitle() == null || material.getTitle().trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(material.getFileUrl() == null || material.getFileUrl().trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        boolean result = courseMaterialService.save(material);
        return ApiResponse.success(result);
    }

    /**
     * 删除课程资料
     */
    @PostMapping("/delete")
    public ApiResponse deleteMaterial(@RequestBody IdRequest request) {
        ThrowUtils.throwIf(request.getId() == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = courseMaterialService.removeById(request.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新课程资料
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateMaterial(@RequestBody CourseMaterial material) {
        if (material.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseMaterialService.updateById(material);
        return ApiResponse.success(result);
    }

    /**
     * 获取资料详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse getMaterialById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        CourseMaterial material = courseMaterialService.getById(id);
        ThrowUtils.throwIf(material == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(material);
    }

    /**
     * 获取课程资料列表
     */
    @GetMapping("/list/course/{courseId}")
    public ApiResponse getCourseMaterials(@PathVariable Long courseId) {
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        List<CourseMaterial> materials = courseMaterialService.lambdaQuery()
                .eq(CourseMaterial::getCourseId, courseId)
                .orderByDesc(CourseMaterial::getCreateTime)
                .list();
        return ApiResponse.success(materials);
    }

    /**
     * 获取教师上传的资料列表
     */
    @GetMapping("/list/teacher/{creatorId}")
    public ApiResponse getTeacherMaterials(@PathVariable Long creatorId) {
        ThrowUtils.throwIf(creatorId == null || creatorId <= 0, ErrorCode.PARAMS_ERROR);
        List<CourseMaterial> materials = courseMaterialService.lambdaQuery()
                .eq(CourseMaterial::getCreatorId, creatorId)
                .orderByDesc(CourseMaterial::getCreateTime)
                .list();
        return ApiResponse.success(materials);
    }
}
