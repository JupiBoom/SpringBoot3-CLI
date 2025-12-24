package com.rosy.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.notification.domain.entity.OrderStatusChangeEvent;
import com.rosy.notification.domain.entity.NotificationRecord;
import com.rosy.notification.domain.entity.UserNotificationPreference;
import com.rosy.notification.domain.entity.NotificationTemplate;
import com.rosy.notification.domain.enums.NotificationTypeEnum;
import com.rosy.notification.domain.enums.OrderStatusEnum;
import com.rosy.notification.domain.enums.NotificationSendStatusEnum;
import com.rosy.notification.mapper.NotificationRecordMapper;
import com.rosy.notification.mapper.UserNotificationPreferenceMapper;
import com.rosy.notification.mapper.NotificationTemplateMapper;
import com.rosy.notification.service.INotificationService;
import com.rosy.notification.service.INotificationChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通知服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRecordMapper notificationRecordMapper;
    private final UserNotificationPreferenceMapper userNotificationPreferenceMapper;
    private final NotificationTemplateMapper notificationTemplateMapper;
    private final Map<String, INotificationChannelService> notificationChannelServices;

    @Override
    @Transactional
    public void handleOrderStatusChangeEvent(OrderStatusChangeEvent event) {
        log.info("处理订单状态变更事件: orderId={}, status={}", event.getOrderId(), event.getOrderStatus());

        // 获取用户通知偏好设置
        List<UserNotificationPreference> preferences = getUserNotificationPreferences(event.getUserId());

        // 为每个启用的通知渠道创建通知记录
        for (UserNotificationPreference preference : preferences) {
            if (preference.getEnabled()) {
                createNotificationRecord(event, preference);
            }
        }
    }

    private void createNotificationRecord(OrderStatusChangeEvent event, UserNotificationPreference preference) {
        // 获取通知模板
        NotificationTemplate template = getNotificationTemplate(preference.getNotificationType(), event.getOrderStatus());
        if (template == null) {
            log.warn("未找到通知模板: type={}, status={}", preference.getNotificationType(), event.getOrderStatus());
            return;
        }

        // 构建通知记录
        NotificationRecord record = new NotificationRecord();
        record.setNotificationId(UUID.randomUUID().toString());
        record.setOrderId(event.getOrderId());
        record.setUserId(event.getUserId());
        record.setNotificationType(preference.getNotificationType());
        record.setTemplateId(template.getTemplateId());
        record.setTargetAddress(preference.getChannel());
        record.setSendStatus(NotificationSendStatusEnum.PENDING.getCode());
        record.setSendCount(0);
        record.setRetryCount(0);
        record.setFirstSendTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        // 保存通知记录
        saveNotificationRecord(record);

        // 发送通知
        sendNotification(record);
    }

    @Override
    public void sendNotification(NotificationRecord record) {
        log.info("发送通知: notificationId={}, type={}, target={}", record.getNotificationId(), record.getNotificationType(), record.getTargetAddress());

        try {
            // 更新通知状态为发送中
            record.setSendStatus(NotificationSendStatusEnum.SENDING.getCode());
            record.setSendCount(record.getSendCount() + 1);
            record.setLastSendTime(LocalDateTime.now());
            notificationRecordMapper.updateById(record);

            // 根据通知类型获取对应的渠道服务
            INotificationChannelService channelService = notificationChannelServices.get(record.getNotificationType());
            if (channelService == null) {
                throw new RuntimeException("未找到通知渠道服务: " + record.getNotificationType());
            }

            // 获取通知模板（注意：这里需要根据实际业务逻辑获取订单状态，目前暂时使用默认值）
            // 实际项目中应该从订单服务获取订单状态
            String orderStatus = "pay_success"; // 这里需要替换为真实的订单状态获取逻辑
            NotificationTemplate template = getNotificationTemplate(record.getNotificationType(), orderStatus);
            if (template == null) {
                throw new RuntimeException("未找到通知模板: type=" + record.getNotificationType() + ", status=" + orderStatus);
            }

            // 替换模板变量
            Map<String, Object> variables = buildTemplateVariables(record, template);
            String content = replaceTemplateVariables(template.getTemplateContent(), variables);

            // 发送通知
            boolean success = channelService.sendNotification(record.getTargetAddress(), content);

            // 记录发送结果
            record.setSendStatus(success ? NotificationSendStatusEnum.SUCCESS.getCode() : NotificationSendStatusEnum.FAIL.getCode());
            if (success) {
                record.setSuccessTime(LocalDateTime.now());
            } else {
                record.setErrorMsg("发送失败");
                // 设置重试时间（指数退避）
                record.setNextRetryTime(LocalDateTime.now().plusSeconds((long) Math.pow(2, record.getRetryCount()) * 60));
            }

            // 更新通知记录
            notificationRecordMapper.updateById(record);

        } catch (Exception e) {
            log.error("发送通知失败: notificationId={}", record.getNotificationId(), e);
            record.setSendStatus(NotificationSendStatusEnum.FAIL.getCode());
            record.setErrorMsg(e.getMessage());
            // 设置重试时间（指数退避）
            record.setNextRetryTime(LocalDateTime.now().plusSeconds((long) Math.pow(2, record.getRetryCount()) * 60));
            notificationRecordMapper.updateById(record);
        }
    }

    private Map<String, Object> buildTemplateVariables(NotificationRecord record, NotificationTemplate template) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", record.getOrderId());
        variables.put("productName", "商品名称"); // 从订单服务获取
        variables.put("orderAmount", 0.0); // 从订单服务获取
        variables.put("eventTime", LocalDateTime.now());
        return variables;
    }

    @Override
    public void saveNotificationRecord(NotificationRecord record) {
        notificationRecordMapper.insert(record);
    }

    @Override
    public List<UserNotificationPreference> getUserNotificationPreferences(String userId) {
        QueryWrapper<UserNotificationPreference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userNotificationPreferenceMapper.selectList(queryWrapper);
    }

    @Override
    public NotificationTemplate getNotificationTemplate(String notificationType, String orderStatus) {
        QueryWrapper<NotificationTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notification_type", notificationType)
                .eq("order_status", orderStatus)
                .eq("enabled", true);
        return notificationTemplateMapper.selectOne(queryWrapper);
    }

    @Override
    public String replaceTemplateVariables(String templateContent, Map<String, Object> variables) {
        String content = templateContent;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            content = content.replace(key, value);
        }
        return content;
    }

    @Override
    public void recordNotificationResult(NotificationRecord record) {
        notificationRecordMapper.updateById(record);
    }

    @Override
    public void retrySendNotification(String notificationId) {
        NotificationRecord record = notificationRecordMapper.selectById(notificationId);
        if (record != null) {
            record.setRetryCount(record.getRetryCount() + 1);
            sendNotification(record);
        }
    }
}