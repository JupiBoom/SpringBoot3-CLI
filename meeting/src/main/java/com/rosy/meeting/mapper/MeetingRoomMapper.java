package com.rosy.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.meeting.domain.entity.MeetingRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {
}