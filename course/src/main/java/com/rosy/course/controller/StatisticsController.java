package com.rosy.course.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.vo.AttendanceRateVO;
import com.rosy.course.domain.vo.TeachingCountVO;
import com.rosy.course.service.IAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据统计 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "数据统计", description = "学生出勤率、教师上课量统计")
public class StatisticsController {

    private final IAttendanceService attendanceService;

    @GetMapping("/attendance-rate/{studentId}")
    @Operation(summary = "获取学生出勤率统计")
    public ApiResponse getAttendanceRateByStudent(@PathVariable Long studentId) {
        List<Map<String, Object>> result = attendanceService.getAttendanceRateByStudent(studentId);
        List<AttendanceRateVO> voList = new ArrayList<>();

        for (Map<String, Object> map : result) {
            AttendanceRateVO vo = new AttendanceRateVO();
            vo.setCourseId(((Number) map.get("course_id")).longValue());
            vo.setCourseName((String) map.get("course_name"));

            Integer totalClasses = ((Number) map.get("total_classes")).intValue();
            Integer attendedClasses = ((Number) map.get("attended_classes")).intValue();

            vo.setTotalClasses(totalClasses);
            vo.setAttendedClasses(attendedClasses);

            // 计算出勤率
            if (totalClasses > 0) {
                BigDecimal rate = new BigDecimal(attendedClasses)
                        .divide(new BigDecimal(totalClasses), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));
                vo.setAttendanceRate(rate);
            } else {
                vo.setAttendanceRate(BigDecimal.ZERO);
            }

            voList.add(vo);
        }

        return ApiResponse.success(voList);
    }

    @GetMapping("/teaching-count/{teacherId}")
    @Operation(summary = "获取教师上课量统计")
    public ApiResponse getTeachingCountByTeacher(@PathVariable Long teacherId) {
        List<Map<String, Object>> result = attendanceService.getTeachingCountByTeacher(teacherId);
        List<TeachingCountVO> voList = new ArrayList<>();

        for (Map<String, Object> map : result) {
            TeachingCountVO vo = new TeachingCountVO();
            vo.setCourseId(((Number) map.get("course_id")).longValue());
            vo.setCourseName((String) map.get("course_name"));
            vo.setTotalTeachingDays(((Number) map.get("total_teaching_days")).intValue());
            voList.add(vo);
        }

        return ApiResponse.success(voList);
    }

    @GetMapping("/absenteeism/{courseId}")
    @Operation(summary = "获取课程缺勤统计")
    public ApiResponse getAbsenteeismByCourse(@PathVariable Long courseId) {
        List<Map<String, Object>> result = attendanceService.getAbsenteeismByCourse(courseId);
        return ApiResponse.success(result);
    }

}