package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.LeaveApplication;
import com.rosy.main.mapper.LeaveApplicationMapper;
import com.rosy.main.service.ILeaveApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeaveApplicationServiceImpl extends ServiceImpl<LeaveApplicationMapper, LeaveApplication> implements ILeaveApplicationService {

    @Override
    public Long submitLeave(LeaveApplication leaveApplication) {
        leaveApplication.setApproveStatus((byte) 0);
        this.save(leaveApplication);
        return leaveApplication.getId();
    }

    @Override
    public Boolean approveLeave(Long id, Byte approveStatus, String approveNote, Long approverId) {
        LeaveApplication leave = this.getById(id);
        if (leave != null) {
            leave.setApproveStatus(approveStatus);
            leave.setApproverId(approverId);
            leave.setApproveTime(LocalDateTime.now());
            leave.setApproveNote(approveNote);
            return this.updateById(leave);
        }
        return false;
    }

    @Override
    public Boolean deleteLeave(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public LeaveApplication getLeaveById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<LeaveApplication> listLeaves(int current, int size, Long courseId, Long studentId, Byte approveStatus) {
        Page<LeaveApplication> page = new Page<>(current, size);
        LambdaQueryWrapper<LeaveApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, LeaveApplication::getCourseId, courseId);
        wrapper.eq(studentId != null, LeaveApplication::getStudentId, studentId);
        wrapper.eq(approveStatus != null, LeaveApplication::getApproveStatus, approveStatus);
        wrapper.orderByDesc(LeaveApplication::getCreateTime);
        return this.page(page, wrapper);
    }
}