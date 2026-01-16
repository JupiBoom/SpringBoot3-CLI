package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.MeetingApprovalRequest;
import com.rosy.main.domain.entity.MeetingApproval;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.enums.ApprovalStatusEnum;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.domain.vo.MeetingApprovalVO;
import com.rosy.main.mapper.MeetingApprovalMapper;
import com.rosy.main.service.IMeetingApprovalService;
import com.rosy.main.service.IMeetingNotificationService;
import com.rosy.main.service.IMeetingReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingApprovalServiceImpl extends ServiceImpl<MeetingApprovalMapper, MeetingApproval> implements IMeetingApprovalService {

    private final IMeetingReservationService meetingReservationService;
    private final IMeetingNotificationService meetingNotificationService;

    public MeetingApprovalServiceImpl(IMeetingReservationService meetingReservationService, IMeetingNotificationService meetingNotificationService) {
        this.meetingReservationService = meetingReservationService;
        this.meetingNotificationService = meetingNotificationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approve(MeetingApprovalRequest request) {
        MeetingReservation reservation = meetingReservationService.getById(request.getReservationId());
        if (reservation == null) {
            throw new BusinessException(40006, "预约不存在");
        }

        if (!reservation.getStatus().equals(ReservationStatusEnum.PENDING.getCode().byteValue())) {
            throw new BusinessException(40007, "该预约不是待审批状态");
        }

        MeetingApproval approval = new MeetingApproval();
        approval.setReservationId(request.getReservationId());
        approval.setApproverId(1L);
        approval.setApprovalStatus(request.getApprovalStatus());
        approval.setApprovalComment(request.getApprovalComment());
        approval.setApprovalTime(LocalDateTime.now());
        this.save(approval);

        if (request.getApprovalStatus().equals(ApprovalStatusEnum.APPROVED.getCode().byteValue())) {
            reservation.setStatus(ReservationStatusEnum.APPROVED.getCode().byteValue());
            meetingNotificationService.sendApprovalNotification(reservation.getId(), reservation.getApplicantId(), true, request.getApprovalComment());
        } else {
            reservation.setStatus(ReservationStatusEnum.REJECTED.getCode().byteValue());
            meetingNotificationService.sendApprovalNotification(reservation.getId(), reservation.getApplicantId(), false, request.getApprovalComment());
        }

        meetingReservationService.updateById(reservation);
        return true;
    }

    @Override
    public MeetingApprovalVO getApprovalByReservationId(Long reservationId) {
        QueryWrapper<MeetingApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("reservation_id", reservationId);
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 1");
        MeetingApproval approval = this.getOne(wrapper);
        if (approval == null) {
            return null;
        }
        MeetingApprovalVO vo = BeanUtil.copyProperties(approval, MeetingApprovalVO.class);
        vo.setApprovalStatusDesc(ApprovalStatusEnum.getDescByCode(approval.getApprovalStatus().intValue()));
        return vo;
    }

    @Override
    public List<MeetingApprovalVO> getApprovalsByApprover(Long approverId) {
        QueryWrapper<MeetingApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("approver_id", approverId);
        wrapper.orderByDesc("approval_time");
        List<MeetingApproval> approvals = this.list(wrapper);
        return approvals.stream()
                .map(approval -> {
                    MeetingApprovalVO vo = BeanUtil.copyProperties(approval, MeetingApprovalVO.class);
                    vo.setApprovalStatusDesc(ApprovalStatusEnum.getDescByCode(approval.getApprovalStatus().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
