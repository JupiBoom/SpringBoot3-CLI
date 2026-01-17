package com.rosy.repair.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.repair.domain.entity.Repairer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepairerMapper extends BaseMapper<Repairer> {
}