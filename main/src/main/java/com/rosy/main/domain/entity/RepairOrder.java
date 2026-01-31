package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备报修工单表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单号，唯一标识
     */
    private String orderNo;

    /**
     * 设备类型ID，关联设备类型表
     */
    private Long deviceTypeId;

    /**
     * 故障类型ID，关联故障类型表
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
     * 故障照片，JSON格式存储多个图片URL
     */
    private String faultImages;

    /**
     * 报修状态：0-待处理，1-维修中，2-已完成
     */
    private Byte status;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 分配给的维修人员ID
     */
    private Long assignedTo;

    /**
     * 分配时间
     */
    private LocalDateTime assignedTime;

    /**
     * 接单时间
     */
    private LocalDateTime acceptedTime;

    /**
     * 完成时间
     */
    private LocalDateTime completedTime;

    /**
     * 创建者ID，关联用户表
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID，关联用户表
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}