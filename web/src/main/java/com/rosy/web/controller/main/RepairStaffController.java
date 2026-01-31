package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.RepairStaffVO;
import com.rosy.main.service.IRepairStaffService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair/staff")
public class RepairStaffController {

    @Resource
    private IRepairStaffService repairStaffService;

    @GetMapping("/detail/{staffId}")
    public ApiResponse getStaffById(@PathVariable Long staffId) {
        if (staffId == null || staffId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairStaffVO staffVO = repairStaffService.getStaffById(staffId);
        return ApiResponse.success(staffVO);
    }

    @GetMapping("/available")
    public ApiResponse getAllAvailableStaff() {
        List<RepairStaffVO> staffList = repairStaffService.getAllAvailableStaff();
        return ApiResponse.success(staffList);
    }

    @GetMapping("/ranking")
    public ApiResponse getStaffRanking() {
        List<RepairStaffVO> staffList = repairStaffService.getStaffRanking();
        return ApiResponse.success(staffList);
    }
}
