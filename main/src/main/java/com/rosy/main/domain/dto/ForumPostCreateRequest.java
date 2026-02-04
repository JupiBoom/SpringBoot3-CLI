package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 论坛帖子创建请求DTO
 */
@Data
public class ForumPostCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联活动ID（可选）
     */
    private Long activityId;

    /**
     * 帖子标题
     */
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 200, message = "帖子标题长度不能超过200")
    private String title;

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    /**
     * 帖子类型：1-经验分享 2-活动回顾 3-问题咨询 4-其他
     */
    @NotNull(message = "帖子类型不能为空")
    private Integer type;

    /**
     * 图片列表
     */
    private List<String> images;
}
