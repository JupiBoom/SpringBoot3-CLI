package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
