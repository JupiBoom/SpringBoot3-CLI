package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.booking.ApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.vo.BookingVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingService extends IService<Booking> {

    Booking createBooking(BookingAddRequest request);

    void cancelBooking(Long bookingId);

    void approveBooking(ApprovalRequest request);

    List<Booking> findConflictingBookings(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId);

    boolean hasBookingConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId);

    BookingVO getBookingDetail(Long id);

    BookingVO getBookingVO(Booking booking);

    LambdaQueryWrapper<Booking> getQueryWrapper(BookingQueryRequest queryRequest);
}
