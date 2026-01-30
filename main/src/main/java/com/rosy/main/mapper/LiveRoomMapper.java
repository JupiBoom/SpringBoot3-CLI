package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoom;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 直播间表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {

}