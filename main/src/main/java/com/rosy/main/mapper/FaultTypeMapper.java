package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.FaultType;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 故障类型表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Mapper
public interface FaultTypeMapper extends BaseMapper<FaultType> {

}