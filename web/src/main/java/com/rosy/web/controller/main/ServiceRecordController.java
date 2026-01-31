package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.service.IServiceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 服务记录控制器
 */
@Tag(name = "服务记录管理", description = "服务记录相关接口")
@RestController
@RequestMapping("/service-record")
public class ServiceRecordController {

    @Autowired
    private IServiceRecordService serviceRecordService;

    @Operation(summary = "签到")
    @PostMapping("/check-in")
    public ApiResponse<Boolean> checkIn(@RequestParam Long activityId, @RequestParam Long userId) {
        try {
            boolean result = serviceRecordService.checkIn(activityId, userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "签出")
    @PostMapping("/check-out")
    public ApiResponse<Boolean> checkOut(@RequestParam Long activityId, @RequestParam Long userId) {
        try {
            boolean result = serviceRecordService.checkOut(activityId, userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "评价服务")
    @PostMapping("/rate")
    public ApiResponse<Boolean> rateService(@RequestParam Long activityId, 
                                           @RequestParam Long userId,
                                           @RequestParam Integer rating,
                                           @RequestParam(required = false) String feedback) {
        try {
            boolean result = serviceRecordService.rateService(activityId, userId, rating, feedback);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "生成服务证明")
    @PostMapping("/certificate")
    public ApiResponse<String> generateCertificate(@RequestParam Long activityId, @RequestParam Long userId) {
        try {
            String certificateId = serviceRecordService.generateCertificate(activityId, userId);
            return ApiResponse.success(certificateId);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取服务记录详情")
    @GetMapping("/{id}")
    public ApiResponse<ServiceRecord> getServiceRecordById(@Parameter(description = "服务记录ID") @PathVariable Long id) {
        ServiceRecord serviceRecord = serviceRecordService.getById(id);
        if (serviceRecord != null) {
            return ApiResponse.success(serviceRecord);
        } else {
            return ApiResponse.error("服务记录不存在");
        }
    }

    @Operation(summary = "分页查询服务记录")
    @GetMapping("/page")
    public ApiResponse<Page<ServiceRecord>> getServiceRecordPage(PageRequest pageRequest,
                                                               @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
                                                               @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                               @Parameter(description = "评价星级") @RequestParam(required = false) Integer rating) {
        Page<ServiceRecord> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<ServiceRecord> result = serviceRecordService.getServiceRecordPage(page, activityId, userId, rating);
        return ApiResponse.success(result);
    }

    @Operation(summary = "计算用户总服务时长")
    @GetMapping("/total-hours")
    public ApiResponse<BigDecimal> calculateTotalServiceHours(@RequestParam Long userId) {
        BigDecimal totalHours = serviceRecordService.calculateTotalServiceHours(userId);
        return ApiResponse.success(totalHours);
    }

    @Operation(summary = "检查用户是否已签到")
    @GetMapping("/check-in-status")
    public ApiResponse<Boolean> isCheckedIn(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean result = serviceRecordService.isCheckedIn(activityId, userId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "检查用户是否已签出")
    @GetMapping("/check-out-status")
    public ApiResponse<Boolean> isCheckedOut(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean result = serviceRecordService.isCheckedOut(activityId, userId);
        return ApiResponse.success(result);
    }
}