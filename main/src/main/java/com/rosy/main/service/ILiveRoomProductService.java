package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;

import java.util.List;

public interface ILiveRoomProductService extends IService<LiveRoomProduct> {

    List<LiveRoomProductVO> getLiveRoomProducts(Long liveRoomId);

    boolean addProductToLiveRoom(Long liveRoomId, Long productId, Integer sortOrder);

    boolean removeProductFromLiveRoom(Long liveRoomId, Long productId);

    boolean setExplainingProduct(Long liveRoomId, Long productId);

    LiveRoomProductVO getCurrentExplainingProduct(Long liveRoomId);
}
