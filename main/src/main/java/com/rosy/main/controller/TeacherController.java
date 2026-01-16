package com.rosy.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.common.BaseResponse;
import com.rosy.common.common.DeleteRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.TeacherVO;
import com.rosy.main.service.ITeacherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeacher(@RequestBody Teacher teacher) {
        if (teacher == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teacherService.save(teacher);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(teacher.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeacher(@RequestBody Teacher teacher) {
        if (teacher == null || teacher.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teacherService.updateById(teacher);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeacher(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teacherService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<TeacherVO> getTeacherById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Teacher teacher = teacherService.getById(id);
        TeacherVO teacherVO = teacherService.getTeacherVO(teacher);
        return ResultUtils.success(teacherVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeacherVO>> listTeachers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        var queryWrapper = teacherService.getQueryWrapper(name, department);
        List<Teacher> teachers = teacherService.list(queryWrapper);
        List<TeacherVO> teacherVOs = teachers.stream()
                .map(teacherService::getTeacherVO)
                .collect(java.util.stream.Collectors.toList());
        return ResultUtils.success(teacherVOs);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<TeacherVO>> listTeachersByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        var queryWrapper = teacherService.getQueryWrapper(name, department);
        Page<Teacher> page = teacherService.page(new Page<>(current, pageSize), queryWrapper);
        Page<TeacherVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<TeacherVO> teacherVOs = page.getRecords().stream()
                .map(teacherService::getTeacherVO)
                .collect(java.util.stream.Collectors.toList());
        voPage.setRecords(teacherVOs);
        return ResultUtils.success(voPage);
    }
}
