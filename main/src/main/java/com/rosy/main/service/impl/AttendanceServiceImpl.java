package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.service.IAttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean signIn(Long courseId, Long studentId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId)
               .eq(Attendance::getStudentId, studentId)
               .eq(Attendance::getAttendanceDate, today);
        Attendance exist = this.getOne(wrapper);
        if (exist != null) {
            return true;
        }
        Attendance attendance = new Attendance();
        attendance.setCourseId(courseId);
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(today);
        attendance.setStatus((byte) 1);
        return this.save(attendance);
    }

    @Override
    public List<AttendanceStatisticsVO> getAttendanceStatisticsByCourse(Long courseId) {
        List<Attendance> list = this.list(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getCourseId, courseId));
        return buildStatistics(list);
    }

    @Override
    public List<AttendanceStatisticsVO> getAttendanceStatisticsByStudent(Long studentId) {
        List<Attendance> list = this.list(new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getStudentId, studentId));
        return buildStatistics(list);
    }

    private List<AttendanceStatisticsVO> buildStatistics(List<Attendance> list) {
        List<AttendanceStatisticsVO> result = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return result;
        }
        list.forEach(attendance -> {
            AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
            vo.setCourseId(attendance.getCourseId());
            vo.setStudentId(attendance.getStudentId());
            vo.setAttendanceDate(attendance.getAttendanceDate());
            vo.setStatus(attendance.getStatus());
            result.add(vo);
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveLeave(Long attendanceId, Byte approveStatus) {
        Attendance attendance = this.getById(attendanceId);
        if (attendance == null) {
            return false;
        }
        attendance.setLeaveStatus(approveStatus);
        if (approveStatus == 1) {
            attendance.setStatus((byte) 2);
        }
        return this.updateById(attendance);
    }
}
