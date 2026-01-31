package com.rosy.web.controller.repair;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.EvaluationAddRequest;
import com.rosy.main.domain.vo.EvaluationVO;
import com.rosy.main.service.IEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评价表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/evaluation")
@Tag(name = "评价管理", description = "评价相关接口")
public class EvaluationController {

    @Resource
    private IEvaluationService evaluationService;

    /**
     * 添加评价
     */
    @PostMapping("/add")
    @Operation(summary = "添加评价", description = "用户对已完成的工单进行评价")
    @ValidateRequest
    public ApiResponse<Long> addEvaluation(@RequestBody EvaluationAddRequest evaluationAddRequest,
                                          HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        Long evaluationId = evaluationService.addEvaluation(evaluationAddRequest, userId);
        return ApiResponse.success(evaluationId);
    }

    /**
     * 根据工单ID获取评价
     */
    @GetMapping("/get/by-order")
    @Operation(summary = "获取工单评价", description = "根据工单ID获取该工单的评价")
    public ApiResponse<EvaluationVO> getEvaluationByOrderId(@RequestParam Long orderId) {
        if (orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        EvaluationVO evaluation = evaluationService.getEvaluationByOrderId(orderId);
        return ApiResponse.success(evaluation);
    }
}