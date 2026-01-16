package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.MeetingReservationAddRequest;
import com.rosy.main.domain.dto.MeetingReservationUpdateRequest;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.vo.MeetingReservationVO;

import java.util.List;

public interface IMeetingReservationService extends IService<MeetingReservation> {

    Long addReservation(MeetingReservationAddRequest request);

    Boolean updateReservation(MeetingReservationUpdateRequest request);

    Boolean cancelReservation(Long id);

    MeetingReservationVO getReservationById(Long id);

    List<MeetingReservationVO> getReservationsByApplicant(Long applicantId);

    List<MeetingReservationVO> getReservationsByRoom(Long roomId);

    List<MeetingReservationVO> getPendingApprovals();

    Boolean checkConflict(Long roomId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, Long excludeReservationId);
}
