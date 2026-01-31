package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报修工单添加请求
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class RepairOrderAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 故障类型ID
     */
    private Long faultTypeId;

    /**
     * 设备位置
     */
    private String deviceLocation;

    /**
     * 故障描述
     */
    private String faultDescription;

    /**
     * 故障照片URL列表
     */
    private List<String> faultImages;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;
}