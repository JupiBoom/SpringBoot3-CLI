package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.utils.StringUtils;
import com.rosy.main.domain.dto.RepairOrderCreateDTO;
import com.rosy.main.domain.dto.RepairOrderAssignDTO;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.domain.vo.RepairOrderPageVO;
import com.rosy.main.service.IRepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报修工单控制器
 */
@RestController
@RequestMapping("/api/repair/order")
@Tag(name = "报修工单管理")
public class RepairOrderController {

    @Autowired
    private IRepairOrderService repairOrderService;

    /**
     * 创建报修工单
     */
    @PostMapping("/create")
    @Operation(summary = "创建报修工单")
    public ApiResponse createOrder(@RequestBody RepairOrderCreateDTO dto) {
        // 模拟登录用户信息，实际项目中应从SecurityContext获取
        Long userId = 1L;
        String userName = "测试用户";
        String userPhone = "13800138000";
        
        RepairOrderVO order = repairOrderService.createOrder(dto, userId, userName, userPhone);
        return ApiResponse.success(order);
    }

    /**
     * 根据ID查询工单详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "查询工单详情")
    public ApiResponse getOrderDetail(@PathVariable Long id) {
        RepairOrderVO order = repairOrderService.getOrderById(id);
        if (order == null) {
            return ApiResponse.error("工单不存在");
        }
        return ApiResponse.success(order);
    }

    /**
     * 分页查询工单列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询工单列表")
    public ApiResponse getOrderPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String equipmentName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long repairmanId) {
        
        RepairOrderPageVO page = repairOrderService.getOrderPage(pageNum, pageSize, orderNo, 
            equipmentName, status, priority, userId, repairmanId);
        return ApiResponse.success(page);
    }

    /**
     * 手动分配工单
     */
    @PostMapping("/assign")
    @Operation(summary = "手动分配工单")
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignDTO dto) {
        // 模拟操作员信息
        Long operatorId = 2L;
        String operatorName = "管理员";
        
        boolean success = repairOrderService.assignOrder(dto, operatorId, operatorName);
        if (success) {
            return ApiResponse.success("分配成功", true);
        }
        return ApiResponse.error("分配失败");
    }

    /**
     * 自动分配工单
     */
    @PostMapping("/auto-assign/{id}")
    @Operation(summary = "自动分配工单")
    public ApiResponse autoAssignOrder(@PathVariable Long id) {
        // 模拟操作员信息
        Long operatorId = 2L;
        String operatorName = "系统自动分配";
        
        boolean success = repairOrderService.autoAssignOrder(id, operatorId, operatorName);
        if (success) {
            return ApiResponse.success("自动分配成功", true);
        }
        return ApiResponse.error("自动分配失败，暂无可用维修人员");
    }

    /**
     * 维修人员接单
     */
    @PostMapping("/accept/{id}")
    @Operation(summary = "维修人员接单")
    public ApiResponse acceptOrder(@PathVariable Long id) {
        // 模拟维修人员信息
        Long repairmanId = 3L;
        String repairmanName = "张师傅";
        
        boolean success = repairOrderService.acceptOrder(id, repairmanId, repairmanName);
        if (success) {
            return ApiResponse.success("接单成功", true);
        }
        return ApiResponse.error("接单失败，工单状态不允许");
    }

    /**
     * 开始维修
     */
    @PostMapping("/start-repair/{id}")
    @Operation(summary = "开始维修")
    public ApiResponse startRepair(@PathVariable Long id, @RequestParam Long repairmanId) {
        boolean success = repairOrderService.startRepair(id, repairmanId);
        if (success) {
            return ApiResponse.success("开始维修", true);
        }
        return ApiResponse.error("操作失败");
    }

    /**
     * 完成维修
     */
    @PostMapping("/complete/{id}")
    @Operation(summary = "完成维修")
    public ApiResponse completeOrder(
            @PathVariable Long id,
            @RequestParam Long repairmanId,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Double cost) {
        boolean success = repairOrderService.completeOrder(id, repairmanId, remark, cost);
        if (success) {
            return ApiResponse.success("维修完成", true);
        }
        return ApiResponse.error("操作失败");
    }

    /**
     * 取消工单
     */
    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消工单")
    public ApiResponse cancelOrder(@PathVariable Long id, @RequestParam String reason) {
        Long userId = 1L;
        String userName = "测试用户";
        
        boolean success = repairOrderService.cancelOrder(id, userId, userName, reason);
        if (success) {
            return ApiResponse.success("工单已取消", true);
        }
        return ApiResponse.error("取消失败，工单状态不允许");
    }

    /**
     * 更新工单优先级
     */
    @PutMapping("/priority/{id}")
    @Operation(summary = "更新工单优先级")
    public ApiResponse updatePriority(@PathVariable Long id, @RequestParam String priority) {
        boolean success = repairOrderService.updatePriority(id, priority);
        if (success) {
            return ApiResponse.success("优先级已更新", true);
        }
        return ApiResponse.error("更新失败");
    }

    /**
     * 根据用户ID查询工单列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户工单列表")
    public ApiResponse getUserOrders(@PathVariable Long userId) {
        List<RepairOrderVO> orders = repairOrderService.getOrdersByUserId(userId);
        return ApiResponse.success(orders);
    }

    /**
     * 根据维修人员ID查询工单列表
     */
    @GetMapping("/repairman/{repairmanId}")
    @Operation(summary = "查询维修人员工单列表")
    public ApiResponse getRepairmanOrders(@PathVariable Long repairmanId) {
        List<RepairOrderVO> orders = repairOrderService.getOrdersByRepairmanId(repairmanId);
        return ApiResponse.success(orders);
    }
}