package com.rosy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.course.domain.entity.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}