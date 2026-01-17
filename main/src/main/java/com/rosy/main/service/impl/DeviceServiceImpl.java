package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.DeviceAddRequest;
import com.rosy.main.domain.dto.repair.DeviceQueryRequest;
import com.rosy.main.domain.entity.Device;
import com.rosy.main.domain.vo.repair.DeviceVO;
import com.rosy.main.mapper.DeviceMapper;
import com.rosy.main.service.IDeviceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Override
    public DeviceVO createDevice(DeviceAddRequest request) {
        Device device = BeanUtil.copyProperties(request, Device.class);
        device.setStatus((byte) 1);
        boolean saved = save(device);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建设备失败");
        }
        return getDeviceVO(device.getId());
    }

    @Override
    public DeviceVO getDeviceVO(Long deviceId) {
        Device device = getById(deviceId);
        if (device == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "设备不存在");
        }
        DeviceVO vo = BeanUtil.copyProperties(device, DeviceVO.class);
        if (device.getStatus() != null) {
            vo.setStatusDesc(device.getStatus() == 0 ? "停用" : device.getStatus() == 1 ? "正常" : "维修中");
        }
        return vo;
    }

    @Override
    public Page<DeviceVO> pageDevices(DeviceQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<Device> page = page(new Page<>(current, size), getQueryWrapper(request));
        Page<DeviceVO> voPage = new Page<>(current, size, page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(device -> {
                    DeviceVO vo = BeanUtil.copyProperties(device, DeviceVO.class);
                    if (device.getStatus() != null) {
                        vo.setStatusDesc(device.getStatus() == 0 ? "停用" : device.getStatus() == 1 ? "正常" : "维修中");
                    }
                    return vo;
                })
                .toList());
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<Device> getQueryWrapper(DeviceQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceNo(), Device::getDeviceNo);
        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceName(), Device::getDeviceName);
        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceType(), Device::getDeviceType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getLocation(), Device::getLocation);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), Device::getStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper, request.getSortField(), request.getSortOrder(), Device::getId);

        return queryWrapper;
    }
}
