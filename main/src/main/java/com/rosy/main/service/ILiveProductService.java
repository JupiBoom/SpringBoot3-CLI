package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.vo.LiveProductVO;

import java.util.List;

/**
 * <p>
 * 直播间商品表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveProductService extends IService<LiveProduct> {

    LiveProductVO getLiveProductVO(LiveProduct liveProduct);

    LambdaQueryWrapper<LiveProduct> getQueryWrapper(Long liveRoomId);

    LiveProductVO addProductToLive(LiveProductAddRequest request);

    boolean removeProductFromLive(Long liveProductId);

    boolean updateProductStatus(Long liveProductId, Byte status);

    List<LiveProductVO> listLiveProducts(Long liveRoomId);

    boolean updateProductSortOrder(Long liveProductId, Integer sortOrder);
}