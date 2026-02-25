package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.ServiceRatingDTO;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.service.IServiceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-record")
@Tag(name = "服务记录管理")
public class ServiceRecordController {

    @Resource
    private IServiceRecordService serviceRecordService;

    @PostMapping("/check-in")
    @ValidateRequest
    @Operation(summary = "签到")
    public ApiResponse checkIn(@RequestBody CheckInOutRequest request) {
        ServiceRecord record = serviceRecordService.checkIn(request.getRegistrationId(), request.getUserId());
        return ApiResponse.success(record);
    }

    @PostMapping("/check-out")
    @ValidateRequest
    @Operation(summary = "签出")
    public ApiResponse checkOut(@RequestBody CheckInOutRequest request) {
        ServiceRecord record = serviceRecordService.checkOut(request.getRegistrationId(), request.getUserId());
        return ApiResponse.success(record);
    }

    @PostMapping("/rate")
    @ValidateRequest
    @Operation(summary = "服务评价")
    public ApiResponse rate(@RequestBody ServiceRatingDTO dto) {
        serviceRecordService.rate(dto, 1L);
        return ApiResponse.success(true);
    }

    @PostMapping("/certificate")
    @ValidateRequest
    @Operation(summary = "生成服务证书")
    public ApiResponse generateCertificate(@RequestBody IdRequest idRequest) {
        String certificate = serviceRecordService.generateCertificate(idRequest.getId(), 1L);
        return ApiResponse.success(certificate);
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的服务记录")
    public ApiResponse getMyServiceRecords(Long userId) {
        List<ServiceRecord> records = serviceRecordService.getMyServiceRecords(userId);
        return ApiResponse.success(records);
    }

    @Data
    public static class CheckInOutRequest {
        private Long registrationId;
        private Long userId;
    }
}
