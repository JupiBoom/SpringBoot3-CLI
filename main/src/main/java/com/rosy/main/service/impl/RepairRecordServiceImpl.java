package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RepairActionTypeEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairRecordRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import com.rosy.main.service.IRepairStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    private final IRepairStaffService repairStaffService;

    @Override
    public RepairRecordVO addRecord(RepairRecordRequest request) {
        RepairRecord record = new RepairRecord();
        BeanUtil.copyProperties(request, record);
        save(record);
        return convertToVO(record);
    }

    @Override
    public List<RepairRecordVO> getRecordsByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRecord::getOrderId, orderId)
               .orderByAsc(RepairRecord::getCreateTime);
        List<RepairRecord> records = list(wrapper);
        return records.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private RepairRecordVO convertToVO(RepairRecord record) {
        RepairRecordVO vo = BeanUtil.copyProperties(record, RepairRecordVO.class);
        vo.setActionTypeDesc(RepairActionTypeEnum.getDescByCode(record.getActionType().intValue()));
        
        if (record.getStaffId() != null) {
            RepairStaff staff = repairStaffService.getById(record.getStaffId());
            if (staff != null) {
                vo.setStaffName(staff.getStaffName());
            }
        }
        
        return vo;
    }
}
