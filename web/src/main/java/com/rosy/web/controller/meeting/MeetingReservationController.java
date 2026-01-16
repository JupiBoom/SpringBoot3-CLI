package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.LogTag;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.MeetingReservationAddRequest;
import com.rosy.main.domain.dto.MeetingReservationUpdateRequest;
import com.rosy.main.domain.vo.MeetingReservationVO;
import com.rosy.main.service.IMeetingReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议预约管理")
@RestController
@RequestMapping("/api/meeting/reservation")
public class MeetingReservationController {

    @Resource
    private IMeetingReservationService meetingReservationService;

    @Operation(summary = "添加预约")
    @PostMapping("/add")
    @LogTag
    public ApiResponse addReservation(@RequestBody MeetingReservationAddRequest request) {
        Long reservationId = meetingReservationService.addReservation(request);
        return ApiResponse.success(reservationId);
    }

    @Operation(summary = "更新预约")
    @PostMapping("/update")
    @LogTag
    public ApiResponse updateReservation(@RequestBody MeetingReservationUpdateRequest request) {
        Boolean result = meetingReservationService.updateReservation(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "取消预约")
    @PostMapping("/cancel/{id}")
    @LogTag
    public ApiResponse cancelReservation(@PathVariable Long id) {
        Boolean result = meetingReservationService.cancelReservation(id);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取预约详情")
    @GetMapping("/get/{id}")
    public ApiResponse getReservationById(@PathVariable Long id) {
        MeetingReservationVO reservation = meetingReservationService.getReservationById(id);
        return ApiResponse.success(reservation);
    }

    @Operation(summary = "获取我的预约")
    @GetMapping("/my")
    public ApiResponse getMyReservations() {
        List<MeetingReservationVO> reservations = meetingReservationService.getReservationsByApplicant(1L);
        return ApiResponse.success(reservations);
    }

    @Operation(summary = "获取会议室预约记录")
    @GetMapping("/room/{roomId}")
    public ApiResponse getReservationsByRoom(@PathVariable Long roomId) {
        List<MeetingReservationVO> reservations = meetingReservationService.getReservationsByRoom(roomId);
        return ApiResponse.success(reservations);
    }

    @Operation(summary = "获取待审批预约")
    @GetMapping("/pending")
    public ApiResponse getPendingApprovals() {
        List<MeetingReservationVO> reservations = meetingReservationService.getPendingApprovals();
        return ApiResponse.success(reservations);
    }
}
