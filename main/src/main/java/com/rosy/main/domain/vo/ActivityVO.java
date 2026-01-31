package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "活动VO")
public class ActivityVO {

    @Schema(description = "活动ID")
    private Long id;

    @Schema(description = "活动标题")
    private String title;

    @Schema(description = "活动描述")
    private String description;

    @Schema(description = "分类：1-环保 2-助老 3-教育 4-医疗")
    private Integer category;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "需求人数")
    private Integer requiredNum;

    @Schema(description = "已报名人数")
    private Integer registeredNum;

    @Schema(description = "状态：1-招募中 2-进行中 3-已完成")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "报名要求")
    private String requirements;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
