package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.StatisticsVO;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据统计前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Resource
    IStatisticsService statisticsService;

    /**
     * 获取教师授课数量统计
     */
    @GetMapping("/teacher-course-count")
    public ApiResponse getTeacherCourseCount() {
        StatisticsVO result = statisticsService.getTeacherCourseCount();
        return ApiResponse.success(result);
    }

    /**
     * 获取学生出勤率统计
     */
    @GetMapping("/student-attendance-rate")
    public ApiResponse getStudentAttendanceRate() {
        StatisticsVO result = statisticsService.getStudentAttendanceRate();
        return ApiResponse.success(result);
    }

    /**
     * 获取讨论量最多的课程TOP10
     */
    @GetMapping("/top-courses-by-discussion")
    public ApiResponse getTopCoursesByDiscussion() {
        List<StatisticsVO> result = statisticsService.getTopCoursesByDiscussion();
        return ApiResponse.success(result);
    }

    /**
     * 获取授课数量最多的教师TOP10
     */
    @GetMapping("/top-teachers-by-courses")
    public ApiResponse getTopTeachersByCourses() {
        List<StatisticsVO> result = statisticsService.getTopTeachersByCourses();
        return ApiResponse.success(result);
    }
}
