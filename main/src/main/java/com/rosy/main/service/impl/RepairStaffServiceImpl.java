package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.RepairStaffStatusEnum;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairStaffVO;
import com.rosy.main.mapper.RepairStaffMapper;
import com.rosy.main.service.IRepairStaffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairStaffServiceImpl extends ServiceImpl<RepairStaffMapper, RepairStaff> implements IRepairStaffService {

    @Override
    public RepairStaffVO getStaffById(Long staffId) {
        RepairStaff staff = getById(staffId);
        return staff != null ? convertToVO(staff) : null;
    }

    @Override
    public List<RepairStaffVO> getAllAvailableStaff() {
        LambdaQueryWrapper<RepairStaff> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairStaff::getStatus, RepairStaffStatusEnum.ONLINE.getCode())
               .orderByAsc(RepairStaff::getOrderCount);
        List<RepairStaff> staffList = list(wrapper);
        return staffList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public RepairStaff getAvailableStaffWithLeastOrders() {
        return baseMapper.selectAvailableStaffWithLeastOrders();
    }

    @Override
    public RepairStaff getAvailableStaffBySpecialty(String specialty) {
        return baseMapper.selectAvailableStaffBySpecialty(specialty);
    }

    @Override
    public List<RepairStaffVO> getStaffRanking() {
        LambdaQueryWrapper<RepairStaff> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RepairStaff::getOrderCount);
        List<RepairStaff> staffList = list(wrapper);
        return staffList.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private RepairStaffVO convertToVO(RepairStaff staff) {
        RepairStaffVO vo = BeanUtil.copyProperties(staff, RepairStaffVO.class);
        vo.setStatusDesc(RepairStaffStatusEnum.getDescByCode(staff.getStatus().intValue()));
        return vo;
    }
}
