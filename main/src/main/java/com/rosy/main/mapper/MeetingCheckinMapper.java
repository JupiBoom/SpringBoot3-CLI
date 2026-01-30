package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingCheckin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议签到Mapper接口
 */
@Mapper
public interface MeetingCheckinMapper extends BaseMapper<MeetingCheckin> {
}