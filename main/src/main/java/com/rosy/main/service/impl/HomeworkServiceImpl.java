package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.vo.HomeworkVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.HomeworkMapper;
import com.rosy.main.service.IHomeworkService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public HomeworkVO getHomeworkVO(Homework homework) {
        if (homework == null) {
            return null;
        }
        HomeworkVO homeworkVO = BeanUtil.copyProperties(homework, HomeworkVO.class);
        if (homework.getCourseId() != null) {
            Course course = courseMapper.selectById(homework.getCourseId());
            if (course != null) {
                homeworkVO.setCourseName(course.getName());
            }
        }
        return homeworkVO;
    }

    @Override
    public LambdaQueryWrapper<Homework> getQueryWrapper(Long courseId, String title) {
        LambdaQueryWrapper<Homework> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(courseId)) {
            queryWrapper.eq(Homework::getCourseId, courseId);
        }
        if (ObjectUtil.isNotEmpty(title)) {
            queryWrapper.like(Homework::getTitle, title);
        }
        queryWrapper.orderByDesc(Homework::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean createHomework(Long courseId, String title, String description, String deadline) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        Homework homework = new Homework();
        homework.setCourseId(courseId);
        homework.setTitle(title);
        homework.setDescription(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        homework.setDeadline(LocalDateTime.parse(deadline, formatter));
        return this.save(homework);
    }

    @Override
    public boolean updateHomework(Long id, String title, String description, String deadline) {
        Homework homework = this.getById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业不存在");
        }
        if (title != null) {
            homework.setTitle(title);
        }
        if (description != null) {
            homework.setDescription(description);
        }
        if (deadline != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            homework.setDeadline(LocalDateTime.parse(deadline, formatter));
        }
        return this.updateById(homework);
    }

    @Override
    public boolean deleteHomework(Long id) {
        Homework homework = this.getById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业不存在");
        }
        return this.removeById(id);
    }

    @Override
    public List<HomeworkVO> getHomeworksByCourse(Long courseId) {
        LambdaQueryWrapper<Homework> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homework::getCourseId, courseId);
        wrapper.orderByDesc(Homework::getCreateTime);
        return this.list(wrapper).stream()
                .map(this::getHomeworkVO)
                .collect(Collectors.toList());
    }
}
