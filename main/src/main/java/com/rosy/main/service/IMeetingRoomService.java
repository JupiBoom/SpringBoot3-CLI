package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

import java.util.List;

public interface IMeetingRoomService extends IService<MeetingRoom> {

    Long addRoom(MeetingRoomAddRequest request);

    Boolean updateRoom(MeetingRoomUpdateRequest request);

    Boolean deleteRoom(Long id);

    MeetingRoomVO getRoomById(Long id);

    List<MeetingRoomVO> getAllRooms();

    List<MeetingRoomVO> getAvailableRooms();
}
