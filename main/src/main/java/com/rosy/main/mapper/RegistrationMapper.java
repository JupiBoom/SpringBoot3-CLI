package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Registration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {
}
