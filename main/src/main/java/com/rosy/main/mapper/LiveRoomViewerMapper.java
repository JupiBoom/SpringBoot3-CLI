package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomViewer;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 直播间观众记录表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface LiveRoomViewerMapper extends BaseMapper<LiveRoomViewer> {

}