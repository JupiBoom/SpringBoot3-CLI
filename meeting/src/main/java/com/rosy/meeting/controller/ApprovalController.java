package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.dto.ApprovalDTO;
import com.rosy.meeting.service.IApprovalService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting/approval")
public class ApprovalController {
    private final IApprovalService approvalService;

    public ApprovalController(IApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/approve")
    public ApiResponse approve(@Validated @RequestBody ApprovalDTO dto) {
        return approvalService.approve(dto);
    }

    @PostMapping("/reject")
    public ApiResponse reject(@Validated @RequestBody ApprovalDTO dto) {
        return approvalService.reject(dto);
    }

    @GetMapping("/history/{reservationId}")
    public ApiResponse getApprovalHistory(@PathVariable Long reservationId) {
        return approvalService.getApprovalHistory(reservationId);
    }
}