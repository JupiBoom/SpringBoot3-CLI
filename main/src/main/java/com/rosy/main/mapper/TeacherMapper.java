package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 教师表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

}