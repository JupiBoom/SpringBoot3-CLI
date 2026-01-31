package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.EvaluationAddRequest;
import com.rosy.main.domain.entity.Evaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.EvaluationVO;
import com.rosy.main.enums.RepairStatusEnum;
import com.rosy.main.mapper.EvaluationMapper;
import com.rosy.main.service.IEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评价表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements IEvaluationService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public Long addEvaluation(EvaluationAddRequest evaluationAddRequest, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(evaluationAddRequest == null, new BusinessException("请求参数不能为空"));
        ThrowUtils.throwIf(evaluationAddRequest.getOrderId() == null, new BusinessException("工单ID不能为空"));
        ThrowUtils.throwIf(evaluationAddRequest.getRating() == null, new BusinessException("评分不能为空"));
        ThrowUtils.throwIf(evaluationAddRequest.getRating() < 1 || evaluationAddRequest.getRating() > 5, 
                         new BusinessException("评分必须在1-5之间"));

        // 查询工单
        RepairOrder repairOrder = repairOrderService.getById(evaluationAddRequest.getOrderId());
        ThrowUtils.throwIf(repairOrder == null, new BusinessException("工单不存在"));
        ThrowUtils.throwIf(!userId.equals(repairOrder.getUserId()), 
                         new BusinessException("只能评价自己提交的工单"));
        ThrowUtils.throwIf(repairOrder.getStatus() != RepairStatusEnum.COMPLETED.getCode(), 
                         new BusinessException("只能评价已完成的工单"));

        // 检查是否已经评价过
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", evaluationAddRequest.getOrderId());
        Evaluation existingEvaluation = this.getOne(queryWrapper);
        ThrowUtils.throwIf(existingEvaluation != null, new BusinessException("该工单已经评价过了"));

        // 创建评价
        Evaluation evaluation = new Evaluation();
        BeanUtil.copyProperties(evaluationAddRequest, evaluation);
        evaluation.setUserId(userId);

        // 保存评价
        boolean result = this.save(evaluation);
        ThrowUtils.throwIf(!result, new BusinessException("添加评价失败"));

        return evaluation.getId();
    }

    @Override
    public EvaluationVO getEvaluationByOrderId(Long orderId) {
        // 参数校验
        ThrowUtils.throwIf(orderId == null, new BusinessException("工单ID不能为空"));

        // 查询评价
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        Evaluation evaluation = this.getOne(queryWrapper);

        if (evaluation == null) {
            return null;
        }

        return getEvaluationVO(evaluation);
    }

    @Override
    public EvaluationVO getEvaluationVO(Evaluation evaluation) {
        EvaluationVO evaluationVO = new EvaluationVO();
        BeanUtil.copyProperties(evaluation, evaluationVO);

        // 获取工单号
        if (evaluation.getOrderId() != null) {
            RepairOrder repairOrder = repairOrderService.getById(evaluation.getOrderId());
            if (repairOrder != null) {
                evaluationVO.setOrderNo(repairOrder.getOrderNo());
            }
        }

        return evaluationVO;
    }
}