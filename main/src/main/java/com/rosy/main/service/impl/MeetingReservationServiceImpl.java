package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.MeetingReservationAddRequest;
import com.rosy.main.domain.dto.MeetingReservationUpdateRequest;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.domain.vo.MeetingReservationVO;
import com.rosy.main.mapper.MeetingReservationMapper;
import com.rosy.main.service.IMeetingReservationService;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingReservationServiceImpl extends ServiceImpl<MeetingReservationMapper, MeetingReservation> implements IMeetingReservationService {

    private final IMeetingRoomService meetingRoomService;

    public MeetingReservationServiceImpl(IMeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @Override
    public Long addReservation(MeetingReservationAddRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException(40001, "开始时间不能晚于结束时间");
        }

        if (checkConflict(request.getRoomId(), request.getStartTime(), request.getEndTime(), null)) {
            throw new BusinessException(40002, "该时间段已被预约");
        }

        MeetingReservation reservation = new MeetingReservation();
        reservation.setRoomId(request.getRoomId());
        reservation.setTitle(request.getTitle());
        reservation.setReason(request.getReason());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setApplicantId(1L);
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode().byteValue());
        reservation.setAttendees(request.getAttendees());
        this.save(reservation);
        return reservation.getId();
    }

    @Override
    public Boolean updateReservation(MeetingReservationUpdateRequest request) {
        MeetingReservation reservation = this.getById(request.getId());
        if (reservation == null) {
            return false;
        }

        if (!reservation.getStatus().equals(ReservationStatusEnum.PENDING.getCode().byteValue())) {
            throw new BusinessException(40003, "只能修改待审批的预约");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException(40001, "开始时间不能晚于结束时间");
        }

        if (checkConflict(request.getRoomId(), request.getStartTime(), request.getEndTime(), request.getId())) {
            throw new BusinessException(40002, "该时间段已被预约");
        }

        reservation.setRoomId(request.getRoomId());
        reservation.setTitle(request.getTitle());
        reservation.setReason(request.getReason());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setAttendees(request.getAttendees());
        return this.updateById(reservation);
    }

    @Override
    public Boolean cancelReservation(Long id) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            return false;
        }

        if (reservation.getStatus().equals(ReservationStatusEnum.CANCELLED.getCode().byteValue())) {
            throw new BusinessException(40004, "该预约已取消");
        }

        if (reservation.getStatus().equals(ReservationStatusEnum.REJECTED.getCode().byteValue())) {
            throw new BusinessException(40005, "已驳回的预约不能取消");
        }

        reservation.setStatus(ReservationStatusEnum.CANCELLED.getCode().byteValue());
        return this.updateById(reservation);
    }

    @Override
    public MeetingReservationVO getReservationById(Long id) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            return null;
        }
        MeetingReservationVO vo = BeanUtil.copyProperties(reservation, MeetingReservationVO.class);
        vo.setStatusDesc(ReservationStatusEnum.getDescByCode(reservation.getStatus().intValue()));
        return vo;
    }

    @Override
    public List<MeetingReservationVO> getReservationsByApplicant(Long applicantId) {
        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("applicant_id", applicantId);
        wrapper.orderByDesc("create_time");
        List<MeetingReservation> reservations = this.list(wrapper);
        return reservations.stream()
                .map(reservation -> {
                    MeetingReservationVO vo = BeanUtil.copyProperties(reservation, MeetingReservationVO.class);
                    vo.setStatusDesc(ReservationStatusEnum.getDescByCode(reservation.getStatus().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingReservationVO> getReservationsByRoom(Long roomId) {
        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id", roomId);
        wrapper.orderByDesc("create_time");
        List<MeetingReservation> reservations = this.list(wrapper);
        return reservations.stream()
                .map(reservation -> {
                    MeetingReservationVO vo = BeanUtil.copyProperties(reservation, MeetingReservationVO.class);
                    vo.setStatusDesc(ReservationStatusEnum.getDescByCode(reservation.getStatus().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingReservationVO> getPendingApprovals() {
        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("status", ReservationStatusEnum.PENDING.getCode());
        wrapper.orderByDesc("create_time");
        List<MeetingReservation> reservations = this.list(wrapper);
        return reservations.stream()
                .map(reservation -> {
                    MeetingReservationVO vo = BeanUtil.copyProperties(reservation, MeetingReservationVO.class);
                    vo.setStatusDesc(ReservationStatusEnum.getDescByCode(reservation.getStatus().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean checkConflict(Long roomId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, Long excludeReservationId) {
        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id", roomId);
        wrapper.in("status", ReservationStatusEnum.PENDING.getCode(), ReservationStatusEnum.APPROVED.getCode());
        wrapper.and(w -> w
                .and(w1 -> w1.lt("start_time", startTime).gt("end_time", startTime))
                .or()
                .and(w2 -> w2.lt("start_time", endTime).gt("end_time", endTime))
                .or()
                .and(w3 -> w3.ge("start_time", startTime).le("end_time", endTime))
        );

        if (excludeReservationId != null) {
            wrapper.ne("id", excludeReservationId);
        }

        return this.count(wrapper) > 0;
    }
}
