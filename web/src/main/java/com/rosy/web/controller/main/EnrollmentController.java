package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.Enrollment;
import com.rosy.main.enums.EnrollmentStatusEnum;
import com.rosy.main.service.IEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报名管理控制器
 */
@Tag(name = "报名管理", description = "报名相关接口")
@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    @Autowired
    private IEnrollmentService enrollmentService;

    @Operation(summary = "用户报名活动")
    @PostMapping
    public ApiResponse<Boolean> enrollActivity(@RequestParam Long activityId, @RequestParam Long userId) {
        try {
            boolean result = enrollmentService.enrollActivity(activityId, userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取报名详情")
    @GetMapping("/{id}")
    public ApiResponse<Enrollment> getEnrollmentById(@Parameter(description = "报名ID") @PathVariable Long id) {
        Enrollment enrollment = enrollmentService.getById(id);
        if (enrollment != null) {
            return ApiResponse.success(enrollment);
        } else {
            return ApiResponse.error("报名记录不存在");
        }
    }

    @Operation(summary = "分页查询报名记录")
    @GetMapping("/page")
    public ApiResponse<Page<Enrollment>> getEnrollmentPage(PageRequest pageRequest,
                                                          @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
                                                          @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                          @Parameter(description = "报名状态") @RequestParam(required = false) Integer status) {
        Page<Enrollment> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<Enrollment> result = enrollmentService.getEnrollmentPage(page, activityId, userId, status);
        return ApiResponse.success(result);
    }

    @Operation(summary = "审核报名")
    @PutMapping("/{id}/audit")
    public ApiResponse<Boolean> auditEnrollment(@Parameter(description = "报名ID") @PathVariable Long id,
                                               @Parameter(description = "审核人ID") @RequestParam Long auditUserId,
                                               @Parameter(description = "审核结果") @RequestParam Integer status,
                                               @Parameter(description = "审核原因") @RequestParam(required = false) String reason) {
        try {
            boolean result = enrollmentService.auditEnrollment(id, auditUserId, status, reason);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "取消报名")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> cancelEnrollment(@Parameter(description = "报名ID") @PathVariable Long id) {
        try {
            boolean result = enrollmentService.cancelEnrollment(id);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "批量审核报名")
    @PutMapping("/batch/audit")
    public ApiResponse<Boolean> batchAuditEnrollment(@RequestBody List<Long> ids,
                                                    @RequestParam Long auditUserId,
                                                    @RequestParam Integer status,
                                                    @RequestParam(required = false) String reason) {
        try {
            boolean result = true;
            for (Long id : ids) {
                result = result && enrollmentService.auditEnrollment(id, auditUserId, status, reason);
            }
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "检查用户是否已报名活动")
    @GetMapping("/check")
    public ApiResponse<Boolean> isUserEnrolled(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean result = enrollmentService.isUserEnrolled(activityId, userId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取报名状态列表")
    @GetMapping("/statuses")
    public ApiResponse<List<EnrollmentStatusEnum>> getEnrollmentStatuses() {
        return ApiResponse.success(List.of(EnrollmentStatusEnum.values()));
    }
}