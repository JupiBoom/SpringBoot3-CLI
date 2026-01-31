package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseSelection;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseSelectionMapper;
import com.rosy.main.mapper.LeaveRequestMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ILeaveRequestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 请假表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements ILeaveRequestService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private CourseSelectionMapper courseSelectionMapper;

    @Resource
    private com.rosy.main.service.IAttendanceService attendanceService;

    @Override
    public QueryWrapper<LeaveRequest> getQueryWrapper(LeaveQueryRequest leaveQueryRequest) {
        QueryWrapper<LeaveRequest> queryWrapper = new QueryWrapper<>();
        if (leaveQueryRequest == null) {
            return queryWrapper;
        }
        
        Long studentId = leaveQueryRequest.getStudentId();
        Long courseId = leaveQueryRequest.getCourseId();
        Byte status = leaveQueryRequest.getStatus();
        LocalDate startDate = leaveQueryRequest.getStartDate();
        LocalDate endDate = leaveQueryRequest.getEndDate();
        
        queryWrapper.eq(studentId != null, "student_id", studentId);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.ge(startDate != null, "start_date", startDate);
        queryWrapper.le(endDate != null, "end_date", endDate);
        
        return queryWrapper;
    }

    @Override
    public LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }
        
        LeaveRequestVO leaveRequestVO = BeanUtil.copyProperties(leaveRequest, LeaveRequestVO.class);
        
        // 查询学生信息
        if (leaveRequest.getStudentId() != null) {
            Student student = studentMapper.selectById(leaveRequest.getStudentId());
            if (student != null) {
                leaveRequestVO.setStudentName(student.getName());
                leaveRequestVO.setStudentNo(student.getStudentNo());
            }
        }
        
        // 查询课程信息
        if (leaveRequest.getCourseId() != null) {
            Course course = courseMapper.selectById(leaveRequest.getCourseId());
            if (course != null) {
                leaveRequestVO.setCourseName(course.getCourseName());
            }
        }
        
        // 查询审批人信息
        if (leaveRequest.getApproverId() != null) {
            Teacher teacher = teacherMapper.selectById(leaveRequest.getApproverId());
            if (teacher != null) {
                leaveRequestVO.setApproverName(teacher.getName());
            }
        }
        
        // 设置状态描述
        Byte status = leaveRequest.getStatus();
        if (status != null) {
            switch (status) {
                case 0:
                    leaveRequestVO.setStatusDesc("待审批");
                    break;
                case 1:
                    leaveRequestVO.setStatusDesc("已批准");
                    break;
                case 2:
                    leaveRequestVO.setStatusDesc("已拒绝");
                    break;
                default:
                    leaveRequestVO.setStatusDesc("未知");
                    break;
            }
        }
        
        return leaveRequestVO;
    }

    @Override
    public Page<LeaveRequestVO> getLeaveRequestVOPage(Page<LeaveRequest> leaveRequestPage) {
        List<LeaveRequest> leaveRequestList = leaveRequestPage.getRecords();
        List<LeaveRequestVO> leaveRequestVOList = leaveRequestList.stream()
                .map(this::getLeaveRequestVO)
                .collect(Collectors.toList());
        
        // 批量查询学生信息
        List<Long> studentIds = leaveRequestList.stream()
                .map(LeaveRequest::getStudentId)
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
        List<Long> courseIds = leaveRequestList.stream()
                .map(LeaveRequest::getCourseId)
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
        
        // 批量查询审批人信息
        List<Long> approverIds = leaveRequestList.stream()
                .map(LeaveRequest::getApproverId)
                .filter(approverId -> approverId != null)
                .distinct()
                .collect(Collectors.toList());
        
        final Map<Long, String> approverNameMap;
        if (!approverIds.isEmpty()) {
            List<Teacher> teacherList = teacherMapper.selectBatchIds(approverIds);
            approverNameMap = teacherList.stream()
                    .collect(Collectors.toMap(Teacher::getId, Teacher::getName));
        } else {
            approverNameMap = Map.of();
        }
        
        // 设置关联信息
        leaveRequestVOList.forEach(leaveRequestVO -> {
            if (leaveRequestVO.getStudentId() != null) {
                leaveRequestVO.setStudentName(studentNameMap.get(leaveRequestVO.getStudentId()));
                leaveRequestVO.setStudentNo(studentNoMap.get(leaveRequestVO.getStudentId()));
            }
            if (leaveRequestVO.getCourseId() != null) {
                leaveRequestVO.setCourseName(courseNameMap.get(leaveRequestVO.getCourseId()));
            }
            if (leaveRequestVO.getApproverId() != null) {
                leaveRequestVO.setApproverName(approverNameMap.get(leaveRequestVO.getApproverId()));
            }
            
            // 设置状态描述
            Byte status = leaveRequestVO.getStatus();
            if (status != null) {
                switch (status) {
                    case 0:
                        leaveRequestVO.setStatusDesc("待审批");
                        break;
                    case 1:
                        leaveRequestVO.setStatusDesc("已批准");
                        break;
                    case 2:
                        leaveRequestVO.setStatusDesc("已拒绝");
                        break;
                    default:
                        leaveRequestVO.setStatusDesc("未知");
                        break;
                }
            }
        });
        
        Page<LeaveRequestVO> leaveRequestVOPage = new Page<>(leaveRequestPage.getCurrent(), leaveRequestPage.getSize(), leaveRequestPage.getTotal());
        leaveRequestVOPage.setRecords(leaveRequestVOList);
        return leaveRequestVOPage;
    }

    @Override
    @Transactional
    public boolean approveLeave(LeaveApproveRequest leaveApproveRequest, Long approverId) {
        if (leaveApproveRequest == null || leaveApproveRequest.getId() == null) {
            return false;
        }
        
        LeaveRequest leaveRequest = this.getById(leaveApproveRequest.getId());
        if (leaveRequest == null) {
            return false;
        }
        
        // 更新请假状态
        leaveRequest.setStatus(leaveApproveRequest.getStatus());
        leaveRequest.setApproverId(approverId);
        leaveRequest.setApproveTime(LocalDateTime.now());
        leaveRequest.setApproveRemark(leaveApproveRequest.getApproveRemark());
        
        boolean result = this.updateById(leaveRequest);
        
        // 如果批准请假，则更新考勤记录为请假状态
        if (result && leaveApproveRequest.getStatus() == 1) {
            LocalDate startDate = leaveRequest.getStartDate();
            LocalDate endDate = leaveRequest.getEndDate();
            
            while (!startDate.isAfter(endDate)) {
                // 检查是否有考勤记录
                QueryWrapper<com.rosy.main.domain.entity.Attendance> attendanceQueryWrapper = new QueryWrapper<>();
                attendanceQueryWrapper.eq("course_id", leaveRequest.getCourseId())
                        .eq("student_id", leaveRequest.getStudentId())
                        .eq("attendance_date", startDate);
                com.rosy.main.domain.entity.Attendance attendance = attendanceService.getOne(attendanceQueryWrapper);
                
                if (attendance != null) {
                    // 更新为请假状态
                    attendance.setStatus((byte) 2);
                    attendanceService.updateById(attendance);
                } else {
                    // 创建新的考勤记录，状态为请假
                    attendance = new com.rosy.main.domain.entity.Attendance();
                    attendance.setCourseId(leaveRequest.getCourseId());
                    attendance.setStudentId(leaveRequest.getStudentId());
                    attendance.setAttendanceDate(startDate);
                    attendance.setStatus((byte) 2); // 请假
                    attendanceService.save(attendance);
                }
                
                startDate = startDate.plusDays(1);
            }
        }
        
        return result;
    }

    @Override
    @Transactional
    public boolean studentLeave(Long studentId, Long courseId, String startDate, String endDate, String reason) {
        // 检查学生是否选了这门课
        QueryWrapper<CourseSelection> selectionQueryWrapper = new QueryWrapper<>();
        selectionQueryWrapper.eq("course_id", courseId)
                .eq("student_id", studentId)
                .eq("status", 1);
        CourseSelection courseSelection = courseSelectionMapper.selectOne(selectionQueryWrapper);
        if (courseSelection == null) {
            throw new RuntimeException("学生未选择该课程");
        }
        
        // 创建请假记录
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStudentId(studentId);
        leaveRequest.setCourseId(courseId);
        leaveRequest.setStartDate(LocalDate.parse(startDate));
        leaveRequest.setEndDate(LocalDate.parse(endDate));
        leaveRequest.setReason(reason);
        leaveRequest.setStatus((byte) 0); // 待审批
        
        return this.save(leaveRequest);
    }
}