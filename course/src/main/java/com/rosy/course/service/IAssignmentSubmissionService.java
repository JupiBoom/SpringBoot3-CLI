package com.rosy.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.course.domain.entity.AssignmentSubmission;

/**
 * <p>
 * 作业提交表 Service 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
public interface IAssignmentSubmissionService extends IService<AssignmentSubmission> {

    /**
     * 提交作业
     */
    boolean submitAssignment(AssignmentSubmission submission);

    /**
     * 评分作业
     */
    boolean gradeAssignment(Long id, Integer score, String evaluation, Long scorerId);

}