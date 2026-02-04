package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.LogTag;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.CheckInRequest;
import com.rosy.main.domain.dto.RegistrationCreateRequest;
import com.rosy.main.domain.dto.RegistrationQueryRequest;
import com.rosy.main.domain.dto.RegistrationReviewRequest;
import com.rosy.main.domain.vo.RegistrationVO;
import com.rosy.main.service.IRegistrationService;
import com.rosy.web.controller.main.utils.UserHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报名管理控制器
 */
@RestController
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {

    @Autowired
    private IRegistrationService registrationService;

    /**
     * 报名活动
     */
    @PostMapping("/register")
    @ValidateRequest
    @LogTag(value = "报名活动", printResult = true)
    public ApiResponse register(@Valid @RequestBody RegistrationCreateRequest request) {
        Long userId = UserHolder.getUserId();
        Long registrationId = registrationService.register(request, userId);
        return ApiResponse.success(registrationId);
    }

    /**
     * 取消报名
     */
    @PostMapping("/cancel")
    @ValidateRequest
    @LogTag(value = "取消报名", printResult = true)
    public ApiResponse cancelRegistration(@Valid @RequestBody IdRequest request) {
        Long userId = UserHolder.getUserId();
        boolean result = registrationService.cancelRegistration(request.getId(), userId);
        return ApiResponse.success(result);
    }

    /**
     * 审核报名
     */
    @PostMapping("/review")
    @ValidateRequest
    @LogTag(value = "审核报名", printResult = true)
    public ApiResponse reviewRegistration(@Valid @RequestBody RegistrationReviewRequest request) {
        Long reviewerId = UserHolder.getUserId();
        boolean result = registrationService.reviewRegistration(request, reviewerId);
        return ApiResponse.success(result);
    }

    /**
     * 签到
     */
    @PostMapping("/check-in")
    @ValidateRequest
    @LogTag(value = "签到", printResult = true)
    public ApiResponse checkIn(@Valid @RequestBody CheckInRequest request) {
        boolean result = registrationService.checkIn(request);
        return ApiResponse.success(result);
    }

    /**
     * 签出
     */
    @PostMapping("/check-out")
    @ValidateRequest
    @LogTag(value = "签出", printResult = true)
    public ApiResponse checkOut(@Valid @RequestBody CheckInRequest request) {
        boolean result = registrationService.checkOut(request);
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取报名详情
     */
    @GetMapping("/get/{id}")
    @LogTag(value = "获取报名详情")
    public ApiResponse getRegistrationById(@PathVariable("id") Long id) {
        RegistrationVO registrationVO = registrationService.getRegistrationById(id);
        return ApiResponse.success(registrationVO);
    }

    /**
     * 分页查询报名列表
     */
    @PostMapping("/list/page")
    @LogTag(value = "分页查询报名列表")
    public ApiResponse listRegistrationsByPage(@RequestBody RegistrationQueryRequest request) {
        Page<RegistrationVO> page = registrationService.listRegistrations(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取我的报名列表
     */
    @GetMapping("/my-list")
    @LogTag(value = "获取我的报名列表")
    public ApiResponse getMyRegistrations() {
        Long userId = UserHolder.getUserId();
        List<RegistrationVO> list = registrationService.getUserRegistrations(userId);
        return ApiResponse.success(list);
    }

    /**
     * 获取活动的报名列表
     */
    @GetMapping("/activity/{activityId}")
    @LogTag(value = "获取活动报名列表")
    public ApiResponse getActivityRegistrations(@PathVariable("activityId") Long activityId) {
        List<RegistrationVO> list = registrationService.getActivityRegistrations(activityId);
        return ApiResponse.success(list);
    }

    /**
     * 检查是否已报名
     */
    @GetMapping("/check/{activityId}")
    @LogTag(value = "检查是否已报名")
    public ApiResponse checkRegistered(@PathVariable("activityId") Long activityId) {
        Long userId = UserHolder.getUserId();
        boolean registered = registrationService.hasRegistered(activityId, userId);
        return ApiResponse.success(registered);
    }
}
