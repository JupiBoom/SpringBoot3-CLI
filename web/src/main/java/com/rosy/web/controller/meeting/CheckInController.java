package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.checkin.CheckInRequest;
import com.rosy.main.domain.entity.CheckIn;
import com.rosy.main.service.ICheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/check-in")
@Tag(name = "签到管理", description = "会议签到管理")
public class CheckInController {

    @Resource
    private ICheckInService checkInService;

    @PostMapping("/do")
    @ValidateRequest
    @Operation(summary = "签到")
    public ApiResponse checkIn(@RequestBody CheckInRequest request) {
        checkInService.checkIn(request);
        return ApiResponse.success(true);
    }

    @PostMapping("/batch")
    @ValidateRequest
    @Operation(summary = "批量签到")
    public ApiResponse batchCheckIn(@RequestParam Long bookingId,
                                     @RequestBody List<CheckInRequest> requests) {
        checkInService.batchCheckIn(bookingId, requests);
        return ApiResponse.success(true);
    }

    @GetMapping("/count")
    @Operation(summary = "获取签到人数")
    public ApiResponse countCheckedIn(@RequestParam Long bookingId) {
        int count = checkInService.countCheckedIn(bookingId);
        return ApiResponse.success(count);
    }

    @GetMapping("/list")
    @Operation(summary = "获取签到列表")
    public ApiResponse listCheckIn(@RequestParam Long bookingId) {
        return ApiResponse.success(checkInService.lambdaQuery()
                .eq(CheckIn::getBookingId, bookingId)
                .list());
    }
}
