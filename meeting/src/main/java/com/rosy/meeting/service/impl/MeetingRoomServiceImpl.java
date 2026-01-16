package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.domain.vo.MeetingRoomCreateVO;
import com.rosy.meeting.domain.vo.MeetingRoomUpdateVO;
import com.rosy.meeting.domain.vo.MeetingRoomVO;
import com.rosy.meeting.mapper.MeetingRoomMapper;
import com.rosy.meeting.service.IMeetingRoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议室服务实现类
 */
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public MeetingRoomVO createMeetingRoom(MeetingRoomCreateVO vo) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtils.copyProperties(vo, meetingRoom);
        meetingRoom.setStatus(1); // 默认启用
        save(meetingRoom);
        return toVO(meetingRoom);
    }

    @Override
    public MeetingRoomVO updateMeetingRoom(MeetingRoomUpdateVO vo) {
        MeetingRoom meetingRoom = getById(vo.getId());
        if (meetingRoom == null) {
            throw new RuntimeException("会议室不存在");
        }
        BeanUtils.copyProperties(vo, meetingRoom);
        updateById(meetingRoom);
        return toVO(meetingRoom);
    }

    @Override
    public void deleteMeetingRoom(Long id) {
        removeById(id);
    }

    @Override
    public MeetingRoomVO getMeetingRoomById(Long id) {
        MeetingRoom meetingRoom = getById(id);
        if (meetingRoom == null) {
            throw new RuntimeException("会议室不存在");
        }
        return toVO(meetingRoom);
    }

    @Override
    public List<MeetingRoomVO> getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = list();
        return meetingRooms.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<MeetingRoomVO> searchMeetingRooms(String name, String location, Integer status) {
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(MeetingRoom::getName, name);
        }
        if (StringUtils.hasText(location)) {
            wrapper.like(MeetingRoom::getLocation, location);
        }
        if (status != null) {
            wrapper.eq(MeetingRoom::getStatus, status);
        }
        List<MeetingRoom> meetingRooms = list(wrapper);
        return meetingRooms.stream().map(this::toVO).collect(Collectors.toList());
    }

    private MeetingRoomVO toVO(MeetingRoom meetingRoom) {
        MeetingRoomVO vo = new MeetingRoomVO();
        BeanUtils.copyProperties(meetingRoom, vo);
        return vo;
    }
}