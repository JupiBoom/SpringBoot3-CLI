package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.MeetingApprovalRequest;
import com.rosy.main.domain.entity.MeetingApproval;
import com.rosy.main.domain.vo.MeetingApprovalVO;

import java.util.List;

public interface IMeetingApprovalService extends IService<MeetingApproval> {

    Boolean approve(MeetingApprovalRequest request);

    MeetingApprovalVO getApprovalByReservationId(Long reservationId);

    List<MeetingApprovalVO> getApprovalsByApprover(Long approverId);
}
