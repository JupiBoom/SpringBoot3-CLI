package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.checkin.CheckInRequest;
import com.rosy.main.domain.entity.CheckIn;

import java.util.List;

public interface ICheckInService extends IService<CheckIn> {

    void checkIn(CheckInRequest request);

    void batchCheckIn(Long bookingId, List<CheckInRequest> requests);

    int countCheckedIn(Long bookingId);
}
