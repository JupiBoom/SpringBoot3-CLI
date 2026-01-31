package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.mapper.HomeworkMapper;
import com.rosy.main.mapper.HomeworkSubmissionMapper;
import com.rosy.main.service.IHomeworkService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {

    private final HomeworkSubmissionMapper homeworkSubmissionMapper;

    public HomeworkServiceImpl(HomeworkSubmissionMapper homeworkSubmissionMapper) {
        this.homeworkSubmissionMapper = homeworkSubmissionMapper;
    }

    @Override
    public Long addHomework(Homework homework) {
        this.save(homework);
        return homework.getId();
    }

    @Override
    public Boolean updateHomework(Homework homework) {
        return this.updateById(homework);
    }

    @Override
    public Boolean deleteHomework(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public Homework getHomeworkById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<Homework> listHomeworks(int current, int size, Long courseId) {
        Page<Homework> page = new Page<>(current, size);
        LambdaQueryWrapper<Homework> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, Homework::getCourseId, courseId);
        wrapper.orderByDesc(Homework::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Long submitHomework(HomeworkSubmission submission) {
        submission.setSubmitTime(LocalDateTime.now());
        submission.setStatus((byte) 1);
        homeworkSubmissionMapper.insert(submission);
        return submission.getId();
    }

    @Override
    public Boolean gradeHomework(Long submissionId, BigDecimal score, String feedback) {
        HomeworkSubmission submission = homeworkSubmissionMapper.selectById(submissionId);
        if (submission != null) {
            submission.setScore(score);
            submission.setFeedback(feedback);
            submission.setStatus((byte) 2);
            return homeworkSubmissionMapper.updateById(submission) > 0;
        }
        return false;
    }

    @Override
    public Page<HomeworkSubmission> listSubmissions(int current, int size, Long homeworkId, Long studentId) {
        Page<HomeworkSubmission> page = new Page<>(current, size);
        LambdaQueryWrapper<HomeworkSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(homeworkId != null, HomeworkSubmission::getHomeworkId, homeworkId);
        wrapper.eq(studentId != null, HomeworkSubmission::getStudentId, studentId);
        wrapper.orderByDesc(HomeworkSubmission::getSubmitTime);
        return homeworkSubmissionMapper.selectPage(page, wrapper);
    }
}