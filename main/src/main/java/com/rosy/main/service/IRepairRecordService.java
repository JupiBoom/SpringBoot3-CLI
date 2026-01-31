package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.RepairRecordRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;

import java.util.List;

public interface IRepairRecordService extends IService<RepairRecord> {

    RepairRecordVO addRecord(RepairRecordRequest request);

    List<RepairRecordVO> getRecordsByOrderId(Long orderId);
}
