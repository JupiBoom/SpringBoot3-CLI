package com.rosy.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.common.BaseResponse;
import com.rosy.common.common.DeleteRequest;
import com.rosy.common.common.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.StudentVO;
import com.rosy.main.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @PostMapping("/add")
    public BaseResponse<Long> addStudent(@RequestBody Student student) {
        if (student == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = studentService.save(student);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(student.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateStudent(@RequestBody Student student) {
        if (student == null || student.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = studentService.updateById(student);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteStudent(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = studentService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<StudentVO> getStudentById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Student student = studentService.getById(id);
        StudentVO studentVO = studentService.getStudentVO(student);
        return ResultUtils.success(studentVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<StudentVO>> listStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNumber) {
        var queryWrapper = studentService.getQueryWrapper(name, studentNumber);
        List<Student> students = studentService.list(queryWrapper);
        List<StudentVO> studentVOs = students.stream()
                .map(studentService::getStudentVO)
                .collect(java.util.stream.Collectors.toList());
        return ResultUtils.success(studentVOs);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<StudentVO>> listStudentsByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNumber) {
        var queryWrapper = studentService.getQueryWrapper(name, studentNumber);
        Page<Student> page = studentService.page(new Page<>(current, pageSize), queryWrapper);
        Page<StudentVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<StudentVO> studentVOs = page.getRecords().stream()
                .map(studentService::getStudentVO)
                .collect(java.util.stream.Collectors.toList());
        voPage.setRecords(studentVOs);
        return ResultUtils.success(voPage);
    }
}
