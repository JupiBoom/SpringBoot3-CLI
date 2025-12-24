package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.NotificationRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知记录Mapper接口
 *
 * @author rosy
 */
@Mapper
public interface NotificationRecordMapper extends BaseMapper<NotificationRecord> {
}