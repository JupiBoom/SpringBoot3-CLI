package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.FaultType;
import com.rosy.main.mapper.FaultTypeMapper;
import com.rosy.main.service.IFaultTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 故障类型表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class FaultTypeServiceImpl extends ServiceImpl<FaultTypeMapper, FaultType> implements IFaultTypeService {

}