package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.dto.RepairEvaluationRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.RepairEvaluationVO;

public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    RepairEvaluationVO submitEvaluation(RepairEvaluationRequest request);

    RepairEvaluationVO getEvaluationByOrderId(Long orderId);

    Page<RepairEvaluationVO> getEvaluationsByStaffId(Long staffId, PageRequest pageRequest);
}
