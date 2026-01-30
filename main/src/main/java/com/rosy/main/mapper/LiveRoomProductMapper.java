package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 直播间商品关联表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Mapper
public interface LiveRoomProductMapper extends BaseMapper<LiveRoomProduct> {

}