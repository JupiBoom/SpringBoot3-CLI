package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.DeviceAddRequest;
import com.rosy.main.domain.dto.repair.DeviceQueryRequest;
import com.rosy.main.domain.entity.Device;
import com.rosy.main.domain.vo.repair.DeviceVO;

public interface IDeviceService extends IService<Device> {

    DeviceVO createDevice(DeviceAddRequest request);

    DeviceVO getDeviceVO(Long deviceId);

    Page<DeviceVO> pageDevices(DeviceQueryRequest request);

    LambdaQueryWrapper<Device> getQueryWrapper(DeviceQueryRequest request);
}
