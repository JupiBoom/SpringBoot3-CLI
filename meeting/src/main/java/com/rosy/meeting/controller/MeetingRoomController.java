package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.dto.MeetingRoomDTO;
import com.rosy.meeting.service.IMeetingRoomService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting/room")
public class MeetingRoomController {
    private final IMeetingRoomService meetingRoomService;

    public MeetingRoomController(IMeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @PostMapping("/create")
    public ApiResponse createMeetingRoom(@Validated @RequestBody MeetingRoomDTO dto) {
        return meetingRoomService.createMeetingRoom(dto);
    }

    @PutMapping("/update")
    public ApiResponse updateMeetingRoom(@Validated @RequestBody MeetingRoomDTO dto) {
        return meetingRoomService.updateMeetingRoom(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteMeetingRoom(@PathVariable Long id) {
        return meetingRoomService.deleteMeetingRoom(id);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse getMeetingRoomById(@PathVariable Long id) {
        return meetingRoomService.getMeetingRoomById(id);
    }

    @GetMapping("/list")
    public ApiResponse getAllMeetingRooms() {
        return meetingRoomService.getAllMeetingRooms();
    }

    @GetMapping("/available")
    public ApiResponse getAvailableRooms() {
        return meetingRoomService.getAvailableRooms();
    }

    @GetMapping("/search")
    public ApiResponse searchMeetingRooms(@RequestParam String keyword) {
        return meetingRoomService.searchMeetingRooms(keyword);
    }
}