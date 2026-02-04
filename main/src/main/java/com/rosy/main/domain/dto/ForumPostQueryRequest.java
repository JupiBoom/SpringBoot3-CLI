package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 论坛帖子查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ForumPostQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 发布者ID
     */
    private Long userId;

    /**
     * 帖子类型
     */
    private Integer type;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否精华
     */
    private Integer isEssence;

    /**
     * 状态
     */
    private Integer status;
}
