package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.dto.MeetingCheckinRequest;
import com.rosy.main.domain.vo.MeetingCheckinVO;

import java.util.List;

public interface IMeetingCheckinService extends IService<MeetingCheckin> {

    Boolean checkin(MeetingCheckinRequest request);

    List<MeetingCheckinVO> getCheckinsByReservation(Long reservationId);

    List<MeetingCheckinVO> getCheckinsByUser(Long userId);
}
