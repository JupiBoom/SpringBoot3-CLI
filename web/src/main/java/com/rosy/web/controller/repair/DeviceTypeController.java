package com.rosy.web.controller.repair;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.DeviceType;
import com.rosy.main.service.IDeviceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备类型表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/device-type")
@Tag(name = "设备类型管理", description = "设备类型相关接口")
public class DeviceTypeController {

    @Resource
    private IDeviceTypeService deviceTypeService;

    /**
     * 创建设备类型
     */
    @PostMapping("/add")
    @Operation(summary = "创建设备类型", description = "添加新的设备类型")
    @ValidateRequest
    public ApiResponse<Long> addDeviceType(@RequestBody DeviceType deviceType) {
        boolean result = deviceTypeService.save(deviceType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(deviceType.getId());
    }

    /**
     * 删除设备类型
     */
    @PostMapping("/delete")
    @Operation(summary = "删除设备类型", description = "根据ID删除设备类型")
    @ValidateRequest
    public ApiResponse<Boolean> deleteDeviceType(@RequestBody IdRequest idRequest) {
        boolean result = deviceTypeService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新设备类型
     */
    @PostMapping("/update")
    @Operation(summary = "更新设备类型", description = "更新设备类型信息")
    @ValidateRequest
    public ApiResponse<Boolean> updateDeviceType(@RequestBody DeviceType deviceType) {
        if (deviceType.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = deviceTypeService.updateById(deviceType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取设备类型
     */
    @GetMapping("/get")
    @Operation(summary = "获取设备类型详情", description = "根据ID获取设备类型详细信息")
    public ApiResponse<DeviceType> getDeviceTypeById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DeviceType deviceType = deviceTypeService.getById(id);
        ThrowUtils.throwIf(deviceType == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(deviceType);
    }

    /**
     * 获取所有设备类型
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有设备类型", description = "获取所有设备类型列表")
    public ApiResponse<List<DeviceType>> listAllDeviceTypes() {
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<DeviceType> deviceTypes = deviceTypeService.list(queryWrapper);
        return ApiResponse.success(deviceTypes);
    }
}