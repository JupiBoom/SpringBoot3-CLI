package com.rosy.main.domain.vo;

import lombok.Data;

import java.util.Map;

@Data
public class StatisticsVO {
    private Long totalCount;
    private Map<Long, Integer> detail;
}
