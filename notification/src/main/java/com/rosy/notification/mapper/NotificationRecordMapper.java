package com.rosy.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.notification.domain.entity.NotificationRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知记录Mapper接口
 */
@Mapper
public interface NotificationRecordMapper extends BaseMapper<NotificationRecord> {
}