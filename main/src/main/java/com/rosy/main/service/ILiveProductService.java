package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.vo.LiveProductRankingVO;
import com.rosy.main.domain.vo.LiveProductVO;

import java.util.List;

public interface ILiveProductService extends IService<LiveProduct> {

    boolean addProduct(LiveProductAddRequest request);

    boolean removeProduct(Long roomId, Long productId);

    boolean updateProductSort(Long roomId, List<Long> productIds);

    List<LiveProductVO> getProductList(Long roomId);

    LiveProductVO getExplainingProduct(Long roomId);

    List<LiveProductRankingVO> getProductRanking(Long roomId);

    boolean recordProductClick(Long roomId, Long productId);

    boolean recordProductOrder(Long roomId, Long productId, Integer quantity, java.math.BigDecimal amount);
}
