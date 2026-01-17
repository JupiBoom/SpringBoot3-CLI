package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.RepairEvaluationCreateDTO;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.service.IRepairEvaluationService;
import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 维修评价控制器
 */
@RestController
@RequestMapping("/api/repair/evaluation")
@Tag(name = "维修评价管理")
public class RepairEvaluationController {

    @Autowired
    private IRepairEvaluationService repairEvaluationService;

    /**
     * 创建维修评价
     */
    @PostMapping("/create")
    @Operation(summary = "创建维修评价")
    public ApiResponse createEvaluation(@RequestBody RepairEvaluationCreateDTO dto) {
        Long userId = 1L;
        String userName = "测试用户";
        
        RepairEvaluationVO evaluation = repairEvaluationService.createEvaluation(dto, userId, userName);
        return ApiResponse.success(evaluation);
    }

    /**
     * 根据工单ID查询评价
     */
    @GetMapping("/by-order/{orderId}")
    @Operation(summary = "根据工单ID查询评价")
    public ApiResponse getEvaluationByOrderId(@PathVariable Long orderId) {
        RepairEvaluationVO evaluation = repairEvaluationService.getEvaluationByOrderId(orderId);
        if (evaluation == null) {
            return ApiResponse.error("评价不存在");
        }
        return ApiResponse.success(evaluation);
    }

    /**
     * 检查工单是否已评价
     */
    @GetMapping("/exists/{orderId}")
    @Operation(summary = "检查工单是否已评价")
    public ApiResponse existsEvaluation(@PathVariable Long orderId) {
        boolean exists = repairEvaluationService.existsByOrderId(orderId);
        return ApiResponse.success(exists);
    }

    /**
     * 根据用户ID查询评价列表
     */
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "根据用户ID查询评价列表")
    public ApiResponse getEvaluationsByUserId(@PathVariable Long userId) {
        List<RepairEvaluationVO> evaluations = repairEvaluationService.getEvaluationsByUserId(userId);
        return ApiResponse.success(evaluations);
    }

    /**
     * 根据维修人员ID查询评价列表
     */
    @GetMapping("/by-repairman/{repairmanId}")
    @Operation(summary = "根据维修人员ID查询评价列表")
    public ApiResponse getEvaluationsByRepairmanId(@PathVariable Long repairmanId) {
        List<RepairEvaluationVO> evaluations = repairEvaluationService.getEvaluationsByRepairmanId(repairmanId);
        return ApiResponse.success(evaluations);
    }

    /**
     * 获取维修人员平均评分
     */
    @GetMapping("/average-score/{repairmanId}")
    @Operation(summary = "获取维修人员平均评分")
    public R<Double> getAverageScore(@PathVariable Long repairmanId) {
        Double averageScore = repairEvaluationService.getAverageScoreByRepairmanId(repairmanId);
        return R.ok(averageScore);
    }

    /**
     * 获取维修人员评价统计
     */
    @GetMapping("/statistics/{repairmanId}")
    @Operation(summary = "获取维修人员评价统计")
    public R<RepairEvaluationStatisticsVO> getEvaluationStatistics(@PathVariable Long repairmanId) {
        RepairEvaluationStatisticsVO statistics = repairEvaluationService.getEvaluationStatistics(repairmanId);
        return R.ok(statistics);
    }
}

/**
 * 评价统计VO
 */
class RepairEvaluationStatisticsVO {
    private Long repairmanId;
    private Integer totalCount;
    private Double averageScore;
    private Integer fiveStarCount;
    private Integer fourStarCount;
    private Integer threeStarCount;
    private Integer twoStarCount;
    private Integer oneStarCount;

    public Long getRepairmanId() {
        return repairmanId;
    }

    public void setRepairmanId(Long repairmanId) {
        this.repairmanId = repairmanId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(Integer fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }

    public Integer getFourStarCount() {
        return fourStarCount;
    }

    public void setFourStarCount(Integer fourStarCount) {
        this.fourStarCount = fourStarCount;
    }

    public Integer getThreeStarCount() {
        return threeStarCount;
    }

    public void setThreeStarCount(Integer threeStarCount) {
        this.threeStarCount = threeStarCount;
    }

    public Integer getTwoStarCount() {
        return twoStarCount;
    }

    public void setTwoStarCount(Integer twoStarCount) {
        this.twoStarCount = twoStarCount;
    }

    public Integer getOneStarCount() {
        return oneStarCount;
    }

    public void setOneStarCount(Integer oneStarCount) {
        this.oneStarCount = oneStarCount;
    }
}