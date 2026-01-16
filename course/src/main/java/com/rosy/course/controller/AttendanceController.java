package com.rosy.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.course.domain.entity.Attendance;
import com.rosy.course.service.IAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 考勤记录表 Controller
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Tag(name = "考勤管理", description = "上课签到、缺勤统计、请假审批")
public class AttendanceController {

    private final IAttendanceService attendanceService;

    @PostMapping("/signin")
    @Operation(summary = "签到")
    public ApiResponse signIn(@RequestBody Attendance attendance) {
        boolean result = attendanceService.signIn(attendance);
        if (!result) {
            return ApiResponse.error("今日已签到");
        }
        return ApiResponse.success(true);
    }

    @PostMapping("/apply-leave")
    @Operation(summary = "申请请假")
    public ApiResponse applyLeave(@RequestBody Attendance attendance) {
        return ApiResponse.success(attendanceService.applyLeave(attendance));
    }

    @PutMapping("/approve-leave/{id}")
    @Operation(summary = "审批请假")
    public ApiResponse approveLeave(@PathVariable Long id, 
                                              @RequestParam Byte status,
                                              @RequestParam Long approverId) {
        return ApiResponse.success(attendanceService.approveLeave(id, status, approverId));
    }

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程考勤列表")
    public ApiResponse listByCourse(@PathVariable Long courseId,
                                                       @RequestParam(required = false) Long studentId) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId);
        
        if (studentId != null) {
            wrapper.eq(Attendance::getStudentId, studentId);
        }
        
        wrapper.orderByDesc(Attendance::getCreateTime);
        
        return ApiResponse.success(attendanceService.list(wrapper));
    }

    @GetMapping("/absent/{courseId}")
    @Operation(summary = "获取课程缺勤统计")
    public ApiResponse getAbsentList(@PathVariable Long courseId) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId)
               .eq(Attendance::getStatus, (byte) 1);
        
        return ApiResponse.success(attendanceService.list(wrapper));
    }

}