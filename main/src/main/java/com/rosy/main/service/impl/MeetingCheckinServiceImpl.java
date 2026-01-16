package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.MeetingCheckinRequest;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.mapper.MeetingCheckinMapper;
import com.rosy.main.service.IMeetingCheckinService;
import com.rosy.main.service.IMeetingReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingCheckinServiceImpl extends ServiceImpl<MeetingCheckinMapper, MeetingCheckin> implements IMeetingCheckinService {

    private final IMeetingReservationService meetingReservationService;

    public MeetingCheckinServiceImpl(IMeetingReservationService meetingReservationService) {
        this.meetingReservationService = meetingReservationService;
    }

    @Override
    public Boolean checkin(MeetingCheckinRequest request) {
        MeetingReservation reservation = meetingReservationService.getById(request.getReservationId());
        if (reservation == null) {
            throw new BusinessException(40008, "预约不存在");
        }

        if (!reservation.getStatus().equals(ReservationStatusEnum.APPROVED.getCode().byteValue())) {
            throw new BusinessException(40009, "该预约未通过审批");
        }

        LocalDateTime now = request.getCheckinTime() != null ? request.getCheckinTime() : LocalDateTime.now();

        if (now.isBefore(reservation.getStartTime())) {
            throw new BusinessException(40010, "会议尚未开始");
        }

        if (now.isAfter(reservation.getEndTime())) {
            throw new BusinessException(40011, "会议已结束");
        }

        QueryWrapper<MeetingCheckin> wrapper = new QueryWrapper<>();
        wrapper.eq("reservation_id", request.getReservationId());
        wrapper.eq("user_id", 1L);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(40012, "已经签到过了");
        }

        MeetingCheckin checkin = new MeetingCheckin();
        checkin.setReservationId(request.getReservationId());
        checkin.setUserId(1L);
        checkin.setCheckinTime(now);

        long minutesLate = ChronoUnit.MINUTES.between(reservation.getStartTime(), now);
        if (minutesLate > 5) {
            checkin.setCheckinType((byte) 2);
        } else {
            checkin.setCheckinType((byte) 1);
        }

        return this.save(checkin);
    }

    @Override
    public List<MeetingCheckinVO> getCheckinsByReservation(Long reservationId) {
        QueryWrapper<MeetingCheckin> wrapper = new QueryWrapper<>();
        wrapper.eq("reservation_id", reservationId);
        wrapper.orderByAsc("checkin_time");
        List<MeetingCheckin> checkins = this.list(wrapper);
        return checkins.stream()
                .map(checkin -> {
                    MeetingCheckinVO vo = BeanUtil.copyProperties(checkin, MeetingCheckinVO.class);
                    vo.setCheckinTypeDesc(checkin.getCheckinType() == 1 ? "正常签到" : "迟到");
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingCheckinVO> getCheckinsByUser(Long userId) {
        QueryWrapper<MeetingCheckin> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("checkin_time");
        List<MeetingCheckin> checkins = this.list(wrapper);
        return checkins.stream()
                .map(checkin -> {
                    MeetingCheckinVO vo = BeanUtil.copyProperties(checkin, MeetingCheckinVO.class);
                    vo.setCheckinTypeDesc(checkin.getCheckinType() == 1 ? "正常签到" : "迟到");
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
