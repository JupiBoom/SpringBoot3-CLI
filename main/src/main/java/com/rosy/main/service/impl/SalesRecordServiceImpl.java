package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.SalesRecord;
import com.rosy.main.domain.vo.ProductRankingVO;
import com.rosy.main.mapper.SalesRecordMapper;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ISalesRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesRecordServiceImpl extends ServiceImpl<SalesRecordMapper, SalesRecord> implements ISalesRecordService {

    @Resource
    private ILiveRoomService liveRoomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordSale(Long liveRoomId, Long productId, String orderNo, Long userId, Integer quantity, BigDecimal unitPrice) {
        if (liveRoomId == null || productId == null || orderNo == null || userId == null || quantity == null || unitPrice == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setLiveRoomId(liveRoomId);
        salesRecord.setProductId(productId);
        salesRecord.setOrderNo(orderNo);
        salesRecord.setUserId(userId);
        salesRecord.setQuantity(quantity);
        salesRecord.setUnitPrice(unitPrice);
        salesRecord.setTotalAmount(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        salesRecord.setCreateTime(LocalDateTime.now());

        boolean saved = this.save(salesRecord);
        if (saved) {
            updateLiveRoomStats(liveRoomId, salesRecord.getTotalAmount());
        }

        return saved;
    }

    private void updateLiveRoomStats(Long liveRoomId, BigDecimal amount) {
        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom != null) {
            liveRoom.setTotalOrders(liveRoom.getTotalOrders() + 1);
            liveRoom.setTotalSales(liveRoom.getTotalSales().add(amount));
            liveRoomService.updateById(liveRoom);
        }
    }

    @Override
    public List<ProductRankingVO> getProductRanking(Long liveRoomId, Integer limit) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<SalesRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesRecord::getLiveRoomId, liveRoomId);

        List<SalesRecord> records = this.list(queryWrapper);

        Map<Long, List<SalesRecord>> groupedByProduct = records.stream()
                .collect(Collectors.groupingBy(SalesRecord::getProductId));

        List<ProductRankingVO> rankings = new ArrayList<>();
        for (Map.Entry<Long, List<SalesRecord>> entry : groupedByProduct.entrySet()) {
            ProductRankingVO vo = new ProductRankingVO();
            vo.setProductId(entry.getKey());

            List<SalesRecord> productRecords = entry.getValue();
            vo.setTotalSold(productRecords.stream().mapToInt(SalesRecord::getQuantity).sum());
            vo.setTotalSales(productRecords.stream()
                    .map(SalesRecord::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            vo.setOrderCount(productRecords.size());

            rankings.add(vo);
        }

        rankings.sort((a, b) -> b.getTotalSales().compareTo(a.getTotalSales()));

        int rank = 1;
        for (ProductRankingVO vo : rankings) {
            vo.setRank(rank++);
        }

        if (limit != null && limit > 0 && rankings.size() > limit) {
            rankings = rankings.subList(0, limit);
        }

        return rankings;
    }

    @Override
    public Integer getTotalOrders(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<SalesRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesRecord::getLiveRoomId, liveRoomId);

        return (int) this.count(queryWrapper);
    }

    @Override
    public BigDecimal getTotalSales(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<SalesRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesRecord::getLiveRoomId, liveRoomId);

        List<SalesRecord> records = this.list(queryWrapper);
        return records.stream()
                .map(SalesRecord::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
