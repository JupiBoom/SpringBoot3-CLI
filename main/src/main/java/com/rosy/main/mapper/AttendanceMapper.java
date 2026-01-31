package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 考勤表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {

}