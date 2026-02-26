package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    @Update("UPDATE activity SET current_count = current_count + 1 WHERE id = #{activityId} AND current_count < required_count")
    int incrementCurrentCount(Long activityId);
}
