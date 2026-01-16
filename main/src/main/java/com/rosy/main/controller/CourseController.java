package com.rosy.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.AuthCheck;
import com.rosy.common.common.BaseResponse;
import com.rosy.common.common.DeleteRequest;
import com.rosy.common.common.ErrorCode;
import com.rosy.common.common.ResultUtils;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.item.ItemAddRequest;
import com.rosy.main.domain.dto.item.ItemQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.service.ICourseService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @PostMapping("/add")
    public BaseResponse<Long> addCourse(@RequestBody Course course) {
        if (course == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.save(course);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(course.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateCourse(@RequestBody Course course) {
        if (course == null || course.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.updateById(course);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCourse(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<CourseVO> getCourseById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseVO courseVO = courseService.getCourseDetail(id);
        return ResultUtils.success(courseVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<CourseVO>> listCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Byte status) {
        var queryWrapper = courseService.getQueryWrapper(name, teacherId, status);
        List<Course> courses = courseService.list(queryWrapper);
        List<CourseVO> courseVOs = courses.stream()
                .map(courseService::getCourseVO)
                .collect(java.util.stream.Collectors.toList());
        return ResultUtils.success(courseVOs);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<CourseVO>> listCoursesByPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Byte status) {
        var queryWrapper = courseService.getQueryWrapper(name, teacherId, status);
        Page<Course> page = courseService.page(new Page<>(current, pageSize), queryWrapper);
        Page<CourseVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<CourseVO> courseVOs = page.getRecords().stream()
                .map(courseService::getCourseVO)
                .collect(java.util.stream.Collectors.toList());
        voPage.setRecords(courseVOs);
        return ResultUtils.success(voPage);
    }

    @PostMapping("/addStudent")
    public BaseResponse<Boolean> addStudentToCourse(@RequestParam Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.addStudentToCourse(courseId, studentId);
        return ResultUtils.success(result);
    }

    @PostMapping("/removeStudent")
    public BaseResponse<Boolean> removeStudentFromCourse(@RequestParam Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.removeStudentFromCourse(courseId, studentId);
        return ResultUtils.success(result);
    }
}
