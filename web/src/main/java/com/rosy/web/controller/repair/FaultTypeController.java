package com.rosy.web.controller.repair;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.FaultType;
import com.rosy.main.service.IFaultTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 故障类型表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/fault-type")
@Tag(name = "故障类型管理", description = "故障类型相关接口")
public class FaultTypeController {

    @Resource
    private IFaultTypeService faultTypeService;

    /**
     * 创建故障类型
     */
    @PostMapping("/add")
    @Operation(summary = "创建故障类型", description = "添加新的故障类型")
    @ValidateRequest
    public ApiResponse<Long> addFaultType(@RequestBody FaultType faultType) {
        boolean result = faultTypeService.save(faultType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(faultType.getId());
    }

    /**
     * 删除故障类型
     */
    @PostMapping("/delete")
    @Operation(summary = "删除故障类型", description = "根据ID删除故障类型")
    @ValidateRequest
    public ApiResponse<Boolean> deleteFaultType(@RequestBody IdRequest idRequest) {
        boolean result = faultTypeService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新故障类型
     */
    @PostMapping("/update")
    @Operation(summary = "更新故障类型", description = "更新故障类型信息")
    @ValidateRequest
    public ApiResponse<Boolean> updateFaultType(@RequestBody FaultType faultType) {
        if (faultType.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = faultTypeService.updateById(faultType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取故障类型
     */
    @GetMapping("/get")
    @Operation(summary = "获取故障类型详情", description = "根据ID获取故障类型详细信息")
    public ApiResponse<FaultType> getFaultTypeById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FaultType faultType = faultTypeService.getById(id);
        ThrowUtils.throwIf(faultType == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(faultType);
    }

    /**
     * 获取所有故障类型
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有故障类型", description = "获取所有故障类型列表")
    public ApiResponse<List<FaultType>> listAllFaultTypes() {
        QueryWrapper<FaultType> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<FaultType> faultTypes = faultTypeService.list(queryWrapper);
        return ApiResponse.success(faultTypes);
    }
}