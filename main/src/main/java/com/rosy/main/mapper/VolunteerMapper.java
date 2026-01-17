package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Volunteer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 志愿者Mapper接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Mapper
public interface VolunteerMapper extends BaseMapper<Volunteer> {
}