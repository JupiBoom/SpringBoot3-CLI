package com.rosy.web.controller.repair;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 维修记录表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/record")
@Tag(name = "维修记录管理", description = "维修记录相关接口")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    /**
     * 添加维修记录
     */
    @PostMapping("/add")
    @Operation(summary = "添加维修记录", description = "维修人员添加维修过程记录")
    @ValidateRequest
    public ApiResponse<Long> addRepairRecord(@RequestBody RepairRecordAddRequest repairRecordAddRequest,
                                            HttpServletRequest request) {
        // TODO: 从请求中获取当前登录维修人员ID
        Long technicianId = 2L; // 临时硬编码，实际应从登录信息中获取
        
        Long recordId = repairRecordService.addRepairRecord(repairRecordAddRequest, technicianId);
        return ApiResponse.success(recordId);
    }

    /**
     * 根据工单ID获取维修记录列表
     */
    @GetMapping("/list/by-order")
    @Operation(summary = "获取工单维修记录", description = "根据工单ID获取该工单的所有维修记录")
    public ApiResponse<List<RepairRecordVO>> getRepairRecordsByOrderId(@RequestParam Long orderId) {
        if (orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        List<RepairRecordVO> repairRecords = repairRecordService.getRepairRecordsByOrderId(orderId);
        return ApiResponse.success(repairRecords);
    }
}