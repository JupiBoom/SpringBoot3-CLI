package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间商品关联表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Data
@TableName("live_room_item")
public class LiveRoomItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 直播间ID
     */
    private Long roomId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 商品名称（冗余）
     */
    private String itemName;

    /**
     * 商品售价（冗余）
     */
    private BigDecimal itemPrice;

    /**
     * 商品主图（冗余）
     */
    private String itemImage;

    /**
     * 商品卖点，JSON数组格式
     */
    private String sellingPoints;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 状态：0-下架，1-上架
     */
    private Byte status;

    /**
     * 直播期间销量
     */
    private Integer salesCount;

    /**
     * 直播期间销售额
     */
    private BigDecimal salesAmount;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
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
