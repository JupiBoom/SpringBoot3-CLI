package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.homework.HomeworkAddRequest;
import com.rosy.main.domain.dto.homework.HomeworkQueryRequest;
import com.rosy.main.domain.dto.homework.HomeworkUpdateRequest;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.service.IHomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homework")
@Tag(name = "HomeworkController", description = "作业管理")
public class HomeworkController {

    @Resource
    private IHomeworkService homeworkService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建作业")
    public ApiResponse addHomework(@RequestBody HomeworkAddRequest homeworkAddRequest) {
        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkAddRequest, homework);
        boolean result = homeworkService.save(homework);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(homework.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除作业")
    public ApiResponse deleteHomework(@RequestBody IdRequest idRequest) {
        boolean result = homeworkService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新作业")
    public ApiResponse updateHomework(@RequestBody HomeworkUpdateRequest homeworkUpdateRequest) {
        if (homeworkUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkUpdateRequest, homework);
        boolean result = homeworkService.updateById(homework);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "根据ID获取作业")
    public ApiResponse getHomeworkById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Homework homework = homeworkService.getById(id);
        ThrowUtils.throwIf(homework == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(homework);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取作业列表")
    public ApiResponse listHomeworkByPage(@RequestBody HomeworkQueryRequest homeworkQueryRequest) {
        long current = homeworkQueryRequest.getCurrent();
        long size = homeworkQueryRequest.getPageSize();
        Page<Homework> homeworkPage = homeworkService.page(new Page<>(current, size),
                homeworkService.getQueryWrapper(homeworkQueryRequest));
        return ApiResponse.success(homeworkPage);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "获取课程的作业列表")
    public ApiResponse getHomeworksByCourse(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(homeworkService.getHomeworksByCourse(courseId));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交作业")
    public ApiResponse submitHomework(@RequestParam Long homeworkId,
                                      @RequestParam Long studentId,
                                      @RequestParam String content) {
        if (homeworkId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        HomeworkSubmission submission = homeworkService.submitHomework(homeworkId, studentId, content);
        return ApiResponse.success(submission);
    }

    @PostMapping("/grade/{submissionId}")
    @Operation(summary = "批改作业")
    public ApiResponse gradeHomework(@PathVariable Long submissionId,
                                     @RequestParam Integer score,
                                     @RequestParam(required = false) String feedback) {
        if (submissionId == null || submissionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkService.gradeHomework(submissionId, score, feedback);
        return ApiResponse.success(result);
    }

    @GetMapping("/{homeworkId}/submissions")
    @Operation(summary = "获取作业的所有提交")
    public ApiResponse getSubmissions(@PathVariable Long homeworkId) {
        if (homeworkId == null || homeworkId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(homeworkService.getSubmissions(homeworkId));
    }
}
