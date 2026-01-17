package com.rosy.repair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.repair.domain.entity.RepairOrder;
import com.rosy.repair.domain.request.*;
import com.rosy.repair.domain.vo.RepairOrderVO;
import com.rosy.repair.domain.vo.StatisticsVO;
import java.time.LocalDateTime;
import java.util.List;

public interface IRepairOrderService extends IService<RepairOrder> {

    RepairOrderVO createOrder(RepairOrderCreateRequest request, Long userId);

    void assignOrder(RepairOrderAssignRequest request);

    void autoAssignOrder(Long orderId);

    void acceptOrder(Long orderId, Long repairerId);

    void completeOrder(RepairRecordRequest request, Long repairerId);

    void evaluateOrder(EvaluationRequest request, Long userId);

    RepairOrderVO getOrderDetail(Long orderId);

    List<RepairOrderVO> getOrderList(Integer status, Long userId, Long repairerId);

    StatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime);
}