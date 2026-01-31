package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通知表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

}