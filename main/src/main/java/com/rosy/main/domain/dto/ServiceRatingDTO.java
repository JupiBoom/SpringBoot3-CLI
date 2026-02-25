package com.rosy.main.domain.dto;

import lombok.Data;

@Data
public class ServiceRatingDTO {
    private Long serviceRecordId;
    private Integer rating;
    private String comment;
}
