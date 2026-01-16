package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.course.domain.entity.Course;
import com.rosy.course.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 课程表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@Tag(name = "课程管理", description = "课程信息增删改查、课程信息维护")
public class CourseController {

    private final ICourseService courseService;

    @GetMapping("/page")
    @Operation(summary = "分页查询课程列表")
    public ApiResponse page(PageRequest pageRequest, @RequestParam(required = false) String name) {
        Page<Course> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        
        if (name != null && !name.isEmpty()) {
            wrapper.like(Course::getName, name);
        }
        
        wrapper.orderByDesc(Course::getCreateTime);
        
        Page<Course> result = courseService.page(page, wrapper);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询课程详情")
    public ApiResponse getById(@PathVariable Long id) {
        Course course = courseService.getById(id);
        return ApiResponse.success(course);
    }

    @PostMapping
    @Operation(summary = "创建课程")
    public ApiResponse create(@RequestBody Course course) {
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        course.setStatus((byte) 0);
        return ApiResponse.success(courseService.save(course));
    }

    @PutMapping
    @Operation(summary = "更新课程")
    public ApiResponse update(@RequestBody Course course) {
        course.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(courseService.updateById(course));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    public ApiResponse delete(@PathVariable Long id) {
        return ApiResponse.success(courseService.removeById(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新课程状态")
    public ApiResponse updateStatus(@PathVariable Long id, @RequestParam Byte status) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(status);
        course.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(courseService.updateById(course));
    }

}