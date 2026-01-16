package com.rosy.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.meeting.domain.dto.MeetingReservationDTO;
import com.rosy.meeting.domain.entity.MeetingReservation;
import com.rosy.common.domain.entity.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IMeetingReservationService extends IService<MeetingReservation> {
    ApiResponse createReservation(MeetingReservationDTO dto);
    
    ApiResponse updateReservation(MeetingReservationDTO dto);
    
    ApiResponse cancelReservation(Long id, Long userId);
    
    ApiResponse getReservationById(Long id);
    
    ApiResponse getReservationsByUser(Long userId);
    
    ApiResponse getReservationsByRoom(Long roomId, LocalDateTime startDate, LocalDateTime endDate);
    
    ApiResponse getAllPendingReservations();
    
    ApiResponse getTodayReservations(Long roomId);
    
    boolean checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeReservationId);
    
    List<MeetingReservation> findConflictingReservations(Long roomId, LocalDateTime startTime, LocalDateTime endTime);
}