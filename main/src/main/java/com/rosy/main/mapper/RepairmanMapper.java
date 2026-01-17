package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Repairman;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修人员Mapper接口
 */
@Mapper
public interface RepairmanMapper extends BaseMapper<Repairman> {
}
