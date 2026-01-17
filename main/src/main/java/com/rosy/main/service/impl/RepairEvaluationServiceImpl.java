package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosy.common.enums.RepairStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairEvaluationCreateDTO;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairmanService;
import com.rosy.main.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 维修评价Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    private final RepairEvaluationMapper evaluationMapper;
    private final RepairOrderMapper orderMapper;
    private final IRepairmanService repairmanService;
    private final INotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairEvaluationVO createEvaluation(RepairEvaluationCreateDTO dto, Long userId, String userName) {
        RepairOrder order = orderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!userId.equals(order.getUserId())) {
            throw new BusinessException("只有报修人可以评价");
        }

        if (!RepairStatusEnum.COMPLETED.getCode().equals(order.getStatus())) {
            throw new BusinessException("只有已完成的工单才能评价");
        }

        if (hasEvaluation(dto.getOrderId())) {
            throw new BusinessException("该工单已评价过");
        }

        RepairEvaluation evaluation = new RepairEvaluation();
        evaluation.setOrderId(dto.getOrderId());
        evaluation.setUserId(userId);
        evaluation.setUserName(userName);
        evaluation.setRepairmanId(order.getRepairmanId());
        evaluation.setRepairmanName(order.getRepairmanName());
        evaluation.setRating(dto.getRating());
        evaluation.setContent(dto.getContent());
        
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                evaluation.setImages(objectMapper.writeValueAsString(dto.getImages()));
            } catch (Exception e) {
                log.error("图片序列化失败", e);
            }
        }
        
        evaluation.setResponseRating(dto.getResponseRating() != null ? dto.getResponseRating() : dto.getRating());
        evaluation.setServiceRating(dto.getServiceRating() != null ? dto.getServiceRating() : dto.getRating());
        evaluation.setQualityRating(dto.getQualityRating() != null ? dto.getQualityRating() : dto.getRating());
        evaluation.setCreator(userName);

        evaluationMapper.insert(evaluation);

        updateRepairmanRating(order.getRepairmanId());

        notificationService.createNotification(
                order.getRepairmanId(), order.getRepairmanName(),
                "评价通知",
                "您完成的工单已收到评价：" + dto.getRating() + "星",
                "order_evaluated", order.getId(), "repair_order"
        );

        return convertToVO(evaluation);
    }

    @Override
    public RepairEvaluationVO getByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getOrderId, orderId);
        RepairEvaluation evaluation = evaluationMapper.selectOne(wrapper);
        return evaluation != null ? convertToVO(evaluation) : null;
    }

    @Override
    public boolean hasEvaluation(Long orderId) {
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getOrderId, orderId);
        return evaluationMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Double getRepairmanAvgRating(Long repairmanId) {
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getRepairmanId, repairmanId);
        List<RepairEvaluation> evaluations = evaluationMapper.selectList(wrapper);
        
        if (evaluations.isEmpty()) {
            return 5.0;
        }
        
        double avg = evaluations.stream()
                .mapToInt(RepairEvaluation::getRating)
                .average()
                .orElse(5.0);
        return Math.round(avg * 10.0) / 10.0;
    }

    private void updateRepairmanRating(Long repairmanId) {
        Double avgRating = getRepairmanAvgRating(repairmanId);
        LambdaQueryWrapper<RepairEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairEvaluation::getRepairmanId, repairmanId);
        Long count = evaluationMapper.selectCount(wrapper);

        var repairman = repairmanService.getById(repairmanId);
        if (repairman != null) {
            repairman.setAvgRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP));
            repairman.setTotalRating(count.intValue());
            repairmanService.updateById(repairman);
        }
    }

    private RepairEvaluationVO convertToVO(RepairEvaluation evaluation) {
        RepairEvaluationVO vo = new RepairEvaluationVO();
        vo.setId(evaluation.getId());
        vo.setOrderId(evaluation.getOrderId());
        vo.setUserId(evaluation.getUserId());
        vo.setUserName(evaluation.getUserName());
        vo.setRepairmanId(evaluation.getRepairmanId());
        vo.setRepairmanName(evaluation.getRepairmanName());
        vo.setRating(evaluation.getRating());
        vo.setRatingDesc(evaluation.getRating() + "星");
        vo.setContent(evaluation.getContent());
        
        if (evaluation.getImages() != null) {
            try {
                vo.setImages(objectMapper.readValue(evaluation.getImages(), new TypeReference<List<String>>(){}));
            } catch (Exception e) {
                log.error("图片反序列化失败", e);
            }
        }
        
        vo.setResponseRating(evaluation.getResponseRating());
        vo.setServiceRating(evaluation.getServiceRating());
        vo.setQualityRating(evaluation.getQualityRating());
        vo.setCreateTime(evaluation.getCreateTime());
        
        return vo;
    }
}
