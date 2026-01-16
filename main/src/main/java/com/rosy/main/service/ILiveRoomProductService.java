package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;

import java.math.BigDecimal;
import java.util.List;

public interface ILiveRoomProductService extends IService<LiveRoomProduct> {

    LiveRoomProductVO addProduct(LiveRoomProduct liveRoomProduct);

    boolean removeProduct(Long id);

    LiveRoomProductVO updateProduct(LiveRoomProduct liveRoomProduct);

    List<LiveRoomProductVO> getProductsByLiveRoomId(Long liveRoomId);

    boolean switchCurrentProduct(Long liveRoomId, Long productId);

    LiveRoomProductVO getLiveRoomProductVO(LiveRoomProduct liveRoomProduct);

    boolean updateProductSales(Long productId, Integer orderCount, BigDecimal salesAmount);
}