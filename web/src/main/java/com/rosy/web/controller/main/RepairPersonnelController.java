package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairPersonnelAddRequest;
import com.rosy.main.domain.dto.repair.RepairPersonnelQueryRequest;
import com.rosy.main.domain.dto.repair.RepairPersonnelUpdateRequest;
import com.rosy.main.domain.entity.RepairPersonnel;
import com.rosy.main.domain.vo.repair.RepairPersonnelVO;
import com.rosy.main.service.IRepairPersonnelService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/personnel")
public class RepairPersonnelController {

    @Resource
    private IRepairPersonnelService repairPersonnelService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse createRepairPersonnel(@RequestBody RepairPersonnelAddRequest request) {
        RepairPersonnelVO vo = repairPersonnelService.createRepairPersonnel(request);
        return ApiResponse.success(vo);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairPersonnel(@RequestBody IdRequest request) {
        boolean result = repairPersonnelService.removeById(request.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRepairPersonnel(@RequestBody RepairPersonnelUpdateRequest request) {
        RepairPersonnel personnel = BeanUtil.copyProperties(request, RepairPersonnel.class);
        boolean result = repairPersonnelService.updateById(personnel);
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    public ApiResponse getRepairPersonnelById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairPersonnelVO vo = repairPersonnelService.getRepairPersonnelVO(id);
        return ApiResponse.success(vo);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairPersonnelByPage(@RequestBody RepairPersonnelQueryRequest request) {
        Page<RepairPersonnelVO> page = repairPersonnelService.pageRepairPersonnel(request);
        return ApiResponse.success(page);
    }

    @GetMapping("/available")
    public ApiResponse getAvailablePersonnel(@RequestParam String deviceType) {
        RepairPersonnelVO vo = repairPersonnelService.getAvailablePersonnel(deviceType);
        return ApiResponse.success(vo);
    }
}
