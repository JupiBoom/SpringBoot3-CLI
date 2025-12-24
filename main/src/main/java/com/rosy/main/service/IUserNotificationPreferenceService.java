package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.UserNotificationPreference;

/**
 * 用户通知偏好设置Service接口
 *
 * @author rosy
 */
public interface IUserNotificationPreferenceService extends IService<UserNotificationPreference> {

    /**
     * 获取用户通知偏好设置
     */
    UserNotificationPreference getUserPreference(Long userId);

    /**
     * 更新用户通知偏好设置
     */
    boolean updateUserPreference(UserNotificationPreference preference);
}