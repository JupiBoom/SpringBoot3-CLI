package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.LogTag;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.service.IMeetingNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议通知管理")
@RestController
@RequestMapping("/api/meeting/notification")
public class MeetingNotificationController {

    @Resource
    private IMeetingNotificationService meetingNotificationService;

    @Operation(summary = "获取我的通知")
    @GetMapping("/my")
    public ApiResponse getMyNotifications() {
        List<MeetingNotificationVO> notifications = meetingNotificationService.getNotificationsByUser(1L);
        return ApiResponse.success(notifications);
    }

    @Operation(summary = "获取未读通知")
    @GetMapping("/unread")
    public ApiResponse getUnreadNotifications() {
        List<MeetingNotificationVO> notifications = meetingNotificationService.getUnreadNotifications(1L);
        return ApiResponse.success(notifications);
    }

    @Operation(summary = "标记为已读")
    @PostMapping("/read/{id}")
    @LogTag
    public ApiResponse markAsRead(@PathVariable Long id) {
        Boolean result = meetingNotificationService.markAsRead(id);
        return ApiResponse.success(result);
    }

    @Operation(summary = "全部标记为已读")
    @PostMapping("/read/all")
    @LogTag
    public ApiResponse markAllAsRead() {
        Boolean result = meetingNotificationService.markAllAsRead(1L);
        return ApiResponse.success(result);
    }
}
