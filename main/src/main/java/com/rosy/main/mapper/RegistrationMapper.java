package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.vo.RegistrationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {

    RegistrationVO selectRegistrationDetail(@Param("id") Long id);

    List<RegistrationVO> selectByActivityId(@Param("activityId") Long activityId);

    List<RegistrationVO> selectByUserId(@Param("userId") Long userId);

    List<Registration> selectApprovedRegistrations(@Param("activityId") Long activityId);

    List<Registration> selectUserApprovedRegistrations(@Param("userId") Long userId, @Param("now") java.time.LocalDateTime now);
}
