package com.rosy.repair.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairOrderVO {

    private Long id;

    private String orderNo;

    private Long userId;

    private String deviceType;

    private String location;

    private String faultType;

    private String description;

    private List<String> images;

    private Integer status;

    private Integer priority;

    private Long repairerId;

    private String repairerName;

    private Integer assignType;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private String repairRecord;

    private String result;

    private Integer star;

    private String evaluation;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setRepairerName(String repairerName) {
        this.repairerName = repairerName;
    }
}
