package com.rosy.main.controller;

import com.rosy.main.common.BaseResponse;
import com.rosy.main.common.ResultUtils;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAddRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAuditRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationQueryRequest;
import com.rosy.main.domain.vo.ActivityRegistrationVO;
import com.rosy.main.service.IActivityRegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 活动报名控制器
 */
@RestController
@RequestMapping("/activity-registration")
@RequiredArgsConstructor
@Validated
public class ActivityRegistrationController {

    private final IActivityRegistrationService activityRegistrationService;

    /**
     * 报名活动
     */
    @PostMapping("/register")
    public BaseResponse<Long> registerActivity(@Valid @RequestBody ActivityRegistrationAddRequest request) {
        Long id = activityRegistrationService.registerActivity(request);
        return ResultUtils.success(id);
    }

    /**
     * 审核报名
     */
    @PostMapping("/{id}/audit")
    public BaseResponse<Boolean> auditRegistration(
            @PathVariable @NotNull(message = "ID不能为空") Long id,
            @Valid @RequestBody ActivityRegistrationAuditRequest request) {
        boolean result = activityRegistrationService.auditRegistration(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 签到
     */
    @PostMapping("/{id}/sign-in")
    public BaseResponse<Boolean> signIn(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        boolean result = activityRegistrationService.signIn(id);
        return ResultUtils.success(result);
    }

    /**
     * 签出
     */
    @PostMapping("/{id}/sign-out")
    public BaseResponse<Boolean> signOut(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        boolean result = activityRegistrationService.signOut(id);
        return ResultUtils.success(result);
    }

    /**
     * 获取志愿者的报名列表
     */
    @GetMapping("/volunteer/{volunteerId}")
    public BaseResponse<List<ActivityRegistrationVO>> getVolunteerRegistrations(
            @PathVariable @NotNull(message = "志愿者ID不能为空") Long volunteerId,
            @RequestBody(required = false) ActivityRegistrationQueryRequest request) {
        List<ActivityRegistrationVO> list = activityRegistrationService.getVolunteerRegistrations(volunteerId, request);
        return ResultUtils.success(list);
    }

    /**
     * 获取活动的报名列表
     */
    @GetMapping("/activity/{activityId}")
    public BaseResponse<List<ActivityRegistrationVO>> getActivityRegistrations(
            @PathVariable @NotNull(message = "活动ID不能为空") Long activityId,
            @RequestBody(required = false) ActivityRegistrationQueryRequest request) {
        List<ActivityRegistrationVO> list = activityRegistrationService.getActivityRegistrations(activityId, request);
        return ResultUtils.success(list);
    }
}