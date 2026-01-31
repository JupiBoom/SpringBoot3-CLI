package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.EvaluationAddRequest;
import com.rosy.main.domain.entity.Evaluation;
import com.rosy.main.domain.vo.EvaluationVO;

/**
 * <p>
 * 评价表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
public interface IEvaluationService extends IService<Evaluation> {

    /**
     * 添加评价
     * @param evaluationAddRequest 评价添加请求
     * @param userId 用户ID
     * @return 评价ID
     */
    Long addEvaluation(EvaluationAddRequest evaluationAddRequest, Long userId);

    /**
     * 根据工单ID获取评价
     * @param orderId 工单ID
     * @return 评价VO
     */
    EvaluationVO getEvaluationByOrderId(Long orderId);

    /**
     * 获取评价VO
     * @param evaluation 评价
     * @return 评价VO
     */
    EvaluationVO getEvaluationVO(Evaluation evaluation);
}