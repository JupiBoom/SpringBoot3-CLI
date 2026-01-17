package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备Mapper接口
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
}
