package com.rosy.web.controller.main;

import com.rosy.common.core.domain.R;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.domain.vo.NotificationPageVO;
import com.rosy.main.service.INotificationService;
import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/api/repair/notification")
@Tag(name = "通知管理")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    /**
     * 获取用户通知列表（分页）
     */
    @GetMapping("/user/list")
    @Operation(summary = "获取用户通知列表")
    public R<NotificationPageVO> getUserNotifications(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        NotificationPageVO page = notificationService.getUserNotifications(userId, pageNum, pageSize);
        return R.ok(page);
    }

    /**
     * 获取用户未读通知数量
     */
    @GetMapping("/user/unread-count")
    @Operation(summary = "获取用户未读通知数量")
    public R<Map<String, Integer>> getUnreadCount(@RequestParam Long userId) {
        int count = notificationService.getUnreadCount(userId);
        return R.ok(Map.of("unreadCount", count));
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/read/{id}")
    @Operation(summary = "标记通知为已读")
    public R<Boolean> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markAsRead(id);
        return R.ok(success);
    }

    /**
     * 标记所有通知为已读
     */
    @PutMapping("/read-all")
    @Operation(summary = "标记所有通知为已读")
    public R<Boolean> markAllAsRead(@RequestParam Long userId) {
        boolean success = notificationService.markAllAsRead(userId);
        return R.ok(success);
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除通知")
    public R<Boolean> deleteNotification(@PathVariable Long id) {
        boolean success = notificationService.deleteNotification(id);
        return R.ok(success);
    }

    /**
     * 批量删除通知
     */
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除通知")
    public R<Boolean> batchDeleteNotifications(@RequestBody List<Long> ids) {
        boolean success = notificationService.batchDeleteNotifications(ids);
        return R.ok(success);
    }
}