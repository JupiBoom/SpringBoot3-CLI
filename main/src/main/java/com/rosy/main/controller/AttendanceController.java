package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.service.IAttendanceService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    @PostMapping("/checkIn")
    public BaseResponse<Boolean> checkIn(@RequestParam Long courseId, @RequestParam Long studentId, @RequestParam String date) {
        if (courseId == null || studentId == null || date == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate attendanceDate = LocalDate.parse(date);
        boolean result = attendanceService.checkIn(courseId, studentId, attendanceDate);
        return ResultUtils.success(result);
    }

    @PostMapping("/batchCheckIn")
    public BaseResponse<Boolean> batchCheckIn(@RequestParam Long courseId, @RequestBody List<Long> studentIds, @RequestParam String date) {
        if (courseId == null || studentIds == null || studentIds.isEmpty() || date == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate attendanceDate = LocalDate.parse(date);
        boolean result = attendanceService.batchCheckIn(courseId, studentIds, attendanceDate);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/course")
    public BaseResponse<List<AttendanceVO>> getAttendanceByCourse(@RequestParam Long courseId, @RequestParam(required = false) String date) {
        if (courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate attendanceDate = date != null ? LocalDate.parse(date) : null;
        List<AttendanceVO> attendanceVOs = attendanceService.getAttendanceByCourse(courseId, attendanceDate);
        return ResultUtils.success(attendanceVOs);
    }

    @GetMapping("/list/student")
    public BaseResponse<List<AttendanceVO>> getAttendanceByStudent(
            @RequestParam Long studentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        if (studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        List<AttendanceVO> attendanceVOs = attendanceService.getAttendanceByStudent(studentId, start, end);
        return ResultUtils.success(attendanceVOs);
    }
}
