package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.entity.AssignmentSubmission;
import com.rosy.course.service.IAssignmentSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 作业提交表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
@Tag(name = "作业提交", description = "作业提交、评分功能")
public class AssignmentSubmissionController {

    private final IAssignmentSubmissionService submissionService;

    @GetMapping("/list/{assignmentId}")
    @Operation(summary = "获取作业提交列表")
    public ApiResponse listByAssignment(@PathVariable Long assignmentId,
                                                                      @RequestParam(required = false) Byte status) {
        LambdaQueryWrapper<AssignmentSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssignmentSubmission::getAssignmentId, assignmentId);
        
        if (status != null) {
            wrapper.eq(AssignmentSubmission::getStatus, status);
        }
        
        wrapper.orderByDesc(AssignmentSubmission::getSubmitTime);
        
        return ApiResponse.success(submissionService.list(wrapper));
    }

    @GetMapping("/my/{assignmentId}")
    @Operation(summary = "获取我的作业提交")
    public ApiResponse getMySubmission(@PathVariable Long assignmentId,
                                                               @RequestParam Long studentId) {
        LambdaQueryWrapper<AssignmentSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssignmentSubmission::getAssignmentId, assignmentId)
               .eq(AssignmentSubmission::getStudentId, studentId);
        
        return ApiResponse.success(submissionService.getOne(wrapper));
    }

    @PostMapping
    @Operation(summary = "提交作业")
    public ApiResponse submit(@RequestBody AssignmentSubmission submission) {
        return ApiResponse.success(submissionService.submitAssignment(submission));
    }

    @PutMapping("/grade/{id}")
    @Operation(summary = "评分作业")
    public ApiResponse grade(@PathVariable Long id,
                                       @RequestParam Integer score,
                                       @RequestParam(required = false) String evaluation,
                                       @RequestParam Long scorerId) {
        return ApiResponse.success(submissionService.gradeAssignment(id, score, evaluation, scorerId));
    }

    @PutMapping
    @Operation(summary = "更新作业提交")
    public ApiResponse update(@RequestBody AssignmentSubmission submission) {
        return ApiResponse.success(submissionService.updateById(submission));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除作业提交")
    public ApiResponse delete(@PathVariable Long id) {
        return ApiResponse.success(submissionService.removeById(id));
    }

}