package com.rosy.web.controller.meeting;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.vo.MeetingBookingRequestVO;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.service.IMeetingBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会议预约管理控制器
 */
@RestController
@RequestMapping("/api/meeting-booking")
@Tag(name = "会议预约管理", description = "会议预约管理相关接口")
public class MeetingBookingController {

    @Autowired
    private IMeetingBookingService meetingBookingService;

    @GetMapping("/page")
    @Operation(summary = "分页查询预约记录", description = "根据条件分页查询预约记录列表")
    public ApiResponse<Page<MeetingBookingVO>> getBookingPage(
            @Parameter(description = "分页参数") PageRequest pageRequest,
            @Parameter(description = "会议室ID") @RequestParam(required = false) Long roomId,
            @Parameter(description = "预约人ID") @RequestParam(required = false) Long bookerId,
            @Parameter(description = "状态：0-待审批，1-已通过，2-已驳回，3-已取消") @RequestParam(required = false) Integer status,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        
        Page<com.rosy.main.domain.entity.MeetingBooking> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<MeetingBookingVO> result = meetingBookingService.getBookingPage(page, roomId, bookerId, status, startDate, endDate);
        
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取预约详情", description = "根据ID获取预约详细信息")
    public ApiResponse<MeetingBookingVO> getBookingById(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        
        MeetingBookingVO result = meetingBookingService.getBookingById(id);
        if (result == null) {
            return ApiResponse.error("预约记录不存在");
        }
        
        return ApiResponse.success(result);
    }

    @PostMapping
    @Operation(summary = "创建预约", description = "创建新的会议预约")
    public ApiResponse<Boolean> createBooking(
            @RequestBody MeetingBookingRequestVO requestVO,
            @Parameter(description = "预约人ID") @RequestHeader("X-User-Id") Long bookerId) {
        
        try {
            boolean result = meetingBookingService.createBooking(requestVO, bookerId);
            return result ? ApiResponse.success(true) : ApiResponse.error("创建失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "更新预约", description = "更新已有的会议预约")
    public ApiResponse<Boolean> updateBooking(@RequestBody MeetingBookingRequestVO requestVO) {
        try {
            boolean result = meetingBookingService.updateBooking(requestVO);
            return result ? ApiResponse.success(true) : ApiResponse.error("更新失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消预约", description = "取消指定的会议预约")
    public ApiResponse<Boolean> cancelBooking(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @Parameter(description = "预约人ID") @RequestHeader("X-User-Id") Long bookerId) {
        
        try {
            boolean result = meetingBookingService.cancelBooking(id, bookerId);
            return result ? ApiResponse.success(true) : ApiResponse.error("取消失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/approve/{id}")
    @Operation(summary = "审批预约", description = "审批指定的会议预约")
    public ApiResponse<Boolean> approveBooking(
            @Parameter(description = "预约ID") @PathVariable Long id,
            @Parameter(description = "是否通过") @RequestParam Boolean approved,
            @Parameter(description = "审批备注") @RequestParam(required = false) String remark,
            @Parameter(description = "审批人ID") @RequestHeader("X-User-Id") Long approveId) {
        
        try {
            boolean result = meetingBookingService.approveBooking(id, approveId, approved, remark);
            return result ? ApiResponse.success(true) : ApiResponse.error("审批失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/checkin/{bookingId}")
    @Operation(summary = "会议签到", description = "为指定会议进行签到")
    public ApiResponse<Boolean> checkInMeeting(
            @Parameter(description = "预约ID") @PathVariable Long bookingId,
            @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId) {
        
        try {
            boolean result = meetingBookingService.checkInMeeting(bookingId, userId);
            return result ? ApiResponse.success(true) : ApiResponse.error("签到失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/notification/unread-count")
    @Operation(summary = "获取未读通知数量", description = "获取当前用户的未读通知数量")
    public ApiResponse<Integer> getUnreadNotificationCount(
            @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId) {
        
        int count = meetingBookingService.getUnreadNotificationCount(userId);
        return ApiResponse.success(count);
    }

    @GetMapping("/notification/list")
    @Operation(summary = "获取通知列表", description = "获取当前用户的通知列表")
    public ApiResponse<List<NotificationVO>> getUserNotifications(
            @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId) {
        
        List<NotificationVO> notifications = meetingBookingService.getUserNotifications(userId);
        return ApiResponse.success(notifications);
    }

    @PutMapping("/notification/read/{notificationId}")
    @Operation(summary = "标记通知为已读", description = "将指定通知标记为已读")
    public ApiResponse<Boolean> markNotificationAsRead(
            @Parameter(description = "通知ID") @PathVariable Long notificationId,
            @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId) {
        
        boolean result = meetingBookingService.markNotificationAsRead(notificationId, userId);
        return result ? ApiResponse.success(true) : ApiResponse.error("操作失败");
    }
}