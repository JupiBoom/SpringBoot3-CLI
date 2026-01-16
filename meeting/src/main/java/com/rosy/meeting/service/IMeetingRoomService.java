package com.rosy.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.domain.dto.MeetingRoomDTO;
import com.rosy.common.domain.entity.ApiResponse;

import java.util.List;

public interface IMeetingRoomService extends IService<MeetingRoom> {
    ApiResponse createMeetingRoom(MeetingRoomDTO dto);
    
    ApiResponse updateMeetingRoom(MeetingRoomDTO dto);
    
    ApiResponse deleteMeetingRoom(Long id);
    
    ApiResponse getMeetingRoomById(Long id);
    
    ApiResponse getAllMeetingRooms();
    
    ApiResponse getAvailableRooms();
    
    ApiResponse searchMeetingRooms(String keyword);
}