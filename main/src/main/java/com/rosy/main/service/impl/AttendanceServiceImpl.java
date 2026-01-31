package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.AttendanceStatusEnum;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.service.IAttendanceService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    private final CourseServiceImpl courseService;

    public AttendanceServiceImpl(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @Override
    public Boolean signIn(Long courseId, Long studentId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId);
        wrapper.eq(Attendance::getStudentId, studentId);
        wrapper.eq(Attendance::getAttendDate, today);
        Attendance existing = this.getOne(wrapper);
        
        if (existing != null) {
            existing.setStatus((byte) 0);
            existing.setSignTime(LocalDateTime.now());
            return this.updateById(existing);
        }
        
        Attendance attendance = new Attendance();
        attendance.setCourseId(courseId);
        attendance.setStudentId(studentId);
        attendance.setAttendDate(today);
        attendance.setStatus((byte) 0);
        attendance.setSignTime(LocalDateTime.now());
        return this.save(attendance);
    }

    @Override
    public Boolean updateAttendanceStatus(Long id, Byte status, String remark) {
        Attendance attendance = this.getById(id);
        if (attendance != null) {
            attendance.setStatus(status);
            attendance.setRemark(remark);
            return this.updateById(attendance);
        }
        return false;
    }

    @Override
    public Boolean deleteAttendance(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public Attendance getAttendanceById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<Attendance> listAttendances(int current, int size, Long courseId, Long studentId, LocalDate attendDate) {
        Page<Attendance> page = new Page<>(current, size);
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, Attendance::getCourseId, courseId);
        wrapper.eq(studentId != null, Attendance::getStudentId, studentId);
        wrapper.eq(attendDate != null, Attendance::getAttendDate, attendDate);
        wrapper.orderByDesc(Attendance::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<Map<String, Object>> getAttendanceStatistics(Long courseId) {
        return baseMapper.getAttendanceStatistics(courseId);
    }

    @Override
    public List<AttendanceStatisticsVO> getStudentAttendanceRate(Long studentId) {
        Map<String, Object> result = baseMapper.getStudentAttendanceRate(studentId);
        if (result == null) {
            result = new HashMap<>();
            result.put("studentId", studentId);
            result.put("totalClasses", 0);
            result.put("attendanceRate", 0);
        }
        AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
        vo.setStudentId(studentId);
        vo.setTotalClasses(((Number) result.getOrDefault("totalClasses", 0)).intValue());
        vo.setAttendanceRate(((Number) result.getOrDefault("attendanceRate", 0)).doubleValue());
        return Collections.singletonList(vo);
    }

    @Override
    public LambdaQueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        if (attendanceQueryRequest == null) {
            return wrapper;
        }
        Long id = attendanceQueryRequest.getId();
        Long courseId = attendanceQueryRequest.getCourseId();
        Long studentId = attendanceQueryRequest.getStudentId();
        Integer status = attendanceQueryRequest.getStatus();
        LocalDateTime startTime = attendanceQueryRequest.getStartTime();
        LocalDateTime endTime = attendanceQueryRequest.getEndTime();

        wrapper.eq(ObjectUtils.isNotEmpty(id), Attendance::getId, id);
        wrapper.eq(ObjectUtils.isNotEmpty(courseId), Attendance::getCourseId, courseId);
        wrapper.eq(ObjectUtils.isNotEmpty(studentId), Attendance::getStudentId, studentId);
        wrapper.eq(ObjectUtils.isNotEmpty(status), Attendance::getStatus, status);
        wrapper.ge(ObjectUtils.isNotEmpty(startTime), Attendance::getCreateTime, startTime);
        wrapper.le(ObjectUtils.isNotEmpty(endTime), Attendance::getCreateTime, endTime);

        String sortField = attendanceQueryRequest.getSortField();
        String sortOrder = attendanceQueryRequest.getSortOrder();
        if (StringUtils.isNotBlank(sortField)) {
            wrapper.orderBy(SqlUtils.validSortField(sortField),
                    sortOrder.equals("ascending"),
                    Attendance::getCreateTime);
        }
        return wrapper;
    }

    @Override
    public List<Map<String, Object>> getCourseAttendanceStatistics(Long courseId) {
        return baseMapper.getAttendanceStatistics(courseId);
    }

    @Override
    public List<Attendance> getAbsentRecords(Long courseId) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId);
        wrapper.eq(Attendance::getStatus, AttendanceStatusEnum.ABSENT.getValue());
        return this.list(wrapper);
    }
}