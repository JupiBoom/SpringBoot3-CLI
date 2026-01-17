package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairPersonnelAddRequest;
import com.rosy.main.domain.dto.repair.RepairPersonnelQueryRequest;
import com.rosy.main.domain.entity.RepairPersonnel;
import com.rosy.main.domain.vo.repair.RepairPersonnelVO;
import com.rosy.main.mapper.RepairPersonnelMapper;
import com.rosy.main.service.IRepairPersonnelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class RepairPersonnelServiceImpl extends ServiceImpl<RepairPersonnelMapper, RepairPersonnel> implements IRepairPersonnelService {

    @Override
    public RepairPersonnelVO createRepairPersonnel(RepairPersonnelAddRequest request) {
        RepairPersonnel personnel = BeanUtil.copyProperties(request, RepairPersonnel.class);
        personnel.setStatus((byte) 1);
        personnel.setTotalOrders(0);
        personnel.setCompletedOrders(0);
        personnel.setAvgRating(BigDecimal.ZERO);
        boolean saved = save(personnel);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建维修人员失败");
        }
        return getRepairPersonnelVO(personnel.getId());
    }

    @Override
    public RepairPersonnelVO getRepairPersonnelVO(Long personnelId) {
        RepairPersonnel personnel = getById(personnelId);
        if (personnel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "维修人员不存在");
        }
        RepairPersonnelVO vo = BeanUtil.copyProperties(personnel, RepairPersonnelVO.class);
        if (personnel.getSkillLevel() != null) {
            vo.setSkillLevelDesc(personnel.getSkillLevel() == 1 ? "初级" : personnel.getSkillLevel() == 2 ? "中级" : "高级");
        }
        if (personnel.getStatus() != null) {
            vo.setStatusDesc(personnel.getStatus() == 0 ? "离职" : "在职");
        }
        return vo;
    }

    @Override
    public Page<RepairPersonnelVO> pageRepairPersonnel(RepairPersonnelQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairPersonnel> page = page(new Page<>(current, size), getQueryWrapper(request));
        Page<RepairPersonnelVO> voPage = new Page<>(current, size, page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(personnel -> {
                    RepairPersonnelVO vo = BeanUtil.copyProperties(personnel, RepairPersonnelVO.class);
                    if (personnel.getSkillLevel() != null) {
                        vo.setSkillLevelDesc(personnel.getSkillLevel() == 1 ? "初级" : personnel.getSkillLevel() == 2 ? "中级" : "高级");
                    }
                    if (personnel.getStatus() != null) {
                        vo.setStatusDesc(personnel.getStatus() == 0 ? "离职" : "在职");
                    }
                    return vo;
                })
                .toList());
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<RepairPersonnel> getQueryWrapper(RepairPersonnelQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairPersonnel> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, request.getName(), RepairPersonnel::getName);
        QueryWrapperUtil.addCondition(queryWrapper, request.getSpecialty(), RepairPersonnel::getSpecialty);
        QueryWrapperUtil.addCondition(queryWrapper, request.getSkillLevel(), RepairPersonnel::getSkillLevel);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), RepairPersonnel::getStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper, request.getSortField(), request.getSortOrder(), RepairPersonnel::getId);

        return queryWrapper;
    }

    @Override
    public RepairPersonnelVO getAvailablePersonnel(String deviceType) {
        LambdaQueryWrapper<RepairPersonnel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairPersonnel::getStatus, 1);
        queryWrapper.orderByDesc(RepairPersonnel::getAvgRating);
        queryWrapper.orderByAsc(RepairPersonnel::getTotalOrders);
        queryWrapper.last("LIMIT 1");
        RepairPersonnel personnel = getOne(queryWrapper);
        return personnel != null ? getRepairPersonnelVO(personnel.getId()) : null;
    }

    @Override
    public void updatePersonnelStats(Long personnelId, Boolean isCompleted) {
        RepairPersonnel personnel = getById(personnelId);
        if (personnel == null) {
            return;
        }
        personnel.setTotalOrders(personnel.getTotalOrders() + 1);
        if (isCompleted) {
            personnel.setCompletedOrders(personnel.getCompletedOrders() + 1);
        }
        updateById(personnel);
    }
}
