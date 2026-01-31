package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairEvaluationRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    private final IRepairOrderService repairOrderService;
    private final IRepairStaffService repairStaffService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairEvaluationVO submitEvaluation(RepairEvaluationRequest request) {
        RepairOrder order = repairOrderService.getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!order.getStatus().equals(RepairOrderStatusEnum.COMPLETED.getCode().byteValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单未完成，无法评价");
        }
        
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getOrderId, request.getOrderId());
        RepairEvaluation existingEvaluation = getOne(wrapper);
        if (existingEvaluation != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该工单已评价");
        }
        
        RepairEvaluation evaluation = new RepairEvaluation();
        evaluation.setOrderId(request.getOrderId());
        evaluation.setUserId(order.getUserId());
        evaluation.setStaffId(order.getStaffId());
        evaluation.setRating(request.getRating());
        evaluation.setContent(request.getContent());
        save(evaluation);
        
        return convertToVO(evaluation);
    }

    @Override
    public RepairEvaluationVO getEvaluationByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getOrderId, orderId);
        RepairEvaluation evaluation = getOne(wrapper);
        return evaluation != null ? convertToVO(evaluation) : null;
    }

    @Override
    public Page<RepairEvaluationVO> getEvaluationsByStaffId(Long staffId, PageRequest pageRequest) {
        Page<RepairEvaluation> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getStaffId, staffId)
               .orderByDesc(RepairEvaluation::getCreateTime);
        Page<RepairEvaluation> evaluationPage = page(page, wrapper);
        return (Page<RepairEvaluationVO>) evaluationPage.convert(this::convertToVO);
    }

    private RepairEvaluationVO convertToVO(RepairEvaluation evaluation) {
        RepairEvaluationVO vo = BeanUtil.copyProperties(evaluation, RepairEvaluationVO.class);
        if (evaluation.getStaffId() != null) {
            RepairStaff staff = repairStaffService.getById(evaluation.getStaffId());
            if (staff != null) {
                vo.setStaffName(staff.getStaffName());
            }
        }
        return vo;
    }
}
