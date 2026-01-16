package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.vo.LeaveRequestVO;

import java.time.LocalDate;
import java.util.List;

public interface ILeaveRequestService extends IService<LeaveRequest> {

    LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest);

    LambdaQueryWrapper<LeaveRequest> getQueryWrapper(Long studentId, Long courseId, Byte status);

    boolean submitLeaveRequest(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate, String reason);

    boolean approveLeaveRequest(Long requestId, Long approverId, Byte status, String remark);

    List<LeaveRequestVO> getPendingRequests(Long courseId);

    List<LeaveRequestVO> getStudentLeaveRequests(Long studentId);
}
