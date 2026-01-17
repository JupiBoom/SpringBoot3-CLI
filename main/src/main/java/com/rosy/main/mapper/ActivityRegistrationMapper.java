package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 活动报名Mapper接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Mapper
public interface ActivityRegistrationMapper extends BaseMapper<ActivityRegistration> {
}