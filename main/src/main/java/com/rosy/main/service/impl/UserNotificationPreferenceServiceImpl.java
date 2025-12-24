package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.UserNotificationPreference;
import com.rosy.main.mapper.UserNotificationPreferenceMapper;
import com.rosy.main.service.IUserNotificationPreferenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户通知偏好设置Service实现类
 *
 * @author rosy
 */
@Service
public class UserNotificationPreferenceServiceImpl extends ServiceImpl<UserNotificationPreferenceMapper, UserNotificationPreference> implements IUserNotificationPreferenceService {

    @Override
    public UserNotificationPreference getUserPreference(Long userId) {
        return lambdaQuery()
                .eq(UserNotificationPreference::getUserId, userId)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPreference(UserNotificationPreference preference) {
        return updateById(preference);
    }
}