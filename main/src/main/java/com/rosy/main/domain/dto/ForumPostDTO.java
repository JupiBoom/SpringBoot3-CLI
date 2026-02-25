package com.rosy.main.domain.dto;

import lombok.Data;

@Data
public class ForumPostDTO {
    private Long activityId;
    private String title;
    private String content;
}
