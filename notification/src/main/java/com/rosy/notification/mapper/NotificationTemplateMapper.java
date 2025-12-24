package com.rosy.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.notification.domain.entity.NotificationTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知模板Mapper接口
 */
@Mapper
public interface NotificationTemplateMapper extends BaseMapper<NotificationTemplate> {
}