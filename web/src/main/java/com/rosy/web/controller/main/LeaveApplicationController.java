package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.leave.LeaveApplicationAddRequest;
import com.rosy.main.domain.dto.leave.LeaveApplicationQueryRequest;
import com.rosy.main.domain.dto.leave.LeaveApplicationUpdateRequest;
import com.rosy.main.domain.entity.LeaveApplication;
import com.rosy.main.service.ILeaveApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
@Tag(name = "LeaveApplicationController", description = "请假申请管理")
public class LeaveApplicationController {

    @Resource
    private ILeaveApplicationService leaveApplicationService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建请假申请")
    public ApiResponse addLeaveApplication(@RequestBody LeaveApplicationAddRequest leaveAddRequest) {
        LeaveApplication leaveApplication = new LeaveApplication();
        BeanUtils.copyProperties(leaveAddRequest, leaveApplication);
        boolean result = leaveApplicationService.save(leaveApplication);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(leaveApplication.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除请假申请")
    public ApiResponse deleteLeaveApplication(@RequestBody IdRequest idRequest) {
        boolean result = leaveApplicationService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新请假申请")
    public ApiResponse updateLeaveApplication(@RequestBody LeaveApplicationUpdateRequest leaveUpdateRequest) {
        if (leaveUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        BeanUtils.copyProperties(leaveUpdateRequest, leaveApplication);
        boolean result = leaveApplicationService.updateById(leaveApplication);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取请假申请")
    public ApiResponse getLeaveApplicationById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LeaveApplication leaveApplication = leaveApplicationService.getById(id);
        ThrowUtils.throwIf(leaveApplication == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(leaveApplication);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取请假申请列表")
    public ApiResponse listLeaveApplicationByPage(@RequestBody LeaveApplicationQueryRequest leaveQueryRequest) {
        long current = leaveQueryRequest.getCurrent();
        long size = leaveQueryRequest.getPageSize();
        Page<LeaveApplication> leavePage = leaveApplicationService.page(new Page<>(current, size),
                leaveApplicationService.getQueryWrapper(leaveQueryRequest));
        return ApiResponse.success(leavePage);
    }

    @PostMapping("/approve/{id}")
    @Operation(summary = "审批请假申请")
    public ApiResponse approveLeaveApplication(@PathVariable Long id, @RequestParam Boolean approved) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = leaveApplicationService.approveLeaveApplication(id, approved);
        return ApiResponse.success(result);
    }

    @GetMapping("/course/{courseId}/pending")
    @Operation(summary = "获取课程待审批的请假申请")
    public ApiResponse getPendingLeaveApplications(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(leaveApplicationService.getPendingLeaveApplications(courseId));
    }
}
