package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ServiceRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务记录Mapper接口
 */
@Mapper
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

}