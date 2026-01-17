package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.RepairEvaluationCreateDTO;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.RepairEvaluationVO;

/**
 * 维修评价Service接口
 */
public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    RepairEvaluationVO createEvaluation(RepairEvaluationCreateDTO dto, Long userId, String userName);

    RepairEvaluationVO getByOrderId(Long orderId);

    boolean hasEvaluation(Long orderId);

    Double getRepairmanAvgRating(Long repairmanId);
}
