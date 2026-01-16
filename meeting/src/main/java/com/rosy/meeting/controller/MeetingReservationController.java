package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.dto.MeetingReservationDTO;
import com.rosy.meeting.service.IMeetingReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/meeting/reservation")
public class MeetingReservationController {
    private final IMeetingReservationService reservationService;

    public MeetingReservationController(IMeetingReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/apply")
    public ApiResponse createReservation(@Validated @RequestBody MeetingReservationDTO dto) {
        return reservationService.createReservation(dto);
    }

    @PutMapping("/update")
    public ApiResponse updateReservation(@Validated @RequestBody MeetingReservationDTO dto) {
        return reservationService.updateReservation(dto);
    }

    @PutMapping("/cancel/{id}")
    public ApiResponse cancelReservation(@PathVariable Long id, @RequestParam Long userId) {
        return reservationService.cancelReservation(id, userId);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse getReservationsByRoom(@PathVariable Long roomId,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reservationService.getReservationsByRoom(roomId, startDate, endDate);
    }

    @GetMapping("/pending")
    public ApiResponse getAllPendingReservations() {
        return reservationService.getAllPendingReservations();
    }

    @GetMapping("/today/{roomId}")
    public ApiResponse getTodayReservations(@PathVariable Long roomId) {
        return reservationService.getTodayReservations(roomId);
    }

    @GetMapping("/check-conflict")
    public ApiResponse checkConflict(@RequestParam Long roomId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean hasConflict = reservationService.checkConflict(roomId, startTime, endTime, null);
        return ApiResponse.success("", hasConflict);
    }
}