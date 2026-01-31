package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.attendance.AttendanceAddRequest;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.dto.attendance.AttendanceUpdateRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.service.IAttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 考勤表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/attendance")
@Tag(name = "考勤管理", description = "考勤相关接口")
public class AttendanceController {
    
    @Resource
    private IAttendanceService attendanceService;

    // region 增删改查

    /**
     * 创建考勤记录
     */
    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建考勤记录", description = "创建新的考勤记录")
    public ApiResponse addAttendance(@RequestBody AttendanceAddRequest attendanceAddRequest) {
        if (attendanceAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceAddRequest, attendance);
        
        boolean result = attendanceService.save(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(attendance.getId());
    }

    /**
     * 删除考勤记录
     */
    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除考勤记录", description = "根据ID删除考勤记录")
    public ApiResponse deleteAttendance(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = attendanceService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新考勤记录
     */
    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新考勤记录", description = "更新考勤记录信息")
    public ApiResponse updateAttendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest,
                                              HttpServletRequest request) {
        if (attendanceUpdateRequest == null || attendanceUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Attendance attendance = BeanUtil.copyProperties(attendanceUpdateRequest, Attendance.class);
        boolean result = attendanceService.updateById(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取考勤记录
     */
    @GetMapping("/get")
    @Operation(summary = "获取考勤记录", description = "根据ID获取考勤记录详细信息")
    public ApiResponse getAttendanceById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendance);
    }

    /**
     * 根据ID获取考勤记录视图对象
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取考勤记录视图", description = "根据ID获取考勤记录详细信息（包含学生和课程信息）")
    public ApiResponse getAttendanceVOById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendanceService.getAttendanceVO(attendance));
    }

    /**
     * 分页获取考勤记录列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取考勤记录列表", description = "分页获取考勤记录列表（不含学生和课程信息）")
    public ApiResponse listAttendanceByPage(@RequestBody AttendanceQueryRequest attendanceQueryRequest) {
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size), 
                attendanceService.getQueryWrapper(attendanceQueryRequest));
        return ApiResponse.success(attendancePage);
    }

    /**
     * 分页获取考勤记录视图列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    @Operation(summary = "分页获取考勤记录视图列表", description = "分页获取考勤记录列表（包含学生和课程信息）")
    public ApiResponse listAttendanceVOByPage(@RequestBody AttendanceQueryRequest attendanceQueryRequest) {
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size), 
                attendanceService.getQueryWrapper(attendanceQueryRequest));
        Page<AttendanceVO> attendanceVOPage = attendanceService.getAttendanceVOPage(attendancePage);
        return ApiResponse.success(attendanceVOPage);
    }

    // endregion

    // region 特殊功能

    /**
     * 学生签到
     */
    @PostMapping("/signin")
    @Operation(summary = "学生签到", description = "学生进行课程签到")
    public ApiResponse studentSignIn(@RequestParam Long courseId, @RequestParam Long studentId) {
        if (courseId == null || courseId <= 0 || studentId == null || studentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = attendanceService.studentSignIn(courseId, studentId);
        return ApiResponse.success(result);
    }

    /**
     * 批量创建考勤记录
     */
    @PostMapping("/batch/create")
    @ValidateRequest
    @Operation(summary = "批量创建考勤记录", description = "为课程的所有选课学生创建考勤记录")
    public ApiResponse batchCreateAttendance(@RequestParam Long courseId, @RequestParam String attendanceDate) {
        if (courseId == null || courseId <= 0 || attendanceDate == null || attendanceDate.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = attendanceService.batchCreateAttendance(courseId, attendanceDate);
        return ApiResponse.success(result);
    }

    // endregion
}