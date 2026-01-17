package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairPersonnel;
import com.rosy.main.domain.vo.repair.RepairEvaluationVO;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairPersonnelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Resource
    private IRepairPersonnelService repairPersonnelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairEvaluationVO createEvaluation(RepairEvaluationAddRequest request, Long userId) {
        RepairOrder repairOrder = repairOrderService.getById(request.getOrderId());
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }
        if (!RepairOrderStatusEnum.COMPLETED.getCode().equals(repairOrder.getStatus().intValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能评价已完成的报修单");
        }
        if (!userId.equals(repairOrder.getReporterId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只能评价自己提交的报修单");
        }

        RepairEvaluation evaluation = BeanUtil.copyProperties(request, RepairEvaluation.class);
        evaluation.setEvaluatorId(userId);
        evaluation.setEvaluatorName(repairOrder.getReporterName());
        boolean saved = save(evaluation);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建评价失败");
        }

        if (repairOrder.getAssigneeId() != null) {
            updatePersonnelRating(repairOrder.getAssigneeId());
        }

        return getRepairEvaluationVO(evaluation);
    }

    @Override
    public RepairEvaluationVO getEvaluationByOrderId(Long orderId) {
        RepairEvaluation evaluation = lambdaQuery()
                .eq(RepairEvaluation::getOrderId, orderId)
                .one();
        return getRepairEvaluationVO(evaluation);
    }

    @Override
    public RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation evaluation) {
        if (evaluation == null) {
            return null;
        }
        return BeanUtil.copyProperties(evaluation, RepairEvaluationVO.class);
    }

    private void updatePersonnelRating(Long personnelId) {
        RepairPersonnel personnel = repairPersonnelService.getById(personnelId);
        if (personnel == null) {
            return;
        }

        List<RepairEvaluation> evaluations = lambdaQuery()
                .isNotNull(RepairEvaluation::getOrderId)
                .list();

        if (evaluations.isEmpty()) {
            return;
        }

        BigDecimal totalRating = BigDecimal.ZERO;
        int count = 0;

        for (RepairEvaluation eval : evaluations) {
            RepairOrder order = repairOrderService.getById(eval.getOrderId());
            if (order != null && personnelId.equals(order.getAssigneeId())) {
                totalRating = totalRating.add(BigDecimal.valueOf(eval.getRating()));
                count++;
            }
        }

        if (count > 0) {
            BigDecimal avgRating = totalRating.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
            personnel.setAvgRating(avgRating);
            repairPersonnelService.updateById(personnel);
        }
    }
}
