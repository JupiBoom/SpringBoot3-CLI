package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.meeting.domain.entity.CheckInRecord;
import com.rosy.meeting.domain.entity.MeetingReservation;
import com.rosy.meeting.mapper.CheckInRecordMapper;
import com.rosy.meeting.service.ICheckInService;
import com.rosy.meeting.service.IMeetingReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInServiceImpl implements ICheckInService {
    private final CheckInRecordMapper checkInRecordMapper;
    private final IMeetingReservationService reservationService;

    public CheckInServiceImpl(CheckInRecordMapper checkInRecordMapper,
                              IMeetingReservationService reservationService) {
        this.checkInRecordMapper = checkInRecordMapper;
        this.reservationService = reservationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse checkIn(Long reservationId, Long userId, String userName) {
        MeetingReservation reservation = reservationService.getById(reservationId);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        
        if (reservation.getStatus() != 1) {
            return ApiResponse.error("只有已通过的会议才能签到");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(reservation.getStartTime())) {
            return ApiResponse.error("会议尚未开始，不能签到");
        }
        
        if (now.isAfter(reservation.getEndTime())) {
            return ApiResponse.error("会议已结束，无法签到");
        }
        
        LambdaQueryWrapper<CheckInRecord> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(CheckInRecord::getReservationId, reservationId);
        checkWrapper.eq(CheckInRecord::getUserId, userId);
        CheckInRecord existing = checkInRecordMapper.selectOne(checkWrapper);
        if (existing != null) {
            return ApiResponse.error("您已签到过");
        }
        
        CheckInRecord record = new CheckInRecord();
        record.setReservationId(reservationId);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setCheckInType(now.isAfter(reservation.getStartTime().plusMinutes(15)) ? 2 : 1);
        
        checkInRecordMapper.insert(record);
        
        return ApiResponse.success("签到成功", record);
    }

    @Override
    public ApiResponse getCheckInRecords(Long reservationId) {
        LambdaQueryWrapper<CheckInRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckInRecord::getReservationId, reservationId);
        wrapper.orderByAsc(CheckInRecord::getCheckInTime);
        
        List<CheckInRecord> records = checkInRecordMapper.selectList(wrapper);
        return ApiResponse.success(records);
    }

    @Override
    public ApiResponse getUserCheckInHistory(Long userId) {
        LambdaQueryWrapper<CheckInRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckInRecord::getUserId, userId);
        wrapper.orderByDesc(CheckInRecord::getCheckInTime);
        
        List<CheckInRecord> records = checkInRecordMapper.selectList(wrapper);
        return ApiResponse.success(records);
    }
}