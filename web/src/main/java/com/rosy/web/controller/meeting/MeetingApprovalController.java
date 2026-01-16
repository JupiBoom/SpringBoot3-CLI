package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.LogTag;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.MeetingApprovalRequest;
import com.rosy.main.domain.vo.MeetingApprovalVO;
import com.rosy.main.service.IMeetingApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议审批管理")
@RestController
@RequestMapping("/api/meeting/approval")
public class MeetingApprovalController {

    @Resource
    private IMeetingApprovalService meetingApprovalService;

    @Operation(summary = "审批预约")
    @PostMapping("/approve")
    @LogTag
    public ApiResponse approve(@RequestBody MeetingApprovalRequest request) {
        Boolean result = meetingApprovalService.approve(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取预约审批记录")
    @GetMapping("/reservation/{reservationId}")
    public ApiResponse getApprovalByReservationId(@PathVariable Long reservationId) {
        MeetingApprovalVO approval = meetingApprovalService.getApprovalByReservationId(reservationId);
        return ApiResponse.success(approval);
    }

    @Operation(summary = "获取我的审批记录")
    @GetMapping("/my")
    public ApiResponse getMyApprovals() {
        List<MeetingApprovalVO> approvals = meetingApprovalService.getApprovalsByApprover(1L);
        return ApiResponse.success(approvals);
    }
}
