package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.DeviceAddRequest;
import com.rosy.main.domain.dto.repair.DeviceQueryRequest;
import com.rosy.main.domain.dto.repair.DeviceUpdateRequest;
import com.rosy.main.domain.entity.Device;
import com.rosy.main.domain.vo.repair.DeviceVO;
import com.rosy.main.service.IDeviceService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/device")
public class DeviceController {

    @Resource
    private IDeviceService deviceService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse createDevice(@RequestBody DeviceAddRequest request) {
        DeviceVO vo = deviceService.createDevice(request);
        return ApiResponse.success(vo);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteDevice(@RequestBody IdRequest request) {
        boolean result = deviceService.removeById(request.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateDevice(@RequestBody DeviceUpdateRequest request) {
        Device device = BeanUtil.copyProperties(request, Device.class);
        boolean result = deviceService.updateById(device);
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    public ApiResponse getDeviceById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceVO vo = deviceService.getDeviceVO(id);
        return ApiResponse.success(vo);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listDevicesByPage(@RequestBody DeviceQueryRequest request) {
        Page<DeviceVO> page = deviceService.pageDevices(request);
        return ApiResponse.success(page);
    }
}
