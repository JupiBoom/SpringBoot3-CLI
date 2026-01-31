package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.CourseDiscussion;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程讨论表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface CourseDiscussionMapper extends BaseMapper<CourseDiscussion> {

}