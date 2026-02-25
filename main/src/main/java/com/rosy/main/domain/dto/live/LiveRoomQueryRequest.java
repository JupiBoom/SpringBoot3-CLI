package com.rosy.main.domain.dto.live;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 直播间查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveRoomQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long id;

    /**
     * 直播间标题（模糊搜索）
     */
    private String title;

    /**
     * 主播ID
     */
    private Long streamerId;

    /**
     * 直播状态：0-未开始，1-直播中，2-已结束，3-已禁播
     */
    private Byte status;

    /**
     * 搜索关键词（标题/主播名称）
     */
    private String keyword;
}
