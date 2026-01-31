package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 维修记录表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Mapper
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {

}