package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.StudentAttendanceStatsVO;
import com.rosy.main.domain.vo.TeacherTeachingStatsVO;
import com.rosy.main.service.IStatisticsService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private IStatisticsService statisticsService;

    @GetMapping("/student/attendance")
    public BaseResponse<StudentAttendanceStatsVO> getStudentAttendanceStats(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (studentId == null || courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        StudentAttendanceStatsVO statsVO = statisticsService.getStudentAttendanceStats(studentId, courseId, start, end);
        return ResultUtils.success(statsVO);
    }

    @GetMapping("/course/attendance")
    public BaseResponse<List<StudentAttendanceStatsVO>> getStudentAttendanceStatsList(
            @RequestParam Long courseId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        List<StudentAttendanceStatsVO> statsVOs = statisticsService.getStudentAttendanceStatsList(courseId, start, end);
        return ResultUtils.success(statsVOs);
    }

    @GetMapping("/teacher/teaching")
    public BaseResponse<TeacherTeachingStatsVO> getTeacherTeachingStats(@RequestParam Long teacherId) {
        if (teacherId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TeacherTeachingStatsVO statsVO = statisticsService.getTeacherTeachingStats(teacherId);
        return ResultUtils.success(statsVO);
    }

    @GetMapping("/teacher/all")
    public BaseResponse<List<TeacherTeachingStatsVO>> getAllTeacherTeachingStats() {
        List<TeacherTeachingStatsVO> statsVOs = statisticsService.getAllTeacherTeachingStats();
        return ResultUtils.success(statsVOs);
    }
}
