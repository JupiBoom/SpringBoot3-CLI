package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.dto.CourseQueryRequest;
import com.rosy.main.service.ICourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程管理前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    ICourseService courseService;

    /**
     * 创建课程
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addCourse(@RequestBody Course course) {
        boolean result = courseService.save(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(course.getId());
    }

    /**
     * 删除课程
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteCourse(@RequestBody IdRequest idRequest) {
        boolean result = courseService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新课程
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateCourse(@RequestBody Course course) {
        if (course.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.updateById(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据id获取课程
     */
    @GetMapping("/get")
    public ApiResponse getCourseById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(course);
    }

    /**
     * 获取所有课程列表
     */
    @GetMapping("/list")
    public ApiResponse listAllCourses() {
        List<Course> courses = courseService.list();
        return ApiResponse.success(courses);
    }

    /**
     * 分页获取课程列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listCourseByPage(@RequestBody CourseQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);

        Page<Course> page = new Page<>(current, size);
        Page<Course> result = courseService.page(page);
        return ApiResponse.success(result);
    }

    /**
     * 根据教师ID获取课程列表
     */
    @GetMapping("/list/teacher/{teacherId}")
    public ApiResponse listCoursesByTeacherId(@PathVariable Long teacherId) {
        ThrowUtils.throwIf(teacherId == null || teacherId <= 0, ErrorCode.PARAMS_ERROR);
        List<Course> courses = courseService.lambdaQuery()
                .eq(Course::getTeacherId, teacherId)
                .list();
        return ApiResponse.success(courses);
    }

    /**
     * 根据关键字搜索课程
     */
    @GetMapping("/search")
    public ApiResponse searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ApiResponse.success(courseService.list());
        }
        List<Course> courses = courseService.lambdaQuery()
                .like(Course::getCourseName, keyword)
                .or()
                .like(Course::getDescription, keyword)
                .list();
        return ApiResponse.success(courses);
    }
}
