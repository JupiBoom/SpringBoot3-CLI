package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.BookingStatusEnum;
import com.rosy.common.enums.CheckInStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.checkin.CheckInRequest;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.CheckIn;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.mapper.CheckInMapper;
import com.rosy.main.service.ICheckInService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {

    @Resource
    private BookingMapper bookingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(CheckInRequest request) {
        Booking booking = bookingMapper.selectById(request.getBookingId());
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }
        if (booking.getStatus() != BookingStatusEnum.APPROVED.getCode().byteValue()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "预约未通过审批，无法签到");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime().minusMinutes(30))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到时间未到");
        }
        if (now.isAfter(booking.getEndTime())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "会议已结束，无法签到");
        }

        LambdaQueryWrapper<CheckIn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CheckIn::getBookingId, request.getBookingId())
                .eq(CheckIn::getUserId, request.getUserId());
        CheckIn existingCheckIn = this.getOne(queryWrapper);

        if (existingCheckIn != null) {
            if (existingCheckIn.getCheckInStatus() == CheckInStatusEnum.CHECKED_IN.getCode().byteValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签到，请勿重复签到");
            }
            existingCheckIn.setCheckInStatus(CheckInStatusEnum.CHECKED_IN.getCode().byteValue());
            existingCheckIn.setCheckInTime(now);
            this.updateById(existingCheckIn);
        } else {
            CheckIn checkIn = new CheckIn();
            checkIn.setBookingId(request.getBookingId());
            checkIn.setUserId(request.getUserId());
            checkIn.setUserName(request.getUserName());
            checkIn.setCheckInStatus(CheckInStatusEnum.CHECKED_IN.getCode().byteValue());
            checkIn.setCheckInTime(now);
            this.save(checkIn);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCheckIn(Long bookingId, List<CheckInRequest> requests) {
        for (CheckInRequest request : requests) {
            request.setBookingId(bookingId);
            checkIn(request);
        }
    }

    @Override
    public int countCheckedIn(Long bookingId) {
        LambdaQueryWrapper<CheckIn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CheckIn::getBookingId, bookingId)
                .eq(CheckIn::getCheckInStatus, CheckInStatusEnum.CHECKED_IN.getCode());
        return (int) this.count(queryWrapper);
    }
}
