package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.BookingStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.booking.ApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.entity.ApprovalRecord;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.BookingVO;
import com.rosy.main.mapper.ApprovalRecordMapper;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IBookingService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements IBookingService {

    @Resource
    private MeetingRoomMapper meetingRoomMapper;

    @Resource
    private ApprovalRecordMapper approvalRecordMapper;

    @Resource
    @Lazy
    private NotificationServiceImpl notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Booking createBooking(BookingAddRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        }

        MeetingRoom room = meetingRoomMapper.selectById(request.getRoomId());
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        }
        if (room.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "会议室不可用");
        }

        if (hasBookingConflict(request.getRoomId(), request.getStartTime(), request.getEndTime(), null)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时间段会议室已被预约");
        }

        Booking booking = new Booking();
        BeanUtil.copyProperties(request, booking);
        booking.setStatus(BookingStatusEnum.PENDING_APPROVAL.getCode().byteValue());
        this.save(booking);

        return booking;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBooking(Long bookingId) {
        Booking booking = this.getById(bookingId);
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }
        if (booking.getStatus() == BookingStatusEnum.CANCELLED.getCode().byteValue()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "预约已取消");
        }

        booking.setStatus(BookingStatusEnum.CANCELLED.getCode().byteValue());
        this.updateById(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveBooking(ApprovalRequest request) {
        Booking booking = this.getById(request.getBookingId());
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }
        if (booking.getStatus() != BookingStatusEnum.PENDING_APPROVAL.getCode().byteValue()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该预约不需要审批");
        }

        if (request.getApprovalResult() == 1) {
            booking.setStatus(BookingStatusEnum.APPROVED.getCode().byteValue());
        } else {
            booking.setStatus(BookingStatusEnum.REJECTED.getCode().byteValue());
        }
        this.updateById(booking);

        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setBookingId(request.getBookingId());
        approvalRecord.setApprovalResult(request.getApprovalResult());
        approvalRecord.setApprovalComment(request.getApprovalComment());
        approvalRecord.setApprovalTime(LocalDateTime.now());
        approvalRecordMapper.insert(approvalRecord);

        notificationService.sendApprovalNotification(booking.getId(), request.getApprovalResult() == 1);
    }

    @Override
    public List<Booking> findConflictingBookings(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId) {
        return baseMapper.findConflictingBookings(roomId, startTime, endTime, excludeBookingId);
    }

    @Override
    public boolean hasBookingConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId) {
        List<Booking> conflictingBookings = findConflictingBookings(roomId, startTime, endTime, excludeBookingId);
        return conflictingBookings != null && !conflictingBookings.isEmpty();
    }

    @Override
    public BookingVO getBookingDetail(Long id) {
        BookingVO bookingVO = baseMapper.getBookingDetail(id);
        if (bookingVO != null && bookingVO.getStatus() != null) {
            BookingStatusEnum statusEnum = BookingStatusEnum.getByCode((int) bookingVO.getStatus());
            if (statusEnum != null) {
                bookingVO.setStatusText(statusEnum.getText());
            }
        }
        return bookingVO;
    }

    @Override
    public BookingVO getBookingVO(Booking booking) {
        return Optional.ofNullable(booking)
                .map(b -> {
                    BookingVO vo = BeanUtil.copyProperties(b, BookingVO.class);
                    if (b.getStatus() != null) {
                        BookingStatusEnum statusEnum = BookingStatusEnum.getByCode((int) b.getStatus());
                        if (statusEnum != null) {
                            vo.setStatusText(statusEnum.getText());
                        }
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Booking> getQueryWrapper(BookingQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Booking> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRoomId(), Booking::getRoomId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), Booking::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), Booking::getStatus);

        if (queryRequest.getQueryStartTime() != null) {
            queryWrapper.ge(Booking::getStartTime, queryRequest.getQueryStartTime());
        }
        if (queryRequest.getQueryEndTime() != null) {
            queryWrapper.le(Booking::getEndTime, queryRequest.getQueryEndTime());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Booking::getCreateTime);

        return queryWrapper;
    }
}
