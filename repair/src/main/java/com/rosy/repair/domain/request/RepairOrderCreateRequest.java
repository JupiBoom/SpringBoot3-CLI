package com.rosy.repair.domain.request;

import lombok.Data;
import java.util.List;

@Data
public class RepairOrderCreateRequest {

    private String deviceType;

    private String location;

    private String faultType;

    private String description;

    private List<String> images;

    public String getDeviceType() {
        return deviceType;
    }

    public String getLocation() {
        return location;
    }

    public String getFaultType() {
        return faultType;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }
}