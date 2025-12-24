package com.rosy.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.notification.domain.entity.UserNotificationPreference;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户通知偏好设置Mapper接口
 */
@Mapper
public interface UserNotificationPreferenceMapper extends BaseMapper<UserNotificationPreference> {
}