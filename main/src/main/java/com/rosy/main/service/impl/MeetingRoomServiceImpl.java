package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议室Service实现类
 */
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public Page<MeetingRoomVO> getMeetingRoomPage(Page<MeetingRoom> page, String name, String location, Integer status) {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like("name", name);
        }
        
        if (StringUtils.hasText(location)) {
            queryWrapper.like("location", location);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        Page<MeetingRoom> meetingRoomPage = this.page(page, queryWrapper);
        
        // 转换为VO
        Page<MeetingRoomVO> voPage = new Page<>();
        BeanUtils.copyProperties(meetingRoomPage, voPage);
        
        List<MeetingRoomVO> voList = meetingRoomPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public MeetingRoomVO getMeetingRoomById(Long id) {
        MeetingRoom meetingRoom = this.getById(id);
        if (meetingRoom == null) {
            return null;
        }
        return convertToVO(meetingRoom);
    }

    @Override
    public boolean addMeetingRoom(MeetingRoom meetingRoom) {
        // 检查名称是否重复
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", meetingRoom.getName());
        MeetingRoom existingRoom = this.getOne(queryWrapper);
        
        if (existingRoom != null) {
            throw new RuntimeException("会议室名称已存在");
        }
        
        return this.save(meetingRoom);
    }

    @Override
    public boolean updateMeetingRoom(MeetingRoom meetingRoom) {
        // 检查名称是否重复（排除自己）
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", meetingRoom.getName());
        queryWrapper.ne("id", meetingRoom.getId());
        MeetingRoom existingRoom = this.getOne(queryWrapper);
        
        if (existingRoom != null) {
            throw new RuntimeException("会议室名称已存在");
        }
        
        return this.updateById(meetingRoom);
    }

    @Override
    public boolean deleteMeetingRoom(Long id) {
        // 检查是否有未完成的预约
        // 这里可以添加检查逻辑，如果有未完成的预约，则不允许删除
        
        return this.removeById(id);
    }

    @Override
    public List<MeetingRoomVO> getAvailableRooms() {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("name");
        
        List<MeetingRoom> meetingRooms = this.list(queryWrapper);
        
        return meetingRooms.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 实体转VO
     * @param meetingRoom 会议室实体
     * @return 会议室VO
     */
    private MeetingRoomVO convertToVO(MeetingRoom meetingRoom) {
        MeetingRoomVO vo = new MeetingRoomVO();
        BeanUtils.copyProperties(meetingRoom, vo);
        return vo;
    }
}