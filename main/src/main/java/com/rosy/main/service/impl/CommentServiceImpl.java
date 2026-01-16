package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Comment;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.CommentVO;
import com.rosy.main.mapper.CommentMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ICommentService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public CommentVO getCommentVO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);
        if (comment.getCourseId() != null) {
            Course course = courseMapper.selectById(comment.getCourseId());
            if (course != null) {
                commentVO.setCourseName(course.getName());
            }
        }
        if (comment.getUserId() != null) {
            if (comment.getUserType() == 0) {
                Student student = studentMapper.selectById(comment.getUserId());
                if (student != null) {
                    commentVO.setUserName(student.getName());
                }
            } else {
                Teacher teacher = teacherMapper.selectById(comment.getUserId());
                if (teacher != null) {
                    commentVO.setUserName(teacher.getName());
                }
            }
        }
        return commentVO;
    }

    @Override
    public LambdaQueryWrapper<Comment> getQueryWrapper(Long courseId, Long userId, Long parentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(courseId)) {
            queryWrapper.eq(Comment::getCourseId, courseId);
        }
        if (ObjectUtil.isNotEmpty(userId)) {
            queryWrapper.eq(Comment::getUserId, userId);
        }
        if (ObjectUtil.isNotEmpty(parentId)) {
            queryWrapper.eq(Comment::getParentId, parentId);
        }
        queryWrapper.orderByDesc(Comment::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean addComment(Long courseId, Long userId, Byte userType, String content, Long parentId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        if (userType == 0) {
            Student student = studentMapper.selectById(userId);
            if (student == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
            }
        } else {
            Teacher teacher = teacherMapper.selectById(userId);
            if (teacher == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "教师不存在");
            }
        }
        if (parentId != null) {
            Comment parentComment = this.getById(parentId);
            if (parentComment == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "父评论不存在");
            }
        }
        Comment comment = new Comment();
        comment.setCourseId(courseId);
        comment.setUserId(userId);
        comment.setUserType(userType);
        comment.setContent(content);
        comment.setParentId(parentId);
        return this.save(comment);
    }

    @Override
    public boolean deleteComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        return this.removeById(id);
    }

    @Override
    public List<CommentVO> getCommentsByCourse(Long courseId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCourseId, courseId);
        wrapper.orderByDesc(Comment::getCreateTime);
        return this.list(wrapper).stream()
                .map(this::getCommentVO)
                .collect(Collectors.toList());
    }
}
