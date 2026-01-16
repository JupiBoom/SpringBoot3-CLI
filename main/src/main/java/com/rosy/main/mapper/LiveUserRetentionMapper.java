package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveUserRetention;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 观众留存记录表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Mapper
public interface LiveUserRetentionMapper extends BaseMapper<LiveUserRetention> {

}
