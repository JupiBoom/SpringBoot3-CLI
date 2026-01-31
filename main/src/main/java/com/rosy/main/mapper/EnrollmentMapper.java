package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Enrollment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报名Mapper接口
 */
@Mapper
public interface EnrollmentMapper extends BaseMapper<Enrollment> {

}