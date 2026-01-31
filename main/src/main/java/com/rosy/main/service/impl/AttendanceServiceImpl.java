package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseSelection;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseSelectionMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IAttendanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseSelectionMapper courseSelectionMapper;

    @Override
    public QueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        if (attendanceQueryRequest == null) {
            return queryWrapper;
        }
        
        Long courseId = attendanceQueryRequest.getCourseId();
        Long studentId = attendanceQueryRequest.getStudentId();
        LocalDate startDate = attendanceQueryRequest.getStartDate();
        LocalDate endDate = attendanceQueryRequest.getEndDate();
        Byte status = attendanceQueryRequest.getStatus();
        
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.eq(studentId != null, "student_id", studentId);
        queryWrapper.ge(startDate != null, "attendance_date", startDate);
        queryWrapper.le(endDate != null, "attendance_date", endDate);
        queryWrapper.eq(status != null, "status", status);
        
        return queryWrapper;
    }

    @Override
    public AttendanceVO getAttendanceVO(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        
        AttendanceVO attendanceVO = BeanUtil.copyProperties(attendance, AttendanceVO.class);
        
        // 查询学生信息
        if (attendance.getStudentId() != null) {
            Student student = studentMapper.selectById(attendance.getStudentId());
            if (student != null) {
                attendanceVO.setStudentName(student.getName());
                attendanceVO.setStudentNo(student.getStudentNo());
            }
        }
        
        // 查询课程信息
        if (attendance.getCourseId() != null) {
            Course course = courseMapper.selectById(attendance.getCourseId());
            if (course != null) {
                attendanceVO.setCourseName(course.getCourseName());
            }
        }
        
        // 设置状态描述
        Byte status = attendance.getStatus();
        if (status != null) {
            switch (status) {
                case 0:
                    attendanceVO.setStatusDesc("缺勤");
                    break;
                case 1:
                    attendanceVO.setStatusDesc("出勤");
                    break;
                case 2:
                    attendanceVO.setStatusDesc("请假");
                    break;
                default:
                    attendanceVO.setStatusDesc("未知");
                    break;
            }
        }
        
        return attendanceVO;
    }

    @Override
    public Page<AttendanceVO> getAttendanceVOPage(Page<Attendance> attendancePage) {
        List<Attendance> attendanceList = attendancePage.getRecords();
        List<AttendanceVO> attendanceVOList = attendanceList.stream()
                .map(this::getAttendanceVO)
                .collect(Collectors.toList());
        
        // 批量查询学生信息
        List<Long> studentIds = attendanceList.stream()
                .map(Attendance::getStudentId)
                .filter(studentId -> studentId != null)
                .distinct()
                .collect(Collectors.toList());
        
        final Map<Long, String> studentNameMap;
        final Map<Long, String> studentNoMap;
        
        if (!studentIds.isEmpty()) {
            List<Student> studentList = studentMapper.selectBatchIds(studentIds);
            studentNameMap = studentList.stream()
                    .collect(Collectors.toMap(Student::getId, Student::getName));
            studentNoMap = studentList.stream()
                    .collect(Collectors.toMap(Student::getId, Student::getStudentNo));
        } else {
            studentNameMap = Map.of();
            studentNoMap = Map.of();
        }
        
        // 批量查询课程信息
        List<Long> courseIds = attendanceList.stream()
                .map(Attendance::getCourseId)
                .filter(courseId -> courseId != null)
                .distinct()
                .collect(Collectors.toList());
        
        final Map<Long, String> courseNameMap;
        if (!courseIds.isEmpty()) {
            List<Course> courseList = courseMapper.selectBatchIds(courseIds);
            courseNameMap = courseList.stream()
                    .collect(Collectors.toMap(Course::getId, Course::getCourseName));
        } else {
            courseNameMap = Map.of();
        }
        
        // 设置关联信息
        attendanceVOList.forEach(attendanceVO -> {
            if (attendanceVO.getStudentId() != null) {
                attendanceVO.setStudentName(studentNameMap.get(attendanceVO.getStudentId()));
                attendanceVO.setStudentNo(studentNoMap.get(attendanceVO.getStudentId()));
            }
            if (attendanceVO.getCourseId() != null) {
                attendanceVO.setCourseName(courseNameMap.get(attendanceVO.getCourseId()));
            }
            
            // 设置状态描述
            Byte status = attendanceVO.getStatus();
            if (status != null) {
                switch (status) {
                    case 0:
                        attendanceVO.setStatusDesc("缺勤");
                        break;
                    case 1:
                        attendanceVO.setStatusDesc("出勤");
                        break;
                    case 2:
                        attendanceVO.setStatusDesc("请假");
                        break;
                    default:
                        attendanceVO.setStatusDesc("未知");
                        break;
                }
            }
        });
        
        Page<AttendanceVO> attendanceVOPage = new Page<>(attendancePage.getCurrent(), attendancePage.getSize(), attendancePage.getTotal());
        attendanceVOPage.setRecords(attendanceVOList);
        return attendanceVOPage;
    }

    @Override
    @Transactional
    public boolean studentSignIn(Long courseId, Long studentId) {
        // 检查学生是否选了这门课
        QueryWrapper<CourseSelection> selectionQueryWrapper = new QueryWrapper<>();
        selectionQueryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId)
                .eq("status", 1);
        CourseSelection courseSelection = courseSelectionMapper.selectOne(selectionQueryWrapper);
        if (courseSelection == null) {
            throw new RuntimeException("学生未选择该课程");
        }
        
        // 检查今天是否已经有考勤记录
        QueryWrapper<Attendance> attendanceQueryWrapper = new QueryWrapper<>();
        attendanceQueryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId)
                .eq("attendance_date", LocalDate.now());
        Attendance existingAttendance = this.getOne(attendanceQueryWrapper);
        
        if (existingAttendance != null) {
            // 更新已有记录为出勤
            existingAttendance.setStatus((byte) 1);
            return this.updateById(existingAttendance);
        } else {
            // 创建新的考勤记录
            Attendance attendance = new Attendance();
            attendance.setCourseId(courseId);
            attendance.setStudentId(studentId);
            attendance.setAttendanceDate(LocalDate.now());
            attendance.setStatus((byte) 1); // 出勤
            return this.save(attendance);
        }
    }

    @Override
    @Transactional
    public boolean batchCreateAttendance(Long courseId, String attendanceDate) {
        LocalDate date = LocalDate.parse(attendanceDate);
        
        // 查询所有选择该课程的学生
        QueryWrapper<CourseSelection> selectionQueryWrapper = new QueryWrapper<>();
        selectionQueryWrapper.eq("course_id", courseId).eq("status", 1);
        List<CourseSelection> selections = courseSelectionMapper.selectList(selectionQueryWrapper);
        
        for (CourseSelection selection : selections) {
            // 检查是否已存在考勤记录
            QueryWrapper<Attendance> attendanceQueryWrapper = new QueryWrapper<>();
            attendanceQueryWrapper.eq("course_id", courseId)
                    .eq("student_id", selection.getStudentId())
                    .eq("attendance_date", date);
            Attendance existingAttendance = this.getOne(attendanceQueryWrapper);
            
            if (existingAttendance == null) {
                // 创建考勤记录，默认为缺勤
                Attendance attendance = new Attendance();
                attendance.setCourseId(courseId);
                attendance.setStudentId(selection.getStudentId());
                attendance.setAttendanceDate(date);
                attendance.setStatus((byte) 0); // 缺勤
                this.save(attendance);
            }
        }
        
        return true;
    }
}