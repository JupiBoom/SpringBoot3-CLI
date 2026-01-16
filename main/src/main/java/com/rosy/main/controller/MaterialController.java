package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.MaterialVO;
import com.rosy.main.service.IMaterialService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Resource
    private IMaterialService materialService;

    @PostMapping("/upload")
    public BaseResponse<Long> uploadMaterial(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String fileUrl,
            @RequestParam Long fileSize,
            @RequestParam String fileType) {
        if (courseId == null || title == null || fileUrl == null || fileSize == null || fileType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialService.uploadMaterial(courseId, title, description, fileUrl, fileSize, fileType);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(1L);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMaterial(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialService.deleteMaterial(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/course")
    public BaseResponse<List<MaterialVO>> getMaterialsByCourse(@RequestParam Long courseId) {
        if (courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<MaterialVO> materialVOs = materialService.getMaterialsByCourse(courseId);
        return ResultUtils.success(materialVOs);
    }
}
