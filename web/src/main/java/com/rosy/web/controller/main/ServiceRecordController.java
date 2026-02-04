package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.LogTag;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.ServiceRecordQueryRequest;
import com.rosy.main.domain.dto.ServiceRecordReviewRequest;
import com.rosy.main.domain.dto.VolunteerCommentRequest;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.web.controller.main.utils.UserHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务记录控制器
 */
@RestController
@RequestMapping("/service-record")
@Slf4j
public class ServiceRecordController {

    @Autowired
    private IServiceRecordService serviceRecordService;

    /**
     * 根据ID获取服务记录
     */
    @GetMapping("/get/{id}")
    @LogTag(value = "获取服务记录")
    public ApiResponse getServiceRecordById(@PathVariable("id") Long id) {
        ServiceRecordVO vo = serviceRecordService.getServiceRecordById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 分页查询服务记录
     */
    @PostMapping("/list/page")
    @LogTag(value = "分页查询服务记录")
    public ApiResponse listServiceRecordsByPage(@RequestBody ServiceRecordQueryRequest request) {
        Page<ServiceRecordVO> page = serviceRecordService.listServiceRecords(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取我的服务记录
     */
    @GetMapping("/my-list")
    @LogTag(value = "获取我的服务记录")
    public ApiResponse getMyServiceRecords() {
        Long userId = UserHolder.getUserId();
        List<ServiceRecordVO> list = serviceRecordService.getUserServiceRecords(userId);
        return ApiResponse.success(list);
    }

    /**
     * 获取活动的服务记录
     */
    @GetMapping("/activity/{activityId}")
    @LogTag(value = "获取活动服务记录")
    public ApiResponse getActivityServiceRecords(@PathVariable("activityId") Long activityId) {
        List<ServiceRecordVO> list = serviceRecordService.getActivityServiceRecords(activityId);
        return ApiResponse.success(list);
    }

    /**
     * 评价服务记录（组织者评价）
     */
    @PostMapping("/review")
    @ValidateRequest
    @LogTag(value = "评价服务记录", printResult = true)
    public ApiResponse reviewServiceRecord(@Valid @RequestBody ServiceRecordReviewRequest request) {
        boolean result = serviceRecordService.reviewServiceRecord(request);
        return ApiResponse.success(result);
    }

    /**
     * 志愿者评价
     */
    @PostMapping("/comment")
    @ValidateRequest
    @LogTag(value = "志愿者评价", printResult = true)
    public ApiResponse volunteerComment(@Valid @RequestBody VolunteerCommentRequest request) {
        Long userId = UserHolder.getUserId();
        boolean result = serviceRecordService.volunteerComment(request, userId);
        return ApiResponse.success(result);
    }

    /**
     * 生成服务证明
     */
    @PostMapping("/generate-certificate/{id}")
    @ValidateRequest
    @LogTag(value = "生成服务证明", printResult = true)
    public ApiResponse generateCertificate(@PathVariable("id") Long id) {
        String certificateNo = serviceRecordService.generateCertificate(id);
        return ApiResponse.success(certificateNo);
    }

    /**
     * 获取我的累计服务时长
     */
    @GetMapping("/my-total-hours")
    @LogTag(value = "获取累计服务时长")
    public ApiResponse getMyTotalHours() {
        Long userId = UserHolder.getUserId();
        BigDecimal hours = serviceRecordService.getUserTotalHours(userId);
        return ApiResponse.success(hours);
    }

    /**
     * 获取我的累计服务次数
     */
    @GetMapping("/my-service-count")
    @LogTag(value = "获取累计服务次数")
    public ApiResponse getMyServiceCount() {
        Long userId = UserHolder.getUserId();
        int count = serviceRecordService.getUserServiceCount(userId);
        return ApiResponse.success(count);
    }

    /**
     * 下载服务证明PDF
     */
    @GetMapping("/certificate/{id}")
    @LogTag(value = "下载服务证明")
    public ApiResponse downloadCertificate(@PathVariable("id") Long id) {
        byte[] pdfBytes = serviceRecordService.getCertificatePdf(id);
        return ApiResponse.success(pdfBytes);
    }
}
