package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.HomeworkSubmitRequest;
import com.rosy.main.domain.dto.HomeworkGradeRequest;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmit;
import com.rosy.main.service.IHomeworkService;
import com.rosy.main.service.IHomeworkSubmitService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 作业管理前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/homework")
public class HomeworkController {
    @Resource
    IHomeworkService homeworkService;

    @Resource
    IHomeworkSubmitService homeworkSubmitService;

    /**
     * 发布作业
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addHomework(@RequestBody Homework homework) {
        ThrowUtils.throwIf(homework.getCourseId() == null || homework.getCourseId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(homework.getTitle() == null || homework.getTitle().trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        boolean result = homeworkService.save(homework);
        return ApiResponse.success(result);
    }

    /**
     * 删除作业
     */
    @PostMapping("/delete")
    public ApiResponse deleteHomework(@RequestBody IdRequest request) {
        ThrowUtils.throwIf(request.getId() == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean result = homeworkService.removeById(request.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新作业
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateHomework(@RequestBody Homework homework) {
        if (homework.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkService.updateById(homework);
        return ApiResponse.success(result);
    }

    /**
     * 获取作业详情
     */
    @GetMapping("/get/{id}")
    public ApiResponse getHomeworkById(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Homework homework = homeworkService.getById(id);
        ThrowUtils.throwIf(homework == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(homework);
    }

    /**
     * 获取课程作业列表
     */
    @GetMapping("/list/course/{courseId}")
    public ApiResponse getCourseHomeworks(@PathVariable Long courseId) {
        ThrowUtils.throwIf(courseId == null || courseId <= 0, ErrorCode.PARAMS_ERROR);
        List<Homework> homeworks = homeworkService.lambdaQuery()
                .eq(Homework::getCourseId, courseId)
                .orderByDesc(Homework::getCreateTime)
                .list();
        return ApiResponse.success(homeworks);
    }

    /**
     * 学生提交作业
     */
    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse submitHomework(@RequestBody HomeworkSubmitRequest request) {
        ThrowUtils.throwIf(request.getHomeworkId() == null || request.getHomeworkId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getStudentId() == null || request.getStudentId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf((request.getContent() == null || request.getContent().trim().isEmpty()) &&
                (request.getFileUrl() == null || request.getFileUrl().trim().isEmpty()), ErrorCode.PARAMS_ERROR);

        boolean result = homeworkSubmitService.submitHomework(
                request.getHomeworkId(),
                request.getStudentId(),
                request.getContent(),
                request.getFileUrl()
        );
        return ApiResponse.success(result);
    }

    /**
     * 教师批改作业
     */
    @PostMapping("/grade")
    @ValidateRequest
    public ApiResponse gradeHomework(@RequestBody HomeworkGradeRequest request) {
        ThrowUtils.throwIf(request.getSubmitId() == null || request.getSubmitId() <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getScore() == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getScore() < 0 || request.getScore() > 100, ErrorCode.PARAMS_ERROR);

        boolean result = homeworkSubmitService.gradeHomework(
                request.getSubmitId(),
                request.getScore(),
                request.getFeedback()
        );
        return ApiResponse.success(result);
    }

    /**
     * 获取作业提交列表
     */
    @GetMapping("/submits/{homeworkId}")
    public ApiResponse getHomeworkSubmits(@PathVariable Long homeworkId) {
        ThrowUtils.throwIf(homeworkId == null || homeworkId <= 0, ErrorCode.PARAMS_ERROR);
        List<HomeworkSubmit> submits = homeworkSubmitService.lambdaQuery()
                .eq(HomeworkSubmit::getHomeworkId, homeworkId)
                .orderByDesc(HomeworkSubmit::getSubmitTime)
                .list();
        return ApiResponse.success(submits);
    }

    /**
     * 获取学生某作业的提交记录
     */
    @GetMapping("/submit/student")
    public ApiResponse getStudentHomeworkSubmit(@RequestParam Long homeworkId, @RequestParam Long studentId) {
        ThrowUtils.throwIf(homeworkId == null || homeworkId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(studentId == null || studentId <= 0, ErrorCode.PARAMS_ERROR);
        HomeworkSubmit submit = homeworkSubmitService.lambdaQuery()
                .eq(HomeworkSubmit::getHomeworkId, homeworkId)
                .eq(HomeworkSubmit::getStudentId, studentId)
                .one();
        return ApiResponse.success(submit);
    }

    /**
     * 获取学生所有作业提交记录
     */
    @GetMapping("/submits/student/{studentId}")
    public ApiResponse getStudentAllSubmits(@PathVariable Long studentId) {
        ThrowUtils.throwIf(studentId == null || studentId <= 0, ErrorCode.PARAMS_ERROR);
        List<HomeworkSubmit> submits = homeworkSubmitService.lambdaQuery()
                .eq(HomeworkSubmit::getStudentId, studentId)
                .orderByDesc(HomeworkSubmit::getSubmitTime)
                .list();
        return ApiResponse.success(submits);
    }
}
