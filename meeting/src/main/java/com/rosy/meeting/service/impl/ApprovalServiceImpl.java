package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.meeting.domain.dto.ApprovalDTO;
import com.rosy.meeting.domain.entity.ApprovalRecord;
import com.rosy.meeting.domain.entity.MeetingReservation;
import com.rosy.meeting.mapper.ApprovalRecordMapper;
import com.rosy.meeting.service.IApprovalService;
import com.rosy.meeting.service.IMeetingReservationService;
import com.rosy.meeting.service.INotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalServiceImpl implements IApprovalService {
    private final ApprovalRecordMapper approvalRecordMapper;
    private final IMeetingReservationService reservationService;
    private final INotificationService notificationService;

    public ApprovalServiceImpl(ApprovalRecordMapper approvalRecordMapper,
                              IMeetingReservationService reservationService,
                              INotificationService notificationService) {
        this.approvalRecordMapper = approvalRecordMapper;
        this.reservationService = reservationService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse approve(ApprovalDTO dto) {
        MeetingReservation reservation = reservationService.getById(dto.getReservationId());
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        
        if (reservation.getStatus() != 0) {
            return ApiResponse.error("只能审批待审批状态的预约");
        }
        
        reservation.setStatus(1);
        reservationService.updateById(reservation);
        
        ApprovalRecord record = new ApprovalRecord();
        record.setReservationId(dto.getReservationId());
        record.setApproverId(dto.getApproverId());
        record.setApproverName(dto.getApproverName());
        record.setAction(1);
        record.setReason(dto.getReason());
        approvalRecordMapper.insert(record);
        
        notificationService.sendApprovalResultNotification(reservation.getApplicantId(), 
            dto.getReservationId(), reservation.getMeetingSubject(), true, dto.getReason());
        
        return ApiResponse.success("审批通过");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse reject(ApprovalDTO dto) {
        MeetingReservation reservation = reservationService.getById(dto.getReservationId());
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        
        if (reservation.getStatus() != 0) {
            return ApiResponse.error("只能审批待审批状态的预约");
        }
        
        reservation.setStatus(2);
        reservationService.updateById(reservation);
        
        ApprovalRecord record = new ApprovalRecord();
        record.setReservationId(dto.getReservationId());
        record.setApproverId(dto.getApproverId());
        record.setApproverName(dto.getApproverName());
        record.setAction(2);
        record.setReason(dto.getReason());
        approvalRecordMapper.insert(record);
        
        notificationService.sendApprovalResultNotification(reservation.getApplicantId(), 
            dto.getReservationId(), reservation.getMeetingSubject(), false, dto.getReason());
        
        return ApiResponse.success("审批驳回");
    }

    @Override
    public ApiResponse getApprovalHistory(Long reservationId) {
        LambdaQueryWrapper<ApprovalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApprovalRecord::getReservationId, reservationId);
        wrapper.orderByDesc(ApprovalRecord::getCreatedTime);
        
        List<ApprovalRecord> records = approvalRecordMapper.selectList(wrapper);
        return ApiResponse.success(records);
    }
}