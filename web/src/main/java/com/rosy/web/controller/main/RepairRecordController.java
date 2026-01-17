package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse createRecord(@RequestBody RepairRecordAddRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        RepairRecordVO vo = repairRecordService.createRecord(request, userId);
        return ApiResponse.success(vo);
    }

    @GetMapping("/list")
    public ApiResponse getRecordsByOrderId(@RequestParam Long orderId) {
        List<RepairRecordVO> records = repairRecordService.getRecordsByOrderId(orderId);
        return ApiResponse.success(records);
    }
}
