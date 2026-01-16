package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.exception.BusinessException;
import com.rosy.meeting.domain.dto.MeetingRoomDTO;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.mapper.MeetingRoomMapper;
import com.rosy.meeting.service.IMeetingRoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createMeetingRoom(MeetingRoomDTO dto) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtils.copyProperties(dto, meetingRoom);
        
        if (dto.getStatus() == null) {
            meetingRoom.setStatus(1);
        }
        
        boolean saved = this.save(meetingRoom);
        if (saved) {
            return ApiResponse.success("会议室创建成功", meetingRoom);
        }
        return ApiResponse.error("会议室创建失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateMeetingRoom(MeetingRoomDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("会议室ID不能为空");
        }
        
        MeetingRoom existingRoom = this.getById(dto.getId());
        if (existingRoom == null) {
            throw new BusinessException("会议室不存在");
        }
        
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtils.copyProperties(dto, meetingRoom);
        
        boolean updated = this.updateById(meetingRoom);
        if (updated) {
            return ApiResponse.success("会议室更新成功", meetingRoom);
        }
        return ApiResponse.error("会议室更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteMeetingRoom(Long id) {
        MeetingRoom meetingRoom = this.getById(id);
        if (meetingRoom == null) {
            throw new BusinessException("会议室不存在");
        }
        
        boolean deleted = this.removeById(id);
        if (deleted) {
            return ApiResponse.success("会议室删除成功");
        }
        return ApiResponse.error("会议室删除失败");
    }

    @Override
    public ApiResponse getMeetingRoomById(Long id) {
        MeetingRoom meetingRoom = this.getById(id);
        if (meetingRoom == null) {
            throw new BusinessException("会议室不存在");
        }
        return ApiResponse.success(meetingRoom);
    }

    @Override
    public ApiResponse getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = this.list();
        return ApiResponse.success(meetingRooms);
    }

    @Override
    public ApiResponse getAvailableRooms() {
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingRoom::getStatus, 1);
        wrapper.orderByAsc(MeetingRoom::getRoomName);
        
        List<MeetingRoom> meetingRooms = this.list(wrapper);
        return ApiResponse.success(meetingRooms);
    }

    @Override
    public ApiResponse searchMeetingRooms(String keyword) {
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.like(MeetingRoom::getRoomName, keyword)
                         .or().like(MeetingRoom::getLocation, keyword)
                         .or().like(MeetingRoom::getDescription, keyword));
        wrapper.orderByAsc(MeetingRoom::getRoomName);
        
        List<MeetingRoom> meetingRooms = this.list(wrapper);
        return ApiResponse.success(meetingRooms);
    }
}