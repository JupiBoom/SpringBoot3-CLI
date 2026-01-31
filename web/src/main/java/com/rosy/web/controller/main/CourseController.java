package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@Tag(name = "CourseController", description = "课程管理")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建课程")
    public ApiResponse addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(courseAddRequest, course);
        boolean result = courseService.save(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(course.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除课程")
    public ApiResponse deleteCourse(@RequestBody IdRequest idRequest) {
        boolean result = courseService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新课程")
    public ApiResponse updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest) {
        if (courseUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = new Course();
        BeanUtils.copyProperties(courseUpdateRequest, course);
        boolean result = courseService.updateById(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取课程")
    public ApiResponse getCourseById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(course);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取课程列表")
    public ApiResponse listCourseByPage(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = courseService.page(new Page<>(current, size),
                courseService.getQueryWrapper(courseQueryRequest));
        return ApiResponse.success(coursePage);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "获取教师的课程列表")
    public ApiResponse getCoursesByTeacher(@PathVariable Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(courseService.getCoursesByTeacher(teacherId));
    }
}
