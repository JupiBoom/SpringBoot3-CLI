package com.rosy.main.service.impl;

import com.rosy.main.domain.entity.Order;
import com.rosy.main.domain.entity.UserNotificationPreference;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.INotificationRecordService;
import com.rosy.main.service.IUserNotificationPreferenceService;
import com.rosy.main.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知服务实现类
 *
 * @author rosy
 */
@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private ITemplateService templateService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private WeChatNotificationService weChatNotificationService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private INotificationRecordService notificationRecordService;

    @Autowired
    private IUserNotificationPreferenceService userNotificationPreferenceService;

    @Override
    public void sendPaymentSuccessNotification(Order order) {
        // 获取支付成功模板
        String template = templateService.getTemplate("payment_success");
        // 替换模板变量
        Map<String, String> params = new HashMap<>();
        params.put("orderNo", order.getOrderNo());
        params.put("itemName", order.getItemName());
        String content = templateService.replaceTemplateVariables(template, params);
        // 发送通知并记录
        sendNotification(order, "payment_success", content);
    }

    @Override
    public void sendShipmentNotification(Order order) {
        // 获取发货模板
        String template = templateService.getTemplate("shipment");
        // 替换模板变量
        Map<String, String> params = new HashMap<>();
        params.put("orderNo", order.getOrderNo());
        params.put("itemName", order.getItemName());
        String content = templateService.replaceTemplateVariables(template, params);
        // 发送通知并记录
        sendNotification(order, "shipment", content);
    }

    @Override
    public void sendReceiptNotification(Order order) {
        // 获取签收模板
        String template = templateService.getTemplate("receipt");
        // 替换模板变量
        Map<String, String> params = new HashMap<>();
        params.put("orderNo", order.getOrderNo());
        params.put("itemName", order.getItemName());
        String content = templateService.replaceTemplateVariables(template, params);
        // 发送通知并记录
        sendNotification(order, "receipt", content);
    }

    /**
     * 发送通知并记录
     */
    private void sendNotification(Order order, String type, String content) {
        // 获取用户通知偏好设置
        UserNotificationPreference preference = userNotificationPreferenceService.getUserPreference(order.getUserId());
        if (preference == null) {
            // 默认启用所有渠道
            preference = new UserNotificationPreference();
            preference.setEnableSms(1);
            preference.setEnableWeChat(1);
            preference.setEnableEmail(1);
        }

        // 发送短信通知
        if (preference.getEnableSms() == 1) {
            try {
                smsNotificationService.send(order.getUserId(), content);
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "sms", content, 1, null);
            } catch (Exception e) {
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "sms", content, 0, e.getMessage());
            }
        }

        // 发送微信通知
        if (preference.getEnableWeChat() == 1) {
            try {
                weChatNotificationService.send(order.getUserId(), content);
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "wechat", content, 1, null);
            } catch (Exception e) {
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "wechat", content, 0, e.getMessage());
            }
        }

        // 发送邮件通知
        if (preference.getEnableEmail() == 1) {
            try {
                emailNotificationService.send(order.getUserId(), content);
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "email", content, 1, null);
            } catch (Exception e) {
                notificationRecordService.recordNotification(order.getId(), order.getUserId(), type, "email", content, 0, e.getMessage());
            }
        }
    }
}