package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.vo.repair.RepairEvaluationVO;
import com.rosy.main.service.IRepairEvaluationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/evaluation")
public class RepairEvaluationController {

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse createEvaluation(@RequestBody RepairEvaluationAddRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        RepairEvaluationVO vo = repairEvaluationService.createEvaluation(request, userId);
        return ApiResponse.success(vo);
    }

    @GetMapping("/get")
    public ApiResponse getEvaluationByOrderId(@RequestParam Long orderId) {
        RepairEvaluationVO vo = repairEvaluationService.getEvaluationByOrderId(orderId);
        return ApiResponse.success(vo);
    }
}
