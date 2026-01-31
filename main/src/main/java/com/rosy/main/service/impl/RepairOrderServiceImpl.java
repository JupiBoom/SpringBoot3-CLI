package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.dto.repair.RepairOrderUpdateRequest;
import com.rosy.main.domain.entity.DeviceType;
import com.rosy.main.domain.entity.FaultType;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.enums.PriorityEnum;
import com.rosy.main.enums.RepairStatusEnum;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.IDeviceTypeService;
import com.rosy.main.service.IFaultTypeService;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 设备报修工单表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Resource
    private IDeviceTypeService deviceTypeService;

    @Resource
    private IFaultTypeService faultTypeService;

    @Override
    public Long createRepairOrder(RepairOrderAddRequest repairOrderAddRequest, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(repairOrderAddRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        ThrowUtils.throwIf(repairOrderAddRequest.getDeviceTypeId() == null, ErrorCode.PARAMS_ERROR, "设备类型不能为空");
        ThrowUtils.throwIf(repairOrderAddRequest.getFaultTypeId() == null, ErrorCode.PARAMS_ERROR, "故障类型不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(repairOrderAddRequest.getDeviceLocation()), ErrorCode.PARAMS_ERROR, "设备位置不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(repairOrderAddRequest.getFaultDescription()), ErrorCode.PARAMS_ERROR, "故障描述不能为空");

        // 验证设备类型和故障类型是否存在
        DeviceType deviceType = deviceTypeService.getById(repairOrderAddRequest.getDeviceTypeId());
        ThrowUtils.throwIf(deviceType == null, ErrorCode.PARAMS_ERROR, "设备类型不存在");

        FaultType faultType = faultTypeService.getById(repairOrderAddRequest.getFaultTypeId());
        ThrowUtils.throwIf(faultType == null, ErrorCode.PARAMS_ERROR, "故障类型不存在");

        // 创建工单
        RepairOrder repairOrder = new RepairOrder();
        BeanUtil.copyProperties(repairOrderAddRequest, repairOrder);
        
        // 生成工单号
        String orderNo = "RO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
                         String.format("%04d", (int)(Math.random() * 10000));
        repairOrder.setOrderNo(orderNo);
        
        // 设置默认状态和优先级
        repairOrder.setStatus((byte) RepairStatusEnum.PENDING.getCode());
        if (repairOrder.getPriority() == null) {
            repairOrder.setPriority((byte) PriorityEnum.MEDIUM.getCode());
        }
        
        // 设置用户ID
        repairOrder.setUserId(userId);

        // 处理故障照片
        if (repairOrderAddRequest.getFaultImages() != null && !repairOrderAddRequest.getFaultImages().isEmpty()) {
            repairOrder.setFaultImages(JSON.toJSONString(repairOrderAddRequest.getFaultImages()));
        }

        // 保存工单
        boolean result = this.save(repairOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建工单失败");

        return repairOrder.getId();
    }

    @Override
    public boolean updateRepairOrder(RepairOrderUpdateRequest repairOrderUpdateRequest) {
        // 参数校验
        ThrowUtils.throwIf(repairOrderUpdateRequest == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        ThrowUtils.throwIf(repairOrderUpdateRequest.getId() == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");

        // 查询原工单
        RepairOrder oldRepairOrder = this.getById(repairOrderUpdateRequest.getId());
        ThrowUtils.throwIf(oldRepairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");

        // 更新工单
        RepairOrder repairOrder = new RepairOrder();
        BeanUtil.copyProperties(repairOrderUpdateRequest, repairOrder);

        // 处理故障照片
        if (repairOrderUpdateRequest.getFaultImages() != null) {
            if (repairOrderUpdateRequest.getFaultImages().isEmpty()) {
                repairOrder.setFaultImages(null);
            } else {
                repairOrder.setFaultImages(JSON.toJSONString(repairOrderUpdateRequest.getFaultImages()));
            }
        }

        return this.updateById(repairOrder);
    }

    @Override
    public Page<RepairOrder> pageRepairOrder(RepairOrderQueryRequest repairOrderQueryRequest) {
        long current = repairOrderQueryRequest.getCurrent();
        long size = repairOrderQueryRequest.getPageSize();
        return this.page(new Page<>(current, size), getQueryWrapper(repairOrderQueryRequest));
    }

    @Override
    public Page<RepairOrderVO> pageRepairOrderVO(RepairOrderQueryRequest repairOrderQueryRequest) {
        Page<RepairOrder> repairOrderPage = pageRepairOrder(repairOrderQueryRequest);
        Page<RepairOrderVO> voPage = new Page<>(repairOrderPage.getCurrent(), repairOrderPage.getSize(), repairOrderPage.getTotal());
        List<RepairOrderVO> voList = repairOrderPage.getRecords().stream()
                .map(this::getRepairOrderVO)
                .collect(java.util.stream.Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        RepairOrderVO repairOrderVO = new RepairOrderVO();
        BeanUtil.copyProperties(repairOrder, repairOrderVO);

        // 设置状态描述
        RepairStatusEnum statusEnum = RepairStatusEnum.getByCode(repairOrder.getStatus());
        if (statusEnum != null) {
            repairOrderVO.setStatusDesc(statusEnum.getDescription());
        }

        // 设置优先级描述
        PriorityEnum priorityEnum = PriorityEnum.getByCode(repairOrder.getPriority());
        if (priorityEnum != null) {
            repairOrderVO.setPriorityDesc(priorityEnum.getDescription());
        }

        // 设置设备类型名称
        if (repairOrder.getDeviceTypeId() != null) {
            DeviceType deviceType = deviceTypeService.getById(repairOrder.getDeviceTypeId());
            if (deviceType != null) {
                repairOrderVO.setDeviceTypeName(deviceType.getName());
            }
        }

        // 设置故障类型名称
        if (repairOrder.getFaultTypeId() != null) {
            FaultType faultType = faultTypeService.getById(repairOrder.getFaultTypeId());
            if (faultType != null) {
                repairOrderVO.setFaultTypeName(faultType.getName());
            }
        }

        // 解析故障照片
        if (StrUtil.isNotBlank(repairOrder.getFaultImages())) {
            try {
                repairOrderVO.setFaultImages(JSON.parseArray(repairOrder.getFaultImages(), String.class));
            } catch (Exception e) {
                // 忽略解析异常
            }
        }

        return repairOrderVO;
    }

    @Override
    public boolean autoAssignOrder(Long orderId) {
        // TODO: 实现自动分配逻辑，可以根据维修人员的工作负载、技能匹配等因素进行分配
        // 这里简化处理，随机分配一个维修人员
        return manualAssignOrder(orderId, 1L); // 假设ID为1的用户是维修人员
    }

    @Override
    public boolean manualAssignOrder(Long orderId, Long technicianId) {
        // 参数校验
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        ThrowUtils.throwIf(technicianId == null, ErrorCode.PARAMS_ERROR, "维修人员ID不能为空");

        // 查询工单
        RepairOrder repairOrder = this.getById(orderId);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(repairOrder.getStatus() != RepairStatusEnum.PENDING.getCode(), 
                         ErrorCode.OPERATION_ERROR, "只有待处理的工单才能分配");

        // 更新工单
        RepairOrder updateOrder = new RepairOrder();
        updateOrder.setId(orderId);
        updateOrder.setAssignedTo(technicianId);
        updateOrder.setAssignedTime(LocalDateTime.now());

        return this.updateById(updateOrder);
    }

    @Override
    public boolean acceptOrder(Long orderId, Long technicianId) {
        // 参数校验
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        ThrowUtils.throwIf(technicianId == null, ErrorCode.PARAMS_ERROR, "维修人员ID不能为空");

        // 查询工单
        RepairOrder repairOrder = this.getById(orderId);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(!technicianId.equals(repairOrder.getAssignedTo()), 
                         ErrorCode.OPERATION_ERROR, "只能接单分配给自己的工单");
        ThrowUtils.throwIf(repairOrder.getStatus() != RepairStatusEnum.PENDING.getCode(), 
                         ErrorCode.OPERATION_ERROR, "只有待处理的工单才能接单");

        // 更新工单
        RepairOrder updateOrder = new RepairOrder();
        updateOrder.setId(orderId);
        updateOrder.setStatus((byte) RepairStatusEnum.IN_PROGRESS.getCode());
        updateOrder.setAcceptedTime(LocalDateTime.now());

        return this.updateById(updateOrder);
    }

    @Override
    public boolean completeOrder(Long orderId) {
        // 参数校验
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");

        // 查询工单
        RepairOrder repairOrder = this.getById(orderId);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        ThrowUtils.throwIf(repairOrder.getStatus() != RepairStatusEnum.IN_PROGRESS.getCode(), 
                         ErrorCode.OPERATION_ERROR, "只有维修中的工单才能完成");

        // 更新工单
        RepairOrder updateOrder = new RepairOrder();
        updateOrder.setId(orderId);
        updateOrder.setStatus((byte) RepairStatusEnum.COMPLETED.getCode());
        updateOrder.setCompletedTime(LocalDateTime.now());

        return this.updateById(updateOrder);
    }

    @Override
    public QueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest repairOrderQueryRequest) {
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        
        if (repairOrderQueryRequest == null) {
            return queryWrapper;
        }

        // 工单号
        String orderNo = repairOrderQueryRequest.getOrderNo();
        if (StrUtil.isNotBlank(orderNo)) {
            queryWrapper.like("order_no", orderNo);
        }

        // 设备类型ID
        Long deviceTypeId = repairOrderQueryRequest.getDeviceTypeId();
        if (deviceTypeId != null) {
            queryWrapper.eq("device_type_id", deviceTypeId);
        }

        // 故障类型ID
        Long faultTypeId = repairOrderQueryRequest.getFaultTypeId();
        if (faultTypeId != null) {
            queryWrapper.eq("fault_type_id", faultTypeId);
        }

        // 设备位置
        String deviceLocation = repairOrderQueryRequest.getDeviceLocation();
        if (StrUtil.isNotBlank(deviceLocation)) {
            queryWrapper.like("device_location", deviceLocation);
        }

        // 报修状态
        Byte status = repairOrderQueryRequest.getStatus();
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        // 优先级
        Byte priority = repairOrderQueryRequest.getPriority();
        if (priority != null) {
            queryWrapper.eq("priority", priority);
        }

        // 报修用户ID
        Long userId = repairOrderQueryRequest.getUserId();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }

        // 分配给的维修人员ID
        Long assignedTo = repairOrderQueryRequest.getAssignedTo();
        if (assignedTo != null) {
            queryWrapper.eq("assigned_to", assignedTo);
        }

        // 创建时间范围
        LocalDateTime createTimeBegin = repairOrderQueryRequest.getCreateTimeBegin();
        LocalDateTime createTimeEnd = repairOrderQueryRequest.getCreateTimeEnd();
        if (createTimeBegin != null) {
            queryWrapper.ge("create_time", createTimeBegin);
        }
        if (createTimeEnd != null) {
            queryWrapper.le("create_time", createTimeEnd);
        }

        // 排序
        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }
}