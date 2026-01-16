package com.rosy.meeting.controller;

import com.rosy.meeting.domain.vo.MeetingBookingApproveVO;
import com.rosy.meeting.domain.vo.MeetingBookingCreateVO;
import com.rosy.meeting.domain.vo.MeetingBookingVO;
import com.rosy.meeting.domain.vo.MeetingRoomStatisticsVO;
import com.rosy.meeting.service.IMeetingBookingService;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议预约管理控制器
 */
@RestController
@RequestMapping("/meeting/booking")
public class MeetingBookingController {

    @Resource
    private IMeetingBookingService meetingBookingService;

    /**
     * 提交预约申请
     */
    @PostMapping("/create")
    public ApiResponse<MeetingBookingVO> createBooking(@RequestBody MeetingBookingCreateVO vo) {
        try {
            MeetingBookingVO result = meetingBookingService.createBooking(vo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 审批预约
     */
    @PostMapping("/approve")
    public ApiResponse<MeetingBookingVO> approveBooking(@RequestBody MeetingBookingApproveVO vo) {
        try {
            MeetingBookingVO result = meetingBookingService.approveBooking(vo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取预约详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse<MeetingBookingVO> getBookingById(@PathVariable Long id) {
        try {
            MeetingBookingVO result = meetingBookingService.getBookingById(id);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取用户的预约列表
     */
    @GetMapping("/list/user/{userId}")
    public ApiResponse<List<MeetingBookingVO>> getBookingsByUserId(@PathVariable Long userId) {
        List<MeetingBookingVO> result = meetingBookingService.getBookingsByUserId(userId);
        return ApiResponse.success(result);
    }

    /**
     * 获取待审批的预约列表
     */
    @GetMapping("/list/pending")
    public ApiResponse<List<MeetingBookingVO>> getPendingBookings() {
        List<MeetingBookingVO> result = meetingBookingService.getPendingBookings();
        return ApiResponse.success(result);
    }

    /**
     * 获取会议室的预约列表
     */
    @GetMapping("/list/room/{meetingRoomId}")
    public ApiResponse<List<MeetingBookingVO>> getBookingsByMeetingRoom(
            @PathVariable Long meetingRoomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<MeetingBookingVO> result = meetingBookingService.getBookingsByMeetingRoom(meetingRoomId, startTime, endTime);
        return ApiResponse.success(result);
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/cancel/{id}")
    public ApiResponse<Void> cancelBooking(@PathVariable Long id) {
        try {
            meetingBookingService.cancelBooking(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 会议签到
     */
    @PostMapping("/checkin/{id}")
    public ApiResponse<Void> checkIn(@PathVariable Long id, @RequestParam Long userId) {
        try {
            meetingBookingService.checkIn(id, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取会议室使用统计
     */
    @GetMapping("/statistics/{meetingRoomId}")
    public ApiResponse<Integer> getUsageStatistics(
            @PathVariable Long meetingRoomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        int result = meetingBookingService.getUsageStatistics(meetingRoomId, startTime, endTime);
        return ApiResponse.success(result);
    }

    /**
     * 获取会议室详细统计信息
     */
    @GetMapping("/statistics/detail/{meetingRoomId}")
    public ApiResponse<MeetingRoomStatisticsVO> getMeetingRoomStatistics(
            @PathVariable Long meetingRoomId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            MeetingRoomStatisticsVO result = meetingBookingService.getMeetingRoomStatistics(meetingRoomId, startTime, endTime);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }
}