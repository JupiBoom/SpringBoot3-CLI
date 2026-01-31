package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;

import java.util.List;

/**
 * <p>
 * 维修记录表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
public interface IRepairRecordService extends IService<RepairRecord> {

    /**
     * 添加维修记录
     * @param repairRecordAddRequest 维修记录添加请求
     * @param technicianId 维修人员ID
     * @return 记录ID
     */
    Long addRepairRecord(RepairRecordAddRequest repairRecordAddRequest, Long technicianId);

    /**
     * 根据工单ID获取维修记录列表
     * @param orderId 工单ID
     * @return 维修记录VO列表
     */
    List<RepairRecordVO> getRepairRecordsByOrderId(Long orderId);

    /**
     * 获取维修记录VO
     * @param repairRecord 维修记录
     * @return 维修记录VO
     */
    RepairRecordVO getRepairRecordVO(RepairRecord repairRecord);
}