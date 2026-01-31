package com.rosy.web.controller.repair;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.dto.repair.RepairOrderUpdateRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 设备报修工单表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/order")
@Tag(name = "报修工单管理", description = "报修工单相关接口")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    /**
     * 创建报修工单
     */
    @PostMapping("/add")
    @Operation(summary = "创建报修工单", description = "用户提交报修申请")
    @ValidateRequest
    public ApiResponse<Long> addRepairOrder(@RequestBody RepairOrderAddRequest repairOrderAddRequest,
                                           HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        Long orderId = repairOrderService.createRepairOrder(repairOrderAddRequest, userId);
        return ApiResponse.success(orderId);
    }

    /**
     * 删除报修工单
     */
    @PostMapping("/delete")
    @Operation(summary = "删除报修工单", description = "根据ID删除报修工单")
    @ValidateRequest
    public ApiResponse<Boolean> deleteRepairOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新报修工单
     */
    @PostMapping("/update")
    @Operation(summary = "更新报修工单", description = "更新报修工单信息")
    @ValidateRequest
    public ApiResponse<Boolean> updateRepairOrder(@RequestBody RepairOrderUpdateRequest repairOrderUpdateRequest,
                                                  HttpServletRequest request) {
        if (repairOrderUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        }
        
        boolean result = repairOrderService.updateRepairOrder(repairOrderUpdateRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取报修工单
     */
    @GetMapping("/get")
    @Operation(summary = "获取报修工单详情", description = "根据ID获取报修工单详细信息")
    public ApiResponse<RepairOrder> getRepairOrderById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairOrder);
    }

    /**
     * 根据ID获取报修工单VO
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取报修工单VO", description = "根据ID获取报修工单视图对象")
    public ApiResponse<RepairOrderVO> getRepairOrderVOById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        
        RepairOrderVO repairOrderVO = repairOrderService.getRepairOrderVO(repairOrder);
        return ApiResponse.success(repairOrderVO);
    }

    /**
     * 分页获取报修工单列表
     */
    @PostMapping("/list/page")
    @Operation(summary = "分页获取报修工单列表", description = "分页查询报修工单列表")
    @ValidateRequest
    public ApiResponse<Page<RepairOrder>> listRepairOrderByPage(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest) {
        long current = repairOrderQueryRequest.getCurrent();
        long size = repairOrderQueryRequest.getPageSize();
        Page<RepairOrder> repairOrderPage = repairOrderService.pageRepairOrder(repairOrderQueryRequest);
        return ApiResponse.success(repairOrderPage);
    }

    /**
     * 分页获取报修工单VO列表
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取报修工单VO列表", description = "分页查询报修工单视图对象列表")
    @ValidateRequest
    public ApiResponse<Page<RepairOrderVO>> listRepairOrderVOByPage(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest) {
        long current = repairOrderQueryRequest.getCurrent();
        long size = repairOrderQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<RepairOrderVO> repairOrderVOPage = repairOrderService.pageRepairOrderVO(repairOrderQueryRequest);
        return ApiResponse.success(repairOrderVOPage);
    }

    /**
     * 自动分配工单
     */
    @PostMapping("/auto-assign")
    @Operation(summary = "自动分配工单", description = "系统自动分配工单给维修人员")
    public ApiResponse<Boolean> autoAssignOrder(@RequestParam Long orderId) {
        boolean result = repairOrderService.autoAssignOrder(orderId);
        return ApiResponse.success(result);
    }

    /**
     * 手动分配工单
     */
    @PostMapping("/manual-assign")
    @Operation(summary = "手动分配工单", description = "管理员手动分配工单给指定维修人员")
    public ApiResponse<Boolean> manualAssignOrder(@RequestParam Long orderId, @RequestParam Long technicianId) {
        boolean result = repairOrderService.manualAssignOrder(orderId, technicianId);
        return ApiResponse.success(result);
    }

    /**
     * 维修人员接单
     */
    @PostMapping("/accept")
    @Operation(summary = "维修人员接单", description = "维修人员确认接收分配的工单")
    public ApiResponse<Boolean> acceptOrder(@RequestParam Long orderId, HttpServletRequest request) {
        // TODO: 从请求中获取当前登录维修人员ID
        Long technicianId = 2L; // 临时硬编码，实际应从登录信息中获取
        
        boolean result = repairOrderService.acceptOrder(orderId, technicianId);
        return ApiResponse.success(result);
    }

    /**
     * 完成工单
     */
    @PostMapping("/complete")
    @Operation(summary = "完成工单", description = "维修人员完成工单")
    public ApiResponse<Boolean> completeOrder(@RequestParam Long orderId) {
        boolean result = repairOrderService.completeOrder(orderId);
        return ApiResponse.success(result);
    }
}