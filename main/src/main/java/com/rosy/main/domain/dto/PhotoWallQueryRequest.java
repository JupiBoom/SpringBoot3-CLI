package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 照片墙查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhotoWallQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 上传者ID
     */
    private Long userId;

    /**
     * 是否精选
     */
    private Integer isFeatured;

    /**
     * 状态
     */
    private Integer status;
}
