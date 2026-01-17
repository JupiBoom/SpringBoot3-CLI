package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.RepairOrderAssignDTO;
import com.rosy.main.domain.dto.RepairOrderCreateDTO;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

import java.util.List;
import java.util.Map;

/**
 * 报修工单Service接口
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    RepairOrderVO createOrder(RepairOrderCreateDTO dto, Long userId, String userName, String userPhone);

    RepairOrderVO getOrderById(Long id);

    Page<RepairOrderVO> pageOrder(Map<String, Object> params, long current, long size);

    boolean assignOrder(RepairOrderAssignDTO dto, Long operatorId, String operatorName);

    boolean acceptOrder(Long orderId, Long repairmanId, String repairmanName);

    boolean startRepair(Long orderId, Long repairmanId);

    boolean completeOrder(Long orderId, Long repairmanId, String remark, Double cost);

    boolean cancelOrder(Long orderId, Long userId, String reason);

    boolean updatePriority(Long orderId, String priority, Long operatorId);

    List<RepairOrderVO> getOrdersByUserId(Long userId, String status);

    List<RepairOrderVO> getOrdersByRepairmanId(Long repairmanId, String status);

    RepairOrderVO autoAssign(Long orderId, Long operatorId, String operatorName);
}
