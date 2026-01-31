package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LeaveRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 请假表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {

}