package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.attendance.AttendanceAddRequest;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.dto.attendance.AttendanceUpdateRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.service.IAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@Tag(name = "AttendanceController", description = "考勤管理")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建考勤记录")
    public ApiResponse addAttendance(@RequestBody AttendanceAddRequest attendanceAddRequest) {
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceAddRequest, attendance);
        boolean result = attendanceService.save(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(attendance.getId());
    }

    @PostMapping("/sign-in")
    @Operation(summary = "学生签到")
    public ApiResponse signIn(@RequestParam Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = attendanceService.signIn(courseId, studentId);
        return ApiResponse.success(result);
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除考勤记录")
    public ApiResponse deleteAttendance(@RequestBody IdRequest idRequest) {
        boolean result = attendanceService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新考勤记录")
    public ApiResponse updateAttendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {
        if (attendanceUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceUpdateRequest, attendance);
        boolean result = attendanceService.updateById(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取考勤记录")
    public ApiResponse getAttendanceById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendance);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取考勤列表")
    public ApiResponse listAttendanceByPage(@RequestBody AttendanceQueryRequest attendanceQueryRequest) {
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size),
                attendanceService.getQueryWrapper(attendanceQueryRequest));
        return ApiResponse.success(attendancePage);
    }

    @GetMapping("/statistics/course/{courseId}")
    @Operation(summary = "获取课程考勤统计")
    public ApiResponse getCourseAttendanceStatistics(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Map<String, Object>> statistics = attendanceService.getCourseAttendanceStatistics(courseId);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/statistics/student/{studentId}")
    @Operation(summary = "获取学生出勤率统计")
    public ApiResponse getStudentAttendanceRate(@PathVariable Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AttendanceStatisticsVO> statistics = attendanceService.getStudentAttendanceRate(studentId);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/course/{courseId}/absent")
    @Operation(summary = "获取课程缺勤记录")
    public ApiResponse getAbsentRecords(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(attendanceService.getAbsentRecords(courseId));
    }
}
