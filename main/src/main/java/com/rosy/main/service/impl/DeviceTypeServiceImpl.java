package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.DeviceType;
import com.rosy.main.mapper.DeviceTypeMapper;
import com.rosy.main.service.IDeviceTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备类型表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

}