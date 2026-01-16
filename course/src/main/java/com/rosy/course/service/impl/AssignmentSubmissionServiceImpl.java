package com.rosy.course.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.AssignmentSubmission;
import com.rosy.course.mapper.AssignmentSubmissionMapper;
import com.rosy.course.service.IAssignmentSubmissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 作业提交表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class AssignmentSubmissionServiceImpl extends ServiceImpl<AssignmentSubmissionMapper, AssignmentSubmission> implements IAssignmentSubmissionService {

    @Override
    public boolean submitAssignment(AssignmentSubmission submission) {
        submission.setStatus((byte) 1);
        submission.setSubmitTime(LocalDateTime.now());
        return this.save(submission);
    }

    @Override
    public boolean gradeAssignment(Long id, Integer score, String evaluation, Long scorerId) {
        AssignmentSubmission submission = this.getById(id);
        if (ObjectUtil.isNull(submission)) {
            return false;
        }
        
        submission.setScore(score);
        submission.setEvaluation(evaluation);
        submission.setScorerId(scorerId);
        submission.setScoreTime(LocalDateTime.now());
        submission.setStatus((byte) 2);
        
        return this.updateById(submission);
    }

}