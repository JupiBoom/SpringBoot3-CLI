package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.entity.LiveProductStats;
import com.rosy.main.domain.vo.LiveProductRankingVO;
import com.rosy.main.domain.vo.LiveProductVO;
import com.rosy.main.mapper.LiveProductMapper;
import com.rosy.main.mapper.LiveProductStatsMapper;
import com.rosy.main.service.ILiveProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class LiveProductServiceImpl extends ServiceImpl<LiveProductMapper, LiveProduct> implements ILiveProductService {

    private final LiveProductStatsMapper liveProductStatsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProduct(LiveProductAddRequest request) {
        LambdaQueryWrapper<LiveProduct> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(LiveProduct::getRoomId, request.getRoomId())
                .eq(LiveProduct::getProductId, request.getProductId());
        LiveProduct existing = this.getOne(checkWrapper);
        if (existing != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该商品已添加到直播间");
        }

        LiveProduct liveProduct = BeanUtil.copyProperties(request, LiveProduct.class);
        if (CollUtil.isNotEmpty(request.getSellingPoints())) {
            liveProduct.setSellingPoints(JSON.toJSONString(request.getSellingPoints()));
        }
        liveProduct.setIsExplaining((byte) 0);
        int result = baseMapper.insert(liveProduct);

        LiveProductStats stats = new LiveProductStats();
        stats.setRoomId(request.getRoomId());
        stats.setProductId(request.getProductId());
        stats.setProductName(request.getProductName());
        stats.setClickCount(0);
        stats.setOrderCount(0);
        stats.setSalesAmount(BigDecimal.ZERO);
        liveProductStatsMapper.insert(stats);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProduct(Long roomId, Long productId) {
        LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProduct::getRoomId, roomId)
                .eq(LiveProduct::getProductId, productId);
        int result = baseMapper.delete(wrapper);

        LambdaQueryWrapper<LiveProductStats> statsWrapper = new LambdaQueryWrapper<>();
        statsWrapper.eq(LiveProductStats::getRoomId, roomId)
                .eq(LiveProductStats::getProductId, productId);
        liveProductStatsMapper.delete(statsWrapper);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductSort(Long roomId, List<Long> productIds) {
        for (int i = 0; i < productIds.size(); i++) {
            LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LiveProduct::getRoomId, roomId)
                    .eq(LiveProduct::getProductId, productIds.get(i));
            LiveProduct product = this.getOne(wrapper);
            if (product != null) {
                product.setSortOrder(i);
                baseMapper.updateById(product);
            }
        }
        return true;
    }

    @Override
    public List<LiveProductVO> getProductList(Long roomId) {
        LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProduct::getRoomId, roomId)
                .orderByAsc(LiveProduct::getSortOrder);
        List<LiveProduct> products = this.list(wrapper);

        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public LiveProductVO getExplainingProduct(Long roomId) {
        LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProduct::getRoomId, roomId)
                .eq(LiveProduct::getIsExplaining, 1);
        LiveProduct product = this.getOne(wrapper);

        return Optional.ofNullable(product)
                .map(this::convertToVO)
                .orElse(null);
    }

    @Override
    public List<LiveProductRankingVO> getProductRanking(Long roomId) {
        LambdaQueryWrapper<LiveProductStats> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProductStats::getRoomId, roomId)
                .orderByDesc(LiveProductStats::getOrderCount, LiveProductStats::getSalesAmount);
        List<LiveProductStats> statsList = liveProductStatsMapper.selectList(wrapper);

        return statsList.stream()
                .map(stats -> {
                    LiveProductRankingVO vo = BeanUtil.copyProperties(stats, LiveProductRankingVO.class);
                    if (stats.getClickCount() > 0) {
                        BigDecimal conversionRate = BigDecimal.valueOf(stats.getOrderCount())
                                .divide(BigDecimal.valueOf(stats.getClickCount()), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));
                        vo.setConversionRate(conversionRate);
                    } else {
                        vo.setConversionRate(BigDecimal.ZERO);
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordProductClick(Long roomId, Long productId) {
        LambdaQueryWrapper<LiveProductStats> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProductStats::getRoomId, roomId)
                .eq(LiveProductStats::getProductId, productId);
        LiveProductStats stats = liveProductStatsMapper.selectOne(wrapper);
        if (stats != null) {
            stats.setClickCount(stats.getClickCount() + 1);
            liveProductStatsMapper.updateById(stats);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordProductOrder(Long roomId, Long productId, Integer quantity, BigDecimal amount) {
        LambdaQueryWrapper<LiveProductStats> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProductStats::getRoomId, roomId)
                .eq(LiveProductStats::getProductId, productId);
        LiveProductStats stats = liveProductStatsMapper.selectOne(wrapper);
        if (stats != null) {
            stats.setOrderCount(stats.getOrderCount() + quantity);
            stats.setSalesAmount(stats.getSalesAmount().add(amount));
            liveProductStatsMapper.updateById(stats);
            return true;
        }
        return false;
    }

    private LiveProductVO convertToVO(LiveProduct product) {
        LiveProductVO vo = BeanUtil.copyProperties(product, LiveProductVO.class);
        if (StrUtil.isNotBlank(product.getSellingPoints())) {
            vo.setSellingPoints(JSON.parseArray(product.getSellingPoints(), String.class));
        }
        return vo;
    }
}
