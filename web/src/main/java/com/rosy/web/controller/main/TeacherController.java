package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.teacher.TeacherAddRequest;
import com.rosy.main.domain.dto.teacher.TeacherQueryRequest;
import com.rosy.main.domain.dto.teacher.TeacherUpdateRequest;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.service.ITeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
@Tag(name = "TeacherController", description = "教师管理")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建教师")
    public ApiResponse addTeacher(@RequestBody TeacherAddRequest teacherAddRequest) {
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherAddRequest, teacher);
        boolean result = teacherService.save(teacher);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(teacher.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除教师")
    public ApiResponse deleteTeacher(@RequestBody IdRequest idRequest) {
        boolean result = teacherService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新教师")
    public ApiResponse updateTeacher(@RequestBody TeacherUpdateRequest teacherUpdateRequest) {
        if (teacherUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherUpdateRequest, teacher);
        boolean result = teacherService.updateById(teacher);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取教师")
    public ApiResponse getTeacherById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Teacher teacher = teacherService.getById(id);
        ThrowUtils.throwIf(teacher == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(teacher);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取教师列表")
    public ApiResponse listTeacherByPage(@RequestBody TeacherQueryRequest teacherQueryRequest) {
        long current = teacherQueryRequest.getCurrent();
        long size = teacherQueryRequest.getPageSize();
        Page<Teacher> teacherPage = teacherService.page(new Page<>(current, size),
                teacherService.getQueryWrapper(teacherQueryRequest));
        return ApiResponse.success(teacherPage);
    }

    @GetMapping("/statistics/{teacherId}")
    @Operation(summary = "获取教师上课统计")
    public ApiResponse getTeacherStatistics(@PathVariable Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(teacherService.getTeacherCourseCount(teacherId));
    }
}
