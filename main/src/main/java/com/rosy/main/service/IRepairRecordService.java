package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.repair.RepairRecordVO;

import java.util.List;

public interface IRepairRecordService extends IService<RepairRecord> {

    RepairRecordVO createRecord(RepairRecordAddRequest request, Long userId);

    List<RepairRecordVO> getRecordsByOrderId(Long orderId);

    RepairRecordVO getRepairRecordVO(RepairRecord record);
}
