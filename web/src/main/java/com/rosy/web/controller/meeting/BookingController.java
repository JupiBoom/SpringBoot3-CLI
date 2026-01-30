package com.rosy.web.controller.meeting;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.booking.ApprovalRequest;
import com.rosy.main.domain.dto.booking.BookingAddRequest;
import com.rosy.main.domain.dto.booking.BookingQueryRequest;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.vo.BookingVO;
import com.rosy.main.service.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@Tag(name = "预约管理", description = "会议室预约的增删改查和审批")
public class BookingController {

    @Resource
    private IBookingService bookingService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建预约")
    public ApiResponse addBooking(@RequestBody BookingAddRequest request) {
        Booking booking = bookingService.createBooking(request);
        return ApiResponse.success(booking.getId());
    }

    @PostMapping("/cancel")
    @ValidateRequest
    @Operation(summary = "取消预约")
    public ApiResponse cancelBooking(@RequestBody IdRequest idRequest) {
        bookingService.cancelBooking(idRequest.getId());
        return ApiResponse.success(true);
    }

    @PostMapping("/approve")
    @ValidateRequest
    @Operation(summary = "审批预约")
    public ApiResponse approveBooking(@RequestBody ApprovalRequest request) {
        bookingService.approveBooking(request);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取预约详情")
    public ApiResponse getBookingById(long id) {
        BookingVO bookingVO = bookingService.getBookingDetail(id);
        ThrowUtils.throwIf(bookingVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(bookingVO);
    }

    @GetMapping("/get/vo")
    @Operation(summary = "获取预约VO")
    public ApiResponse getBookingVOById(long id) {
        Booking booking = bookingService.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(bookingService.getBookingVO(booking));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取预约列表")
    public ApiResponse listBookingByPage(@RequestBody BookingQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Booking> page = bookingService.page(new Page<>(current, size),
                bookingService.getQueryWrapper(queryRequest));
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    @Operation(summary = "分页获取预约VO列表")
    public ApiResponse listBookingVOByPage(@RequestBody BookingQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Booking> page = bookingService.page(new Page<>(current, size),
                bookingService.getQueryWrapper(queryRequest));
        Page<BookingVO> voPage = PageUtils.convert(page, bookingService::getBookingVO);
        return ApiResponse.success(voPage);
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的预约列表")
    public ApiResponse myBookings(@RequestParam(defaultValue = "1") long current,
                                   @RequestParam(defaultValue = "10") long size) {
        Page<Booking> page = bookingService.page(new Page<>(current, size),
                bookingService.lambdaQuery()
                        .orderByDesc(Booking::getCreateTime));
        Page<BookingVO> voPage = PageUtils.convert(page, bookingService::getBookingVO);
        return ApiResponse.success(voPage);
    }
}
