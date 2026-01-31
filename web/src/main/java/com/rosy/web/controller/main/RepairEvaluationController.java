package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairEvaluationRequest;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.service.IRepairEvaluationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/evaluation")
public class RepairEvaluationController {

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse submitEvaluation(@RequestBody RepairEvaluationRequest request) {
        RepairEvaluationVO evaluationVO = repairEvaluationService.submitEvaluation(request);
        return ApiResponse.success(evaluationVO);
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse getEvaluationByOrderId(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairEvaluationVO evaluationVO = repairEvaluationService.getEvaluationByOrderId(orderId);
        return ApiResponse.success(evaluationVO);
    }

    @PostMapping("/staff/{staffId}")
    @ValidateRequest
    public ApiResponse getEvaluationsByStaffId(@PathVariable Long staffId, @RequestBody PageRequest pageRequest) {
        if (staffId == null || staffId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<RepairEvaluationVO> page = repairEvaluationService.getEvaluationsByStaffId(staffId, pageRequest);
        return ApiResponse.success(page);
    }
}
