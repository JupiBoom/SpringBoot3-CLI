package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseStudent;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseStudentMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private CourseStudentMapper courseStudentMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public CourseVO getCourseVO(Course course) {
        if (course == null) {
            return null;
        }
        CourseVO courseVO = BeanUtil.copyProperties(course, CourseVO.class);
        if (course.getTeacherId() != null) {
            Teacher teacher = teacherMapper.selectById(course.getTeacherId());
            if (teacher != null) {
                courseVO.setTeacherName(teacher.getName());
            }
        }
        return courseVO;
    }

    @Override
    public LambdaQueryWrapper<Course> getQueryWrapper(String name, Long teacherId, Byte status) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(name)) {
            queryWrapper.like(Course::getName, name);
        }
        if (ObjectUtil.isNotEmpty(teacherId)) {
            queryWrapper.eq(Course::getTeacherId, teacherId);
        }
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(Course::getStatus, status);
        }
        queryWrapper.orderByDesc(Course::getCreateTime);
        return queryWrapper;
    }

    @Override
    public CourseVO getCourseDetail(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        return getCourseVO(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addStudentToCourse(Long courseId, Long studentId) {
        Course course = this.getById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "课程已满员");
        }
        LambdaQueryWrapper<CourseStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getStudentId, studentId);
        if (courseStudentMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "学生已选该课程");
        }
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(courseId);
        courseStudent.setStudentId(studentId);
        courseStudentMapper.insert(courseStudent);
        course.setCurrentStudents(course.getCurrentStudents() + 1);
        return this.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = this.getById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        LambdaQueryWrapper<CourseStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getStudentId, studentId);
        if (courseStudentMapper.selectCount(wrapper) == 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "学生未选该课程");
        }
        courseStudentMapper.delete(wrapper);
        course.setCurrentStudents(Math.max(0, course.getCurrentStudents() - 1));
        return this.updateById(course);
    }
}
