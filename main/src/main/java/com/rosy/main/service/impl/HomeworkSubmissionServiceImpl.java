package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.HomeworkSubmissionVO;
import com.rosy.main.mapper.HomeworkMapper;
import com.rosy.main.mapper.HomeworkSubmissionMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IHomeworkSubmissionService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeworkSubmissionServiceImpl extends ServiceImpl<HomeworkSubmissionMapper, HomeworkSubmission> implements IHomeworkSubmissionService {

    @Resource
    private HomeworkMapper homeworkMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public HomeworkSubmissionVO getHomeworkSubmissionVO(HomeworkSubmission submission) {
        if (submission == null) {
            return null;
        }
        HomeworkSubmissionVO submissionVO = BeanUtil.copyProperties(submission, HomeworkSubmissionVO.class);
        if (submission.getHomeworkId() != null) {
            Homework homework = homeworkMapper.selectById(submission.getHomeworkId());
            if (homework != null) {
                submissionVO.setHomeworkTitle(homework.getTitle());
            }
        }
        if (submission.getStudentId() != null) {
            Student student = studentMapper.selectById(submission.getStudentId());
            if (student != null) {
                submissionVO.setStudentName(student.getName());
            }
        }
        return submissionVO;
    }

    @Override
    public LambdaQueryWrapper<HomeworkSubmission> getQueryWrapper(Long homeworkId, Long studentId) {
        LambdaQueryWrapper<HomeworkSubmission> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(homeworkId)) {
            queryWrapper.eq(HomeworkSubmission::getHomeworkId, homeworkId);
        }
        if (ObjectUtil.isNotEmpty(studentId)) {
            queryWrapper.eq(HomeworkSubmission::getStudentId, studentId);
        }
        queryWrapper.orderByDesc(HomeworkSubmission::getSubmitTime);
        return queryWrapper;
    }

    @Override
    public boolean submitHomework(Long homeworkId, Long studentId, String content, String fileUrl) {
        Homework homework = homeworkMapper.selectById(homeworkId);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业不存在");
        }
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        LambdaQueryWrapper<HomeworkSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomeworkSubmission::getHomeworkId, homeworkId)
                .eq(HomeworkSubmission::getStudentId, studentId);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已提交过该作业");
        }
        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setHomeworkId(homeworkId);
        submission.setStudentId(studentId);
        submission.setContent(content);
        submission.setFileUrl(fileUrl);
        return this.save(submission);
    }

    @Override
    public boolean gradeHomework(Long submissionId, String score, String feedback) {
        HomeworkSubmission submission = this.getById(submissionId);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业提交不存在");
        }
        if (score != null) {
            submission.setScore(new BigDecimal(score));
        }
        if (feedback != null) {
            submission.setFeedback(feedback);
        }
        return this.updateById(submission);
    }

    @Override
    public List<HomeworkSubmissionVO> getSubmissionsByHomework(Long homeworkId) {
        LambdaQueryWrapper<HomeworkSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomeworkSubmission::getHomeworkId, homeworkId);
        wrapper.orderByDesc(HomeworkSubmission::getSubmitTime);
        return this.list(wrapper).stream()
                .map(this::getHomeworkSubmissionVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HomeworkSubmissionVO> getSubmissionsByStudent(Long studentId) {
        LambdaQueryWrapper<HomeworkSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomeworkSubmission::getStudentId, studentId);
        wrapper.orderByDesc(HomeworkSubmission::getSubmitTime);
        return this.list(wrapper).stream()
                .map(this::getHomeworkSubmissionVO)
                .collect(Collectors.toList());
    }
}
