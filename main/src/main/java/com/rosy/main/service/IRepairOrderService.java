package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderCompleteRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.repair.RepairOrderVO;

public interface IRepairOrderService extends IService<RepairOrder> {

    RepairOrderVO createRepairOrder(RepairOrderAddRequest request, Long userId);

    RepairOrderVO getRepairOrderVO(Long orderId);

    Page<RepairOrderVO> pageRepairOrders(RepairOrderQueryRequest request);

    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request);

    void assignOrder(RepairOrderAssignRequest request);

    void acceptOrder(Long orderId, Long userId);

    void completeOrder(RepairOrderCompleteRequest request);

    void cancelOrder(Long orderId, Long userId);

    String generateOrderNo();
}
