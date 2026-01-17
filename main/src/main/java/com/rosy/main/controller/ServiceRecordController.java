package com.rosy.main.controller;

import com.rosy.main.common.BaseResponse;
import com.rosy.main.common.ResultUtils;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.dto.req.ServiceRecordQueryRequest;
import com.rosy.main.dto.req.ServiceRecordEvaluationRequest;
import com.rosy.main.service.IServiceRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-record")
@RequiredArgsConstructor
@Validated
public class ServiceRecordController {

    private final IServiceRecordService serviceRecordService;

    @GetMapping("/{id}")
    public BaseResponse<ServiceRecordVO> getServiceRecordVO(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        ServiceRecordVO serviceRecordVO = serviceRecordService.getServiceRecordVO(id);
        return ResultUtils.success(serviceRecordVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<ServiceRecordVO>> listServiceRecordVO(@Valid ServiceRecordQueryRequest request) {
        List<ServiceRecordVO> serviceRecordVOList = serviceRecordService.listServiceRecordVO(request);
        return ResultUtils.success(serviceRecordVOList);
    }

    @PostMapping("/{id}/evaluate")
    public BaseResponse<Void> evaluateServiceRecord(
            @PathVariable @NotNull(message = "ID不能为空") Long id,
            @Valid @RequestBody ServiceRecordEvaluationRequest request) {
        serviceRecordService.evaluateServiceRecord(id, request);
        return ResultUtils.success();
    }

    @GetMapping("/{id}/certificate")
    public BaseResponse<String> generateServiceCertificate(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        String certificate = serviceRecordService.generateServiceCertificate(id);
        return ResultUtils.success(certificate);
    }

    @GetMapping("/volunteer/{volunteerId}/stats")
    public BaseResponse<ServiceRecordVO> getVolunteerServiceStats(
            @PathVariable @NotNull(message = "志愿者ID不能为空") Long volunteerId) {
        ServiceRecordVO stats = serviceRecordService.getVolunteerServiceStats(volunteerId);
        return ResultUtils.success(stats);
    }
}