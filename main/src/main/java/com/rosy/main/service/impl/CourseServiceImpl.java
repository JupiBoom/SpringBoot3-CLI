package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ICourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (courseQueryRequest == null) {
            return queryWrapper;
        }
        
        String courseName = courseQueryRequest.getCourseName();
        Long teacherId = courseQueryRequest.getTeacherId();
        String classroom = courseQueryRequest.getClassroom();
        Byte status = courseQueryRequest.getStatus();
        
        queryWrapper.like(courseName != null && !courseName.isEmpty(), "course_name", courseName);
        queryWrapper.eq(teacherId != null, "teacher_id", teacherId);
        queryWrapper.like(classroom != null && !classroom.isEmpty(), "classroom", classroom);
        queryWrapper.eq(status != null, "status", status);
        
        return queryWrapper;
    }

    @Override
    public CourseVO getCourseVO(Course course) {
        if (course == null) {
            return null;
        }
        
        CourseVO courseVO = BeanUtil.copyProperties(course, CourseVO.class);
        
        // 查询教师信息
        if (course.getTeacherId() != null) {
            Teacher teacher = teacherMapper.selectById(course.getTeacherId());
            if (teacher != null) {
                courseVO.setTeacherName(teacher.getName());
            }
        }
        
        return courseVO;
    }

    @Override
    public Page<CourseVO> getCourseVOPage(Page<Course> coursePage) {
        List<Course> courseList = coursePage.getRecords();
        List<CourseVO> courseVOList = courseList.stream().map(this::getCourseVO).collect(Collectors.toList());
        
        // 批量查询教师信息
        List<Long> teacherIds = courseList.stream()
                .map(Course::getTeacherId)
                .filter(teacherId -> teacherId != null)
                .distinct()
                .collect(Collectors.toList());
        
        if (!teacherIds.isEmpty()) {
            List<Teacher> teacherList = teacherMapper.selectBatchIds(teacherIds);
            Map<Long, String> teacherNameMap = teacherList.stream()
                    .collect(Collectors.toMap(Teacher::getId, Teacher::getName));
            
            courseVOList.forEach(courseVO -> {
                if (courseVO.getTeacherId() != null) {
                    courseVO.setTeacherName(teacherNameMap.get(courseVO.getTeacherId()));
                }
            });
        }
        
        Page<CourseVO> courseVOPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
        courseVOPage.setRecords(courseVOList);
        return courseVOPage;
    }
}