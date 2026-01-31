package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairStaffVO;

import java.util.List;

public interface IRepairStaffService extends IService<RepairStaff> {

    RepairStaffVO getStaffById(Long staffId);

    List<RepairStaffVO> getAllAvailableStaff();

    RepairStaff getAvailableStaffWithLeastOrders();

    RepairStaff getAvailableStaffBySpecialty(String specialty);

    List<RepairStaffVO> getStaffRanking();
}
