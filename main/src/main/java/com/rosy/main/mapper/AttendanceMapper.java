package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {

    List<Map<String, Object>> getAttendanceStatistics(@Param("courseId") Long courseId);

    Map<String, Object> getStudentAttendanceRate(@Param("studentId") Long studentId);
}