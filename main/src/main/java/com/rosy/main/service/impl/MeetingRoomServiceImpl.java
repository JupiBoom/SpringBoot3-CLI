package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public Long addRoom(MeetingRoomAddRequest request) {
        MeetingRoom room = new MeetingRoom();
        room.setName(request.getName());
        room.setLocation(request.getLocation());
        room.setCapacity(request.getCapacity());
        room.setEquipment(request.getEquipment());
        room.setStatus(request.getStatus() != null ? request.getStatus() : (byte) 1);
        this.save(room);
        return room.getId();
    }

    @Override
    public Boolean updateRoom(MeetingRoomUpdateRequest request) {
        MeetingRoom room = this.getById(request.getId());
        if (room == null) {
            return false;
        }
        room.setName(request.getName());
        room.setLocation(request.getLocation());
        room.setCapacity(request.getCapacity());
        room.setEquipment(request.getEquipment());
        room.setStatus(request.getStatus());
        return this.updateById(room);
    }

    @Override
    public Boolean deleteRoom(Long id) {
        return this.removeById(id);
    }

    @Override
    public MeetingRoomVO getRoomById(Long id) {
        MeetingRoom room = this.getById(id);
        if (room == null) {
            return null;
        }
        return BeanUtil.copyProperties(room, MeetingRoomVO.class);
    }

    @Override
    public List<MeetingRoomVO> getAllRooms() {
        List<MeetingRoom> rooms = this.list();
        return rooms.stream()
                .map(room -> BeanUtil.copyProperties(room, MeetingRoomVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingRoomVO> getAvailableRooms() {
        QueryWrapper<MeetingRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        List<MeetingRoom> rooms = this.list(wrapper);
        return rooms.stream()
                .map(room -> BeanUtil.copyProperties(room, MeetingRoomVO.class))
                .collect(Collectors.toList());
    }
}
