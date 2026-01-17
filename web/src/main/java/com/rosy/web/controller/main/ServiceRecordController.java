package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.service.IServiceRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-record")
public class ServiceRecordController {

    @Resource
    private IServiceRecordService serviceRecordService;

    @PostMapping("/check-in")
    @ValidateRequest
    public ApiResponse checkIn(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean result = serviceRecordService.checkIn(activityId, userId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/check-out")
    @ValidateRequest
    public ApiResponse checkOut(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean result = serviceRecordService.checkOut(activityId, userId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/rate")
    @ValidateRequest
    public ApiResponse rateService(@RequestParam Long activityId, @RequestParam Long userId, @RequestParam byte rating, @RequestParam(required = false) String comment) {
        boolean result = serviceRecordService.rateService(activityId, userId, rating, comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/certificate")
    public ApiResponse generateCertificate(@RequestParam Long activityId, @RequestParam Long userId) {
        String certificateUrl = serviceRecordService.generateCertificate(activityId, userId);
        return ApiResponse.success(certificateUrl);
    }

    @GetMapping("/list")
    public ApiResponse listServiceRecords(@RequestParam(required = false) Long activityId, @RequestParam(required = false) Long userId) {
        var queryWrapper = serviceRecordService.getQueryWrapper(activityId, userId);
        return ApiResponse.success(serviceRecordService.list(queryWrapper));
    }

    @GetMapping("/list/page/vo")
    public ApiResponse listServiceRecordVOByPage(@RequestParam(required = false) Long activityId, @RequestParam(required = false) Long userId, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        var queryWrapper = serviceRecordService.getQueryWrapper(activityId, userId);
        Page<ServiceRecord> page = serviceRecordService.page(new Page<>(current, size), queryWrapper);
        Page<ServiceRecordVO> voPage = PageUtils.convert(page, serviceRecordService::getServiceRecordVO);
        return ApiResponse.success(voPage);
    }

    @GetMapping("/get/vo")
    public ApiResponse getServiceRecordVOById(@RequestParam Long id) {
        ServiceRecord serviceRecord = serviceRecordService.getById(id);
        ThrowUtils.throwIf(serviceRecord == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(serviceRecordService.getServiceRecordVO(serviceRecord));
    }
}
