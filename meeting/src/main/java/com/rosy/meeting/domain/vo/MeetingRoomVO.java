package com.rosy.meeting.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议室VO
 */
@Data
public class MeetingRoomVO {

    /**
     * 会议室ID
     */
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 位置
     */
    private String location;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 设备列表
     */
    private List<String> equipment;

    /**
     * 状态：0-可用，1-不可用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}