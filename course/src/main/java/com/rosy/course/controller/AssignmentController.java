package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.entity.Assignment;
import com.rosy.course.service.IAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 作业表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
@Tag(name = "作业管理", description = "作业发布、管理功能")
public class AssignmentController {

    private final IAssignmentService assignmentService;

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程作业列表")
    public ApiResponse listByCourse(@PathVariable Long courseId,
                                                   @RequestParam(required = false) Byte status) {
        LambdaQueryWrapper<Assignment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Assignment::getCourseId, courseId);
        
        if (status != null) {
            wrapper.eq(Assignment::getStatus, status);
        }
        
        wrapper.orderByDesc(Assignment::getCreateTime);
        
        return ApiResponse.success(assignmentService.list(wrapper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取作业详情")
    public ApiResponse getById(@PathVariable Long id) {
        Assignment assignment = assignmentService.getById(id);
        return ApiResponse.success(assignment);
    }

    @PostMapping
    @Operation(summary = "创建作业")
    public ApiResponse create(@RequestBody Assignment assignment) {
        assignment.setCreateTime(LocalDateTime.now());
        assignment.setUpdateTime(LocalDateTime.now());
        assignment.setStatus((byte) 0);
        return ApiResponse.success(assignmentService.save(assignment));
    }

    @PostMapping("/publish")
    @Operation(summary = "发布作业")
    public ApiResponse publish(@RequestBody Assignment assignment) {
        return ApiResponse.success(assignmentService.publishAssignment(assignment));
    }

    @PutMapping
    @Operation(summary = "更新作业")
    public ApiResponse update(@RequestBody Assignment assignment) {
        assignment.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(assignmentService.updateById(assignment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除作业")
    public ApiResponse delete(@PathVariable Long id) {
        return ApiResponse.success(assignmentService.removeById(id));
    }

}