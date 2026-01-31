package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.LeaveApplication;

public interface ILeaveApplicationService extends IService<LeaveApplication> {

    Long submitLeave(LeaveApplication leaveApplication);

    Boolean approveLeave(Long id, Byte approveStatus, String approveNote, Long approverId);

    Boolean deleteLeave(IdRequest idRequest);

    LeaveApplication getLeaveById(Long id);

    Page<LeaveApplication> listLeaves(int current, int size, Long courseId, Long studentId, Byte approveStatus);
}