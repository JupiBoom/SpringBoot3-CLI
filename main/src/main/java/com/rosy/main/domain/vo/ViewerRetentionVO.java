package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ViewerRetentionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<RetentionPointVO> retentionPoints;

    @Data
    public static class RetentionPointVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Integer minute;

        private Integer viewerCount;

        private Double retentionRate;
    }
}