package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.LogTag;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议室管理")
@RestController
@RequestMapping("/api/meeting/room")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @Operation(summary = "添加会议室")
    @PostMapping("/add")
    @LogTag
    public ApiResponse addRoom(@RequestBody MeetingRoomAddRequest request) {
        Long roomId = meetingRoomService.addRoom(request);
        return ApiResponse.success(roomId);
    }

    @Operation(summary = "更新会议室")
    @PostMapping("/update")
    @LogTag
    public ApiResponse updateRoom(@RequestBody MeetingRoomUpdateRequest request) {
        Boolean result = meetingRoomService.updateRoom(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "删除会议室")
    @DeleteMapping("/delete/{id}")
    @LogTag
    public ApiResponse deleteRoom(@PathVariable Long id) {
        Boolean result = meetingRoomService.deleteRoom(id);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取会议室详情")
    @GetMapping("/get/{id}")
    public ApiResponse getRoomById(@PathVariable Long id) {
        MeetingRoomVO room = meetingRoomService.getRoomById(id);
        return ApiResponse.success(room);
    }

    @Operation(summary = "获取所有会议室")
    @GetMapping("/list")
    public ApiResponse getAllRooms() {
        List<MeetingRoomVO> rooms = meetingRoomService.getAllRooms();
        return ApiResponse.success(rooms);
    }

    @Operation(summary = "获取可用会议室")
    @GetMapping("/available")
    public ApiResponse getAvailableRooms() {
        List<MeetingRoomVO> rooms = meetingRoomService.getAvailableRooms();
        return ApiResponse.success(rooms);
    }
}
