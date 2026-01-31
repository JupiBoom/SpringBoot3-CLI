package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修记录表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
@TableName("repair_record")
public class RepairRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单ID，关联报修工单表
     */
    private Long orderId;

    /**
     * 维修内容记录
     */
    private String repairContent;

    /**
     * 维修过程照片，JSON格式存储多个图片URL
     */
    private String repairImages;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

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