package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.dto.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.RepairOrderSubmitRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

public interface IRepairOrderService extends IService<RepairOrder> {

    RepairOrderVO submitOrder(RepairOrderSubmitRequest request);

    RepairOrderVO assignOrder(RepairOrderAssignRequest request);

    RepairOrderVO autoAssignOrder(Long orderId);

    RepairOrderVO acceptOrder(Long orderId);

    RepairOrderVO completeOrder(Long orderId);

    RepairOrderVO cancelOrder(Long orderId);

    RepairOrderVO confirmCompletion(Long orderId);

    RepairOrderVO getOrderDetail(Long orderId);

    Page<RepairOrderVO> getMyOrders(PageRequest pageRequest);

    Page<RepairOrderVO> getStaffOrders(PageRequest pageRequest);

    Page<RepairOrderVO> getAllOrders(PageRequest pageRequest);
}
