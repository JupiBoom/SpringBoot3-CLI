package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.service.ILeaveRequestService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestController {

    @Resource
    private ILeaveRequestService leaveRequestService;

    @PostMapping("/submit")
    public BaseResponse<Boolean> submitLeaveRequest(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String reason) {
        if (studentId == null || courseId == null || startDate == null || endDate == null || reason == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        boolean result = leaveRequestService.submitLeaveRequest(studentId, courseId, start, end, reason);
        return ResultUtils.success(result);
    }

    @PostMapping("/approve")
    public BaseResponse<Boolean> approveLeaveRequest(
            @RequestParam Long requestId,
            @RequestParam Long approverId,
            @RequestParam Byte status,
            @RequestParam(required = false) String remark) {
        if (requestId == null || approverId == null || status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = leaveRequestService.approveLeaveRequest(requestId, approverId, status, remark);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/pending")
    public BaseResponse<List<LeaveRequestVO>> getPendingRequests(@RequestParam(required = false) Long courseId) {
        List<LeaveRequestVO> leaveRequestVOs = leaveRequestService.getPendingRequests(courseId);
        return ResultUtils.success(leaveRequestVOs);
    }

    @GetMapping("/list/student")
    public BaseResponse<List<LeaveRequestVO>> getStudentLeaveRequests(@RequestParam Long studentId) {
        if (studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LeaveRequestVO> leaveRequestVOs = leaveRequestService.getStudentLeaveRequests(studentId);
        return ResultUtils.success(leaveRequestVOs);
    }
}
