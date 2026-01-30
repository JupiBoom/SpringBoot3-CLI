package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议室VO类
 */
@Data
public class MeetingRoomVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室位置
     */
    private String location;

    /**
     * 会议室容量
     */
    private Integer capacity;

    /**
     * 设备信息
     */
    private String equipment;

    /**
     * 会议室描述
     */
    private String description;

    /**
     * 状态：0-停用，1-可用
     */
    private Integer status;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}