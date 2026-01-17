package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairPersonnelAddRequest;
import com.rosy.main.domain.dto.repair.RepairPersonnelQueryRequest;
import com.rosy.main.domain.entity.RepairPersonnel;
import com.rosy.main.domain.vo.repair.RepairPersonnelVO;

public interface IRepairPersonnelService extends IService<RepairPersonnel> {

    RepairPersonnelVO createRepairPersonnel(RepairPersonnelAddRequest request);

    RepairPersonnelVO getRepairPersonnelVO(Long personnelId);

    Page<RepairPersonnelVO> pageRepairPersonnel(RepairPersonnelQueryRequest request);

    LambdaQueryWrapper<RepairPersonnel> getQueryWrapper(RepairPersonnelQueryRequest request);

    RepairPersonnelVO getAvailablePersonnel(String deviceType);

    void updatePersonnelStats(Long personnelId, Boolean isCompleted);
}
