package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.enums.RepairStatusEnum;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修记录表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public Long addRepairRecord(RepairRecordAddRequest repairRecordAddRequest, Long technicianId) {
        // 参数校验
        ThrowUtils.throwIf(repairRecordAddRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        ThrowUtils.throwIf(repairRecordAddRequest.getOrderId() == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(repairRecordAddRequest.getRepairContent()), ErrorCode.PARAMS_ERROR, "维修内容不能为空");

        // 查询工单
        RepairOrder repairOrder = repairOrderService.getById(repairRecordAddRequest.getOrderId());
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(!technicianId.equals(repairOrder.getAssignedTo()), 
                         ErrorCode.OPERATION_ERROR, "只能为自己负责的工单添加维修记录");
        ThrowUtils.throwIf(repairOrder.getStatus() != RepairStatusEnum.IN_PROGRESS.getCode(), 
                         ErrorCode.OPERATION_ERROR, "只能为维修中的工单添加维修记录");

        // 创建维修记录
        RepairRecord repairRecord = new RepairRecord();
        BeanUtil.copyProperties(repairRecordAddRequest, repairRecord);
        
        // 设置记录时间
        if (StrUtil.isNotBlank(repairRecordAddRequest.getRecordTime())) {
            try {
                repairRecord.setRecordTime(LocalDateTime.parse(repairRecordAddRequest.getRecordTime(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } catch (Exception e) {
                repairRecord.setRecordTime(LocalDateTime.now());
            }
        } else {
            repairRecord.setRecordTime(LocalDateTime.now());
        }

        // 处理维修照片
        if (repairRecordAddRequest.getRepairImages() != null && !repairRecordAddRequest.getRepairImages().isEmpty()) {
            repairRecord.setRepairImages(JSON.toJSONString(repairRecordAddRequest.getRepairImages()));
        }

        // 保存维修记录
        boolean result = this.save(repairRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加维修记录失败");

        return repairRecord.getId();
    }

    @Override
    public List<RepairRecordVO> getRepairRecordsByOrderId(Long orderId) {
        // 参数校验
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");

        // 查询维修记录
        QueryWrapper<RepairRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.orderByAsc("record_time");
        List<RepairRecord> repairRecords = this.list(queryWrapper);

        // 转换为VO
        return repairRecords.stream()
                .map(this::getRepairRecordVO)
                .collect(Collectors.toList());
    }

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord repairRecord) {
        RepairRecordVO repairRecordVO = new RepairRecordVO();
        BeanUtil.copyProperties(repairRecord, repairRecordVO);

        // 获取工单号
        if (repairRecord.getOrderId() != null) {
            RepairOrder repairOrder = repairOrderService.getById(repairRecord.getOrderId());
            if (repairOrder != null) {
                repairRecordVO.setOrderNo(repairOrder.getOrderNo());
            }
        }

        // 解析维修照片
        if (StrUtil.isNotBlank(repairRecord.getRepairImages())) {
            try {
                repairRecordVO.setRepairImages(JSON.parseArray(repairRecord.getRepairImages(), String.class));
            } catch (Exception e) {
                // 忽略解析异常
            }
        }

        return repairRecordVO;
    }
}