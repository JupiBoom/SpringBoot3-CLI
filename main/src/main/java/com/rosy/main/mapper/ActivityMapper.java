package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    ActivityVO selectActivityDetail(@Param("id") Long id);

    List<Activity> selectActivitiesStartingInOneHour(@Param("now") LocalDateTime now, @Param("oneHourLater") LocalDateTime oneHourLater);

    List<Activity> selectActivitiesToUpdateStatus(@Param("now") LocalDateTime now);
}
