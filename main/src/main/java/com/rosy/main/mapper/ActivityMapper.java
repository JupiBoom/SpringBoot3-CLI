package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 活动Mapper接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
}