package com.rosy.meeting.service;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.entity.CheckInRecord;

public interface ICheckInService {
    ApiResponse checkIn(Long reservationId, Long userId, String userName);
    
    ApiResponse getCheckInRecords(Long reservationId);
    
    ApiResponse getUserCheckInHistory(Long userId);
}