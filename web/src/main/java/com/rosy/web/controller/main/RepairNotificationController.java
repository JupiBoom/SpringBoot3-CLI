package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.service.IRepairNotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/notification")
public class RepairNotificationController {

    @Resource
    private IRepairNotificationService repairNotificationService;

    @PostMapping("/read/{notificationId}")
    public ApiResponse markAsRead(@PathVariable Long notificationId) {
        if (notificationId == null || notificationId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        repairNotificationService.markAsRead(notificationId);
        return ApiResponse.success(true);
    }

    @GetMapping("/unread/count/{userId}")
    public ApiResponse getUnreadCount(@PathVariable Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int count = repairNotificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }
}
