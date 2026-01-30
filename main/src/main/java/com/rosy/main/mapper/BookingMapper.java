package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.vo.BookingVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingMapper extends BaseMapper<Booking> {

    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          @Param("excludeBookingId") Long excludeBookingId);

    BookingVO getBookingDetail(@Param("id") Long id);
}
