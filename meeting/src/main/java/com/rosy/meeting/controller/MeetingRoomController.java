package com.rosy.meeting.controller;

import com.rosy.meeting.domain.vo.MeetingRoomCreateVO;
import com.rosy.meeting.domain.vo.MeetingRoomUpdateVO;
import com.rosy.meeting.domain.vo.MeetingRoomVO;
import com.rosy.meeting.service.IMeetingRoomService;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 会议室管理控制器
 */
@RestController
@RequestMapping("/meeting/room")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    /**
     * 创建会议室
     */
    @PostMapping("/create")
    public ApiResponse<MeetingRoomVO> createMeetingRoom(@RequestBody MeetingRoomCreateVO vo) {
        try {
            MeetingRoomVO result = meetingRoomService.createMeetingRoom(vo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 更新会议室
     */
    @PostMapping("/update")
    public ApiResponse<MeetingRoomVO> updateMeetingRoom(@RequestBody MeetingRoomUpdateVO vo) {
        try {
            MeetingRoomVO result = meetingRoomService.updateMeetingRoom(vo);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 删除会议室
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteMeetingRoom(@PathVariable Long id) {
        try {
            meetingRoomService.deleteMeetingRoom(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取会议室详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse<MeetingRoomVO> getMeetingRoomById(@PathVariable Long id) {
        try {
            MeetingRoomVO result = meetingRoomService.getMeetingRoomById(id);
            return ApiResponse.success(result);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取所有会议室列表
     */
    @GetMapping("/list")
    public ApiResponse<List<MeetingRoomVO>> getAllMeetingRooms() {
        List<MeetingRoomVO> result = meetingRoomService.getAllMeetingRooms();
        return ApiResponse.success(result);
    }

    /**
     * 搜索会议室
     */
    @GetMapping("/search")
    public ApiResponse<List<MeetingRoomVO>> searchMeetingRooms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer status) {
        List<MeetingRoomVO> result = meetingRoomService.searchMeetingRooms(name, location, status);
        return ApiResponse.success(result);
    }
}