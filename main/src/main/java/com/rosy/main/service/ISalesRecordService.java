package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.SalesRecord;
import com.rosy.main.domain.vo.ProductRankingVO;

import java.math.BigDecimal;
import java.util.List;

public interface ISalesRecordService extends IService<SalesRecord> {

    boolean recordSale(Long liveRoomId, Long productId, String orderNo, Long userId, Integer quantity, BigDecimal unitPrice);

    List<ProductRankingVO> getProductRanking(Long liveRoomId, Integer limit);

    Integer getTotalOrders(Long liveRoomId);

    BigDecimal getTotalSales(Long liveRoomId);
}
