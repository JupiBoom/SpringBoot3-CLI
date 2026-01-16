package com.rosy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.course.domain.entity.CourseMaterial;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程资料表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Mapper
public interface CourseMaterialMapper extends BaseMapper<CourseMaterial> {

}