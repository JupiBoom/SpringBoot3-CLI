package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.meeting.domain.dto.MeetingReservationDTO;
import com.rosy.meeting.domain.entity.MeetingReservation;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.domain.entity.Notification;
import com.rosy.meeting.mapper.MeetingReservationMapper;
import com.rosy.meeting.service.IMeetingReservationService;
import com.rosy.meeting.service.INotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingReservationServiceImpl extends ServiceImpl<MeetingReservationMapper, MeetingReservation> implements IMeetingReservationService {
    private final INotificationService notificationService;

    public MeetingReservationServiceImpl(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createReservation(MeetingReservationDTO dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            return ApiResponse.error("开始时间不能晚于结束时间");
        }
        
        if (dto.getStartTime().isBefore(LocalDateTime.now())) {
            return ApiResponse.error("不能预约过去的时间");
        }
        
        if (checkConflict(dto.getRoomId(), dto.getStartTime(), dto.getEndTime(), null)) {
            return ApiResponse.error("该时间段会议室已被预约，请选择其他时间");
        }
        
        MeetingReservation reservation = new MeetingReservation();
        BeanUtils.copyProperties(dto, reservation);
        reservation.setStatus(0);
        
        boolean saved = this.save(reservation);
        if (saved) {
            notificationService.sendApprovalNotification(reservation.getApplicantId(), 
                reservation.getId(), reservation.getMeetingSubject());
            return ApiResponse.success("预约申请提交成功，等待审批", reservation);
        }
        return ApiResponse.error("预约申请提交失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateReservation(MeetingReservationDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("预约ID不能为空");
        }
        
        MeetingReservation existingReservation = this.getById(dto.getId());
        if (existingReservation == null) {
            throw new BusinessException("预约不存在");
        }
        
        if (existingReservation.getStatus() != 0) {
            return ApiResponse.error("只能修改待审批状态的预约");
        }
        
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            return ApiResponse.error("开始时间不能晚于结束时间");
        }
        
        if (checkConflict(dto.getRoomId(), dto.getStartTime(), dto.getEndTime(), dto.getId())) {
            return ApiResponse.error("该时间段会议室已被预约，请选择其他时间");
        }
        
        MeetingReservation reservation = new MeetingReservation();
        BeanUtils.copyProperties(dto, reservation);
        
        boolean updated = this.updateById(reservation);
        if (updated) {
            return ApiResponse.success("预约更新成功", reservation);
        }
        return ApiResponse.error("预约更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse cancelReservation(Long id, Long userId) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        
        if (!reservation.getApplicantId().equals(userId)) {
            return ApiResponse.error("只能取消自己的预约");
        }
        
        if (reservation.getStatus() == 4) {
            return ApiResponse.error("已完成的会议不能取消");
        }
        
        reservation.setStatus(3);
        boolean updated = this.updateById(reservation);
        if (updated) {
            return ApiResponse.success("预约已取消");
        }
        return ApiResponse.error("取消失败");
    }

    @Override
    public ApiResponse getReservationById(Long id) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        return ApiResponse.success(reservation);
    }

    @Override
    public ApiResponse getReservationsByUser(Long userId) {
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getApplicantId, userId);
        wrapper.orderByDesc(MeetingReservation::getCreatedTime);
        
        List<MeetingReservation> reservations = this.list(wrapper);
        return ApiResponse.success(reservations);
    }

    @Override
    public ApiResponse getReservationsByRoom(Long roomId, LocalDateTime startDate, LocalDateTime endDate) {
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getRoomId, roomId);
        if (startDate != null && endDate != null) {
            wrapper.between(MeetingReservation::getStartTime, startDate, endDate);
        }
        wrapper.orderByAsc(MeetingReservation::getStartTime);
        
        List<MeetingReservation> reservations = this.list(wrapper);
        return ApiResponse.success(reservations);
    }

    @Override
    public ApiResponse getAllPendingReservations() {
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getStatus, 0);
        wrapper.orderByAsc(MeetingReservation::getStartTime);
        
        List<MeetingReservation> reservations = this.list(wrapper);
        return ApiResponse.success(reservations);
    }

    @Override
    public ApiResponse getTodayReservations(Long roomId) {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getRoomId, roomId);
        wrapper.ge(MeetingReservation::getStartTime, todayStart);
        wrapper.le(MeetingReservation::getEndTime, todayEnd);
        wrapper.orderByAsc(MeetingReservation::getStartTime);
        
        List<MeetingReservation> reservations = this.list(wrapper);
        return ApiResponse.success(reservations);
    }

    @Override
    public boolean checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeReservationId) {
        int count = this.baseMapper.countConflictingReservations(roomId, startTime, endTime, excludeReservationId);
        return count > 0;
    }

    @Override
    public List<MeetingReservation> findConflictingReservations(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return this.baseMapper.findConflictingReservations(roomId, startTime, endTime);
    }
}