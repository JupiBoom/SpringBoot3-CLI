package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.student.StudentAddRequest;
import com.rosy.main.domain.dto.student.StudentQueryRequest;
import com.rosy.main.domain.dto.student.StudentUpdateRequest;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@Tag(name = "StudentController", description = "学生管理")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建学生")
    public ApiResponse addStudent(@RequestBody StudentAddRequest studentAddRequest) {
        Student student = new Student();
        BeanUtils.copyProperties(studentAddRequest, student);
        boolean result = studentService.save(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(student.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除学生")
    public ApiResponse deleteStudent(@RequestBody IdRequest idRequest) {
        boolean result = studentService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新学生")
    public ApiResponse updateStudent(@RequestBody StudentUpdateRequest studentUpdateRequest) {
        if (studentUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentUpdateRequest, student);
        boolean result = studentService.updateById(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取学生")
    public ApiResponse getStudentById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Student student = studentService.getById(id);
        ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(student);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取学生列表")
    public ApiResponse listStudentByPage(@RequestBody StudentQueryRequest studentQueryRequest) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        return ApiResponse.success(studentPage);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取课程的学生列表")
    public ApiResponse getStudentsByCourse(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(studentService.getStudentsByCourse(courseId));
    }
}
