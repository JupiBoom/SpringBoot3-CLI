package com.rosy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.course.domain.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {

    /**
     * 统计学生课程出勤率
     */
    @Select("SELECT c.id as course_id, c.name as course_name, " +
            "COUNT(a.id) as total_classes, " +
            "SUM(CASE WHEN a.status = 0 THEN 1 ELSE 0 END) as attended_classes " +
            "FROM course c " +
            "LEFT JOIN attendance a ON c.id = a.course_id AND a.student_id = #{studentId} " +
            "GROUP BY c.id, c.name")
    List<Map<String, Object>> selectAttendanceRateByStudent(@Param("studentId") Long studentId);

    /**
     * 统计教师的上课量
     */
    @Select("SELECT c.id as course_id, c.name as course_name, " +
            "COUNT(DISTINCT a.sign_time) as total_teaching_days " +
            "FROM course c " +
            "LEFT JOIN attendance a ON c.id = a.course_id " +
            "WHERE c.teacher_id = #{teacherId} " +
            "GROUP BY c.id, c.name")
    List<Map<String, Object>> selectTeachingCountByTeacher(@Param("teacherId") Long teacherId);

}