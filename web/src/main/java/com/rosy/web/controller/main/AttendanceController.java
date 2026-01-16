package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.service.IAttendanceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 考勤管理前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Resource
    IAttendanceService attendanceService;

    /**
     * 学生签到
     */
    @PostMapping("/signin")
    public ApiResponse signIn(@RequestParam Long courseId, @RequestParam Long studentId) {
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(studentId == null || studentId <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = attendanceService.signIn(courseId, studentId);
        return ApiResponse.success(result);
    }

    /**
     * 申请请假
     */
    @PostMapping("/apply-leave")
    @ValidateRequest
    public ApiResponse applyLeave(@RequestBody Attendance attendance) {
        ThrowUtils.throwIf(attendance.getCourseId() == null || attendance.getCourseId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(attendance.getStudentId() == null || attendance.getStudentId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(attendance.getLeaveType() == null, ErrorCode.PARAMS_ERROR);
        attendance.setStatus((byte) 2);
        attendance.setLeaveStatus((byte) 0);
        boolean result = attendanceService.save(attendance);
        return ApiResponse.success(result);
    }

    /**
     * 审批请假
     */
    @PostMapping("/approve-leave")
    public ApiResponse approveLeave(@RequestParam Long attendanceId, @RequestParam Byte approveStatus) {
        ThrowUtils.throwIf(attendanceId == null || attendanceId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(approveStatus == null || (approveStatus != 1 && approveStatus != 2), ErrorCode.PARAMS_ERROR);
        boolean result = attendanceService.approveLeave(attendanceId, approveStatus);
        return ApiResponse.success(result);
    }

    /**
     * 获取课程考勤统计
     */
    @GetMapping("/statistics/course/{courseId}")
    public ApiResponse getCourseAttendanceStatistics(@PathVariable Long courseId) {
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        List<AttendanceStatisticsVO> statistics = attendanceService.getAttendanceStatisticsByCourse(courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取学生考勤统计
     */
    @GetMapping("/statistics/student/{studentId}")
    public ApiResponse getStudentAttendanceStatistics(@PathVariable Long studentId) {
        ThrowUtils.throwIf(studentId == null || studentId <= 0, ErrorCode.PARAMS_ERROR);
        List<AttendanceStatisticsVO> statistics = attendanceService.getAttendanceStatisticsByStudent(studentId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取学生某课程的考勤记录
     */
    @GetMapping("/record/student-course")
    public ApiResponse getStudentCourseAttendance(@RequestParam Long studentId, @RequestParam Long courseId) {
        ThrowUtils.throwIf(studentId == null || studentId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        List<Attendance> attendanceList = attendanceService.lambdaQuery()
                .eq(Attendance::getStudentId, studentId)
                .eq(Attendance::getCourseId, courseId)
                .list();
        return ApiResponse.success(attendanceList);
    }

    /**
     * 获取待审批的请假列表
     */
    @GetMapping("/leave/pending")
    public ApiResponse getPendingLeaveApprovals(@RequestParam(required = false) Long courseId) {
        if (courseId == null) {
            List<Attendance> leaveList = attendanceService.lambdaQuery()
                    .eq(Attendance::getStatus, (byte) 2)
                    .eq(Attendance::getLeaveStatus, (byte) 0)
                    .list();
            return ApiResponse.success(leaveList);
        } else {
            List<Attendance> leaveList = attendanceService.lambdaQuery()
                    .eq(Attendance::getCourseId, courseId)
                    .eq(Attendance::getStatus, (byte) 2)
                    .eq(Attendance::getLeaveStatus, (byte) 0)
                    .list();
            return ApiResponse.success(leaveList);
        }
    }

    /**
     * 获取考勤记录详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse getAttendanceById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendance);
    }
}
