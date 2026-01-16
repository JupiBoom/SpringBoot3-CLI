package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.service.ICheckInService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting/checkin")
public class CheckInController {
    private final ICheckInService checkInService;

    public CheckInController(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("/do")
    public ApiResponse checkIn(@RequestParam Long reservationId,
                                @RequestParam Long userId,
                                @RequestParam String userName) {
        return checkInService.checkIn(reservationId, userId, userName);
    }

    @GetMapping("/records/{reservationId}")
    public ApiResponse getCheckInRecords(@PathVariable Long reservationId) {
        return checkInService.getCheckInRecords(reservationId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse getUserCheckInHistory(@PathVariable Long userId) {
        return checkInService.getUserCheckInHistory(userId);
    }
}