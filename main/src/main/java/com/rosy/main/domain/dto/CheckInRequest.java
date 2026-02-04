package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 签到/签出请求DTO
 */
@Data
public class CheckInRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @NotNull(message = "报名ID不能为空")
    private Long registrationId;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 地点
     */
    @Size(max = 300, message = "地点长度不能超过300")
    private String location;
}
