package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveData;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 直播间数据表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Mapper
public interface LiveDataMapper extends BaseMapper<LiveData> {

}
