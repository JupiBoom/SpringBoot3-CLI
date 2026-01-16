package com.rosy.meeting.service;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.dto.ApprovalDTO;
import com.rosy.meeting.domain.entity.ApprovalRecord;

public interface IApprovalService {
    ApiResponse approve(ApprovalDTO dto);
    
    ApiResponse reject(ApprovalDTO dto);
    
    ApiResponse getApprovalHistory(Long reservationId);
}