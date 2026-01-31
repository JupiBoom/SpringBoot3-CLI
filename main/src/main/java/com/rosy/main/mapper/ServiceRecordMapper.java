package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

    ServiceRecordVO selectServiceRecordDetail(@Param("id") Long id);

    List<ServiceRecordVO> selectByUserId(@Param("userId") Long userId);

    ServiceRecord selectByRegistrationId(@Param("registrationId") Long registrationId);
}
