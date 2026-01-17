package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Repairman;

/**
 * 维修人员Service接口
 */
public interface IRepairmanService extends IService<Repairman> {

    Repairman findAvailableRepairman(String equipmentType);

    Repairman getByUserId(Long userId);
}
