package com.rosy.main.controller;

import com.rosy.common.common.BaseResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.HomeworkVO;
import com.rosy.main.service.IHomeworkService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Resource
    private IHomeworkService homeworkService;

    @PostMapping("/create")
    public BaseResponse<Long> createHomework(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String deadline) {
        if (courseId == null || title == null || deadline == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkService.createHomework(courseId, title, description, deadline);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(1L);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateHomework(
            @RequestParam Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkService.updateHomework(id, title, description, deadline);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteHomework(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = homeworkService.deleteHomework(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/course")
    public BaseResponse<List<HomeworkVO>> getHomeworksByCourse(@RequestParam Long courseId) {
        if (courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<HomeworkVO> homeworkVOs = homeworkService.getHomeworksByCourse(courseId);
        return ResultUtils.success(homeworkVOs);
    }
}
