package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新直播间商品请求
 */
@Data
public class LiveRoomItemUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @NotNull(message = "记录ID不能为空")
    private Long id;

    /**
     * 商品卖点列表
     */
    private List<String> sellingPoints;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 状态：0-下架，1-上架
     */
    private Byte status;
}
