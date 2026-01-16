package com.rosy.course.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.Attendance;
import com.rosy.course.mapper.AttendanceMapper;
import com.rosy.course.service.IAttendanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Override
    public boolean signIn(Attendance attendance) {
        // 检查今日是否已签到
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, attendance.getCourseId())
                .eq(Attendance::getStudentId, attendance.getStudentId())
                .ge(Attendance::getSignTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
                .le(Attendance::getSignTime, LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
        
        Attendance exist = this.getOne(wrapper);
        if (ObjectUtil.isNotNull(exist)) {
            return false; // 今日已签到
        }
        
        attendance.setStatus((byte) 0);
        attendance.setSignTime(LocalDateTime.now());
        attendance.setLeaveStatus((byte) 0);
        return this.save(attendance);
    }

    @Override
    public boolean applyLeave(Attendance attendance) {
        attendance.setStatus((byte) 2);
        attendance.setLeaveStatus((byte) 0); // 待审批
        return this.save(attendance);
    }

    @Override
    public boolean approveLeave(Long id, Byte status, Long approverId) {
        Attendance attendance = this.getById(id);
        if (ObjectUtil.isNull(attendance)) {
            return false;
        }
        
        attendance.setLeaveStatus(status);
        attendance.setApproverId(approverId);
        attendance.setApproveTime(LocalDateTime.now());
        
        // 如果批准，更新状态为请假
        if (status == 1) {
            attendance.setStatus((byte) 2);
        }
        // 如果拒绝，状态保持为缺勤
        if (status == 2) {
            attendance.setStatus((byte) 1);
        }
        
        return this.updateById(attendance);
    }

    @Override
    public List<Map<String, Object>> getAttendanceRateByStudent(Long studentId) {
        return this.baseMapper.selectAttendanceRateByStudent(studentId);
    }

    @Override
    public List<Map<String, Object>> getTeachingCountByTeacher(Long teacherId) {
        return this.baseMapper.selectTeachingCountByTeacher(teacherId);
    }

    @Override
    public List<Map<String, Object>> getAbsenteeismByCourse(Long courseId) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId)
                .eq(Attendance::getStatus, (byte) 1); // 缺勤
        
        return this.listMaps(wrapper);
    }

}