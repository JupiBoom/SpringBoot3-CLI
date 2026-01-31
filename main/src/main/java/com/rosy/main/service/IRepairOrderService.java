package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.dto.repair.RepairOrderUpdateRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

/**
 * <p>
 * 设备报修工单表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    /**
     * 创建报修工单
     * @param repairOrderAddRequest 报修工单添加请求
     * @param userId 用户ID
     * @return 工单ID
     */
    Long createRepairOrder(RepairOrderAddRequest repairOrderAddRequest, Long userId);

    /**
     * 更新报修工单
     * @param repairOrderUpdateRequest 报修工单更新请求
     * @return 是否成功
     */
    boolean updateRepairOrder(RepairOrderUpdateRequest repairOrderUpdateRequest);

    /**
     * 分页查询报修工单
     * @param repairOrderQueryRequest 查询请求
     * @return 分页结果
     */
    Page<RepairOrder> pageRepairOrder(RepairOrderQueryRequest repairOrderQueryRequest);

    /**
     * 分页查询报修工单VO
     * @param repairOrderQueryRequest 查询请求
     * @return 分页VO结果
     */
    Page<RepairOrderVO> pageRepairOrderVO(RepairOrderQueryRequest repairOrderQueryRequest);

    /**
     * 获取报修工单VO
     * @param repairOrder 报修工单
     * @return 报修工单VO
     */
    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    /**
     * 自动分配工单
     * @param orderId 工单ID
     * @return 是否成功
     */
    boolean autoAssignOrder(Long orderId);

    /**
     * 手动分配工单
     * @param orderId 工单ID
     * @param technicianId 维修人员ID
     * @return 是否成功
     */
    boolean manualAssignOrder(Long orderId, Long technicianId);

    /**
     * 维修人员接单
     * @param orderId 工单ID
     * @param technicianId 维修人员ID
     * @return 是否成功
     */
    boolean acceptOrder(Long orderId, Long technicianId);

    /**
     * 完成工单
     * @param orderId 工单ID
     * @return 是否成功
     */
    boolean completeOrder(Long orderId);

    /**
     * 获取查询条件
     * @param repairOrderQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest repairOrderQueryRequest);
}