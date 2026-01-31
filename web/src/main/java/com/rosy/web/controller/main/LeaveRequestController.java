package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.leave.LeaveAddRequest;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.service.ILeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 请假表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/leave")
@Tag(name = "请假管理", description = "请假相关接口")
public class LeaveRequestController {
    
    @Resource
    private ILeaveRequestService leaveRequestService;

    // region 增删改查

    /**
     * 创建请假记录
     */
    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建请假记录", description = "学生提交请假申请")
    public ApiResponse<Long> addLeaveRequest(@RequestBody LeaveAddRequest leaveAddRequest) {
        if (leaveAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        LeaveRequest leaveRequest = new LeaveRequest();
        BeanUtils.copyProperties(leaveAddRequest, leaveRequest);
        leaveRequest.setStatus((byte) 0); // 待审批
        
        boolean result = leaveRequestService.save(leaveRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(leaveRequest.getId());
    }

    /**
     * 删除请假记录
     */
    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除请假记录", description = "根据ID删除请假记录")
    public ApiResponse<Boolean> deleteLeaveRequest(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = leaveRequestService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取请假记录
     */
    @GetMapping("/get")
    @Operation(summary = "获取请假记录", description = "根据ID获取请假记录详细信息")
    public ApiResponse<LeaveRequest> getLeaveRequestById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        LeaveRequest leaveRequest = leaveRequestService.getById(id);
        ThrowUtils.throwIf(leaveRequest == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(leaveRequest);
    }

    /**
     * 根据ID获取请假记录视图对象
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取请假记录视图", description = "根据ID获取请假记录详细信息（包含学生和课程信息）")
    public ApiResponse<LeaveRequestVO> getLeaveRequestVOById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        LeaveRequest leaveRequest = leaveRequestService.getById(id);
        ThrowUtils.throwIf(leaveRequest == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(leaveRequestService.getLeaveRequestVO(leaveRequest));
    }

    /**
     * 分页获取请假记录列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取请假记录列表", description = "分页获取请假记录列表（不含学生和课程信息）")
    public ApiResponse<Page<LeaveRequest>> listLeaveRequestByPage(@RequestBody LeaveQueryRequest leaveQueryRequest) {
        long current = leaveQueryRequest.getCurrent();
        long size = leaveQueryRequest.getPageSize();
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.page(new Page<>(current, size), 
                leaveRequestService.getQueryWrapper(leaveQueryRequest));
        return ApiResponse.success(leaveRequestPage);
    }

    /**
     * 分页获取请假记录视图列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    @Operation(summary = "分页获取请假记录视图列表", description = "分页获取请假记录列表（包含学生和课程信息）")
    public ApiResponse<Page<LeaveRequestVO>> listLeaveRequestVOByPage(@RequestBody LeaveQueryRequest leaveQueryRequest) {
        long current = leaveQueryRequest.getCurrent();
        long size = leaveQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.page(new Page<>(current, size), 
                leaveRequestService.getQueryWrapper(leaveQueryRequest));
        Page<LeaveRequestVO> leaveRequestVOPage = leaveRequestService.getLeaveRequestVOPage(leaveRequestPage);
        return ApiResponse.success(leaveRequestVOPage);
    }

    // endregion

    // region 特殊功能

    /**
     * 审批请假
     */
    @PostMapping("/approve")
    @ValidateRequest
    @Operation(summary = "审批请假", description = "教师审批学生请假申请")
    public ApiResponse<Boolean> approveLeave(@RequestBody LeaveApproveRequest leaveApproveRequest,
                                          @RequestParam Long approverId) {
        if (leaveApproveRequest == null || leaveApproveRequest.getId() == null || approverId == null || approverId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = leaveRequestService.approveLeave(leaveApproveRequest, approverId);
        return ApiResponse.success(result);
    }

    /**
     * 学生请假
     */
    @PostMapping("/student/leave")
    @Operation(summary = "学生请假", description = "学生提交请假申请")
    public ApiResponse<Boolean> studentLeave(@RequestParam Long studentId,
                                          @RequestParam Long courseId,
                                          @RequestParam String startDate,
                                          @RequestParam String endDate,
                                          @RequestParam String reason) {
        if (studentId == null || studentId <= 0 || courseId == null || courseId <= 0 || 
            startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty() || reason == null || reason.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = leaveRequestService.studentLeave(studentId, courseId, startDate, endDate, reason);
        return ApiResponse.success(result);
    }

    // endregion
}