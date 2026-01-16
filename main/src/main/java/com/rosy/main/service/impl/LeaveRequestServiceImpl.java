package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.LeaveRequestMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ILeaveRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements ILeaveRequestService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }
        LeaveRequestVO leaveRequestVO = BeanUtil.copyProperties(leaveRequest, LeaveRequestVO.class);
        if (leaveRequest.getStudentId() != null) {
            Student student = studentMapper.selectById(leaveRequest.getStudentId());
            if (student != null) {
                leaveRequestVO.setStudentName(student.getName());
            }
        }
        if (leaveRequest.getCourseId() != null) {
            Course course = courseMapper.selectById(leaveRequest.getCourseId());
            if (course != null) {
                leaveRequestVO.setCourseName(course.getName());
            }
        }
        if (leaveRequest.getApproverId() != null) {
            Teacher teacher = teacherMapper.selectById(leaveRequest.getApproverId());
            if (teacher != null) {
                leaveRequestVO.setApproverName(teacher.getName());
            }
        }
        return leaveRequestVO;
    }

    @Override
    public LambdaQueryWrapper<LeaveRequest> getQueryWrapper(Long studentId, Long courseId, Byte status) {
        LambdaQueryWrapper<LeaveRequest> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(studentId)) {
            queryWrapper.eq(LeaveRequest::getStudentId, studentId);
        }
        if (ObjectUtil.isNotEmpty(courseId)) {
            queryWrapper.eq(LeaveRequest::getCourseId, courseId);
        }
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(LeaveRequest::getStatus, status);
        }
        queryWrapper.orderByDesc(LeaveRequest::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitLeaveRequest(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate, String reason) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        if (startDate.isAfter(endDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始日期不能晚于结束日期");
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStudentId(studentId);
        leaveRequest.setCourseId(courseId);
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setReason(reason);
        leaveRequest.setStatus((byte) 0);
        return this.save(leaveRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveLeaveRequest(Long requestId, Long approverId, Byte status, String remark) {
        LeaveRequest leaveRequest = this.getById(requestId);
        if (leaveRequest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请假申请不存在");
        }
        if (!leaveRequest.getStatus().equals((byte) 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该申请已处理");
        }
        Teacher teacher = teacherMapper.selectById(approverId);
        if (teacher == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "审批人不存在");
        }
        leaveRequest.setStatus(status);
        leaveRequest.setApproverId(approverId);
        leaveRequest.setApproveTime(LocalDateTime.now());
        leaveRequest.setApproveRemark(remark);
        return this.updateById(leaveRequest);
    }

    @Override
    public List<LeaveRequestVO> getPendingRequests(Long courseId) {
        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveRequest::getStatus, (byte) 0);
        if (courseId != null) {
            wrapper.eq(LeaveRequest::getCourseId, courseId);
        }
        return this.list(wrapper).stream()
                .map(this::getLeaveRequestVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestVO> getStudentLeaveRequests(Long studentId) {
        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveRequest::getStudentId, studentId);
        wrapper.orderByDesc(LeaveRequest::getCreateTime);
        return this.list(wrapper).stream()
                .map(this::getLeaveRequestVO)
                .collect(Collectors.toList());
    }
}
