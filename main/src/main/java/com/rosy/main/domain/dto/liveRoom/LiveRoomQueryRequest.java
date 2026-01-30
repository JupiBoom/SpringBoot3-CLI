package com.rosy.main.domain.dto.liveRoom;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间查询请求
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LiveRoomQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 直播间标题
     */
    private String title;

    /**
     * 直播状态：0-未开始，1-直播中，2-已结束
     */
    private Byte status;

    /**
     * 主播ID
     */
    private Long hostId;

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序
     */
    private String sortOrder;
}