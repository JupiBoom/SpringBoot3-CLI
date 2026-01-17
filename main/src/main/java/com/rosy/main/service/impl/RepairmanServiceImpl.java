package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Repairman;
import com.rosy.main.mapper.RepairmanMapper;
import com.rosy.main.service.IRepairmanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 维修人员Service实现类
 */
@Service
@RequiredArgsConstructor
public class RepairmanServiceImpl extends ServiceImpl<RepairmanMapper, Repairman> implements IRepairmanService {

    private final RepairmanMapper repairmanMapper;

    @Override
    public Repairman findAvailableRepairman(String equipmentType) {
        LambdaQueryWrapper<Repairman> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Repairman::getStatus, "active");
        
        if (equipmentType != null) {
            wrapper.like(Repairman::getSpecialty, equipmentType);
        }
        
        wrapper.orderByDesc(Repairman::getAvgRating)
               .orderByAsc(Repairman::getCompletedOrders)
               .last("LIMIT 1");
        
        return repairmanMapper.selectOne(wrapper);
    }

    @Override
    public Repairman getByUserId(Long userId) {
        LambdaQueryWrapper<Repairman> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Repairman::getUserId, userId);
        return repairmanMapper.selectOne(wrapper);
    }
}
