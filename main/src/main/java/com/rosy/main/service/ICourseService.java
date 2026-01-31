package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ICourseService extends IService<Course> {

    /**
     * 获取查询条件
     *
     * @param courseQueryRequest 课程查询请求
     * @return 查询条件
     */
    QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest);

    /**
     * 获取课程视图对象
     *
     * @param course 课程实体
     * @return 课程视图对象
     */
    CourseVO getCourseVO(Course course);

    /**
     * 分页获取课程视图对象
     *
     * @param coursePage 课程分页对象
     * @return 课程视图对象分页
     */
    Page<CourseVO> getCourseVOPage(Page<Course> coursePage);
}