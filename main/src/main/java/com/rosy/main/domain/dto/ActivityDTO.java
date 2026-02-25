package com.rosy.main.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityDTO {
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer needPeople;
    private String contactName;
    private String contactPhone;
    private String requirements;
}
