package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修评价VO
 */
@Data
public class RepairEvaluationVO {

    private Long id;

    private Long orderId;

    private Long userId;

    private String userName;

    private Long repairmanId;

    private String repairmanName;

    private Integer rating;

    private String ratingDesc;

    private String content;

    private List<String> images;

    private Integer responseRating;

    private Integer serviceRating;

    private Integer qualityRating;

    private LocalDateTime createTime;
}
