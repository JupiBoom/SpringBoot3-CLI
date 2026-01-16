package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.HomeworkSubmissionVO;
import com.rosy.main.service.IHomeworkSubmissionService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/homeworkSubmission")
public class HomeworkSubmissionController {

    @Resource
    private IHomeworkSubmissionService homeworkSubmissionService;

    @PostMapping("/submit")
    public BaseResponse<Long> submitHomework(
            @RequestParam Long homeworkId,
            @RequestParam Long studentId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String fileUrl) {
        if (homeworkId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkSubmissionService.submitHomework(homeworkId, studentId, content, fileUrl);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(1L);
    }

    @PostMapping("/grade")
    public BaseResponse<Boolean> gradeHomework(
            @RequestParam Long submissionId,
            @RequestParam(required = false) String score,
            @RequestParam(required = false) String feedback) {
        if (submissionId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkSubmissionService.gradeHomework(submissionId, score, feedback);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/homework")
    public BaseResponse<List<HomeworkSubmissionVO>> getSubmissionsByHomework(@RequestParam Long homeworkId) {
        if (homeworkId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<HomeworkSubmissionVO> submissionVOs = homeworkSubmissionService.getSubmissionsByHomework(homeworkId);
        return ResultUtils.success(submissionVOs);
    }

    @GetMapping("/list/student")
    public BaseResponse<List<HomeworkSubmissionVO>> getSubmissionsByStudent(@RequestParam Long studentId) {
        if (studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<HomeworkSubmissionVO> submissionVOs = homeworkSubmissionService.getSubmissionsByStudent(studentId);
        return ResultUtils.success(submissionVOs);
    }
}
