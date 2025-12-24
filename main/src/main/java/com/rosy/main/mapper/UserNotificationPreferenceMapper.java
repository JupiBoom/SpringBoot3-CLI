package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.UserNotificationPreference;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户通知偏好设置Mapper接口
 *
 * @author rosy
 */
@Mapper
public interface UserNotificationPreferenceMapper extends BaseMapper<UserNotificationPreference> {
}