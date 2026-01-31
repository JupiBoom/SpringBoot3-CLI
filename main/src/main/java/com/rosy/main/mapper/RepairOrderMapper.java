package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 设备报修工单表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

}