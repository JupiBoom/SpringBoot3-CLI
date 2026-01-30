package com.rosy.web.controller.meeting;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会议室管理控制器
 */
@RestController
@RequestMapping("/api/meeting-room")
@Tag(name = "会议室管理", description = "会议室管理相关接口")
public class MeetingRoomController {

    @Autowired
    private IMeetingRoomService meetingRoomService;

    @GetMapping("/page")
    @Operation(summary = "分页查询会议室", description = "根据条件分页查询会议室列表")
    public ApiResponse<Page<MeetingRoomVO>> getMeetingRoomPage(
            @Parameter(description = "分页参数") PageRequest pageRequest,
            @Parameter(description = "会议室名称") @RequestParam(required = false) String name,
            @Parameter(description = "会议室位置") @RequestParam(required = false) String location,
            @Parameter(description = "状态：0-停用，1-可用") @RequestParam(required = false) Integer status) {
        
        Page<MeetingRoom> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<MeetingRoomVO> result = meetingRoomService.getMeetingRoomPage(page, name, location, status);
        
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取会议室详情", description = "根据ID获取会议室详细信息")
    public ApiResponse<MeetingRoomVO> getMeetingRoomById(
            @Parameter(description = "会议室ID") @PathVariable Long id) {
        
        MeetingRoomVO result = meetingRoomService.getMeetingRoomById(id);
        if (result == null) {
            return ApiResponse.error("会议室不存在");
        }
        
        return ApiResponse.success(result);
    }

    @PostMapping
    @Operation(summary = "添加会议室", description = "添加新的会议室")
    public ApiResponse<Boolean> addMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        try {
            boolean result = meetingRoomService.addMeetingRoom(meetingRoom);
            return result ? ApiResponse.success(true) : ApiResponse.error("添加失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "更新会议室", description = "更新会议室信息")
    public ApiResponse<Boolean> updateMeetingRoom(@RequestBody MeetingRoom meetingRoom) {
        try {
            boolean result = meetingRoomService.updateMeetingRoom(meetingRoom);
            return result ? ApiResponse.success(true) : ApiResponse.error("更新失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除会议室", description = "根据ID删除会议室")
    public ApiResponse<Boolean> deleteMeetingRoom(
            @Parameter(description = "会议室ID") @PathVariable Long id) {
        try {
            boolean result = meetingRoomService.deleteMeetingRoom(id);
            return result ? ApiResponse.success(true) : ApiResponse.error("删除失败");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/available")
    @Operation(summary = "获取可用会议室", description = "获取所有状态为可用的会议室列表")
    public ApiResponse<List<MeetingRoomVO>> getAvailableRooms() {
        List<MeetingRoomVO> result = meetingRoomService.getAvailableRooms();
        return ApiResponse.success(result);
    }
}