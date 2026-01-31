package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairRecordRequest;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRecord(@RequestBody RepairRecordRequest request) {
        RepairRecordVO recordVO = repairRecordService.addRecord(request);
        return ApiResponse.success(recordVO);
    }

    @GetMapping("/list/{orderId}")
    public ApiResponse getRecordsByOrderId(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<RepairRecordVO> records = repairRecordService.getRecordsByOrderId(orderId);
        return ApiResponse.success(records);
    }
}
