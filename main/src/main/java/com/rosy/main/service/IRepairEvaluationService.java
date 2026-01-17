package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.repair.RepairEvaluationVO;

public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    RepairEvaluationVO createEvaluation(RepairEvaluationAddRequest request, Long userId);

    RepairEvaluationVO getEvaluationByOrderId(Long orderId);

    RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation evaluation);
}
