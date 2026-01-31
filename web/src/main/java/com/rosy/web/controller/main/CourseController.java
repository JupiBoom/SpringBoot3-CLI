package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/course")
@Tag(name = "课程管理", description = "课程相关接口")
public class CourseController {
    
    @Resource
    private ICourseService courseService;

    // region 增删改查

    /**
     * 创建课程
     */
    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建课程", description = "创建新的课程")
    public ApiResponse<Long> addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        if (courseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Course course = new Course();
        BeanUtils.copyProperties(courseAddRequest, course);
        
        // 默认状态为启用
        course.setStatus((byte) 1);
        
        boolean result = courseService.save(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(course.getId());
    }

    /**
     * 删除课程
     */
    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除课程", description = "根据ID删除课程")
    public ApiResponse<Boolean> deleteCourse(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = courseService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新课程
     */
    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新课程", description = "更新课程信息")
    public ApiResponse<Boolean> updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest,
                                              HttpServletRequest request) {
        if (courseUpdateRequest == null || courseUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Course course = BeanUtil.copyProperties(courseUpdateRequest, Course.class);
        boolean result = courseService.updateById(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取课程
     */
    @GetMapping("/get")
    @Operation(summary = "获取课程", description = "根据ID获取课程详细信息")
    public ApiResponse<Course> getCourseById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(course);
    }

    /**
     * 根据ID获取课程视图对象
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取课程视图", description = "根据ID获取课程详细信息（包含教师姓名）")
    public ApiResponse<CourseVO> getCourseVOById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(courseService.getCourseVO(course));
    }

    /**
     * 分页获取课程列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取课程列表", description = "分页获取课程列表（不含教师姓名）")
    public ApiResponse<Page<Course>> listCourseByPage(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = courseService.page(new Page<>(current, size), 
                courseService.getQueryWrapper(courseQueryRequest));
        return ApiResponse.success(coursePage);
    }

    /**
     * 分页获取课程视图列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    @Operation(summary = "分页获取课程视图列表", description = "分页获取课程列表（包含教师姓名）")
    public ApiResponse<Page<CourseVO>> listCourseVOByPage(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Course> coursePage = courseService.page(new Page<>(current, size), 
                courseService.getQueryWrapper(courseQueryRequest));
        Page<CourseVO> courseVOPage = courseService.getCourseVOPage(coursePage);
        return ApiResponse.success(courseVOPage);
    }

    // endregion
}