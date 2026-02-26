package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.entity.Product;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.mapper.LiveRoomProductMapper;
import com.rosy.main.service.ILiveRoomProductService;
import com.rosy.main.service.IProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LiveRoomProductServiceImpl extends ServiceImpl<LiveRoomProductMapper, LiveRoomProduct> implements ILiveRoomProductService {

    @Resource
    private IProductService productService;

    @Override
    public List<LiveRoomProductVO> getLiveRoomProducts(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<LiveRoomProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .orderByAsc(LiveRoomProduct::getSortOrder);

        List<LiveRoomProduct> liveRoomProducts = this.list(queryWrapper);
        List<LiveRoomProductVO> result = new ArrayList<>();

        for (LiveRoomProduct lrp : liveRoomProducts) {
            LiveRoomProductVO vo = new LiveRoomProductVO();
            vo.setId(lrp.getId());
            vo.setLiveRoomId(lrp.getLiveRoomId());
            vo.setProductId(lrp.getProductId());
            vo.setSortOrder(lrp.getSortOrder());
            vo.setIsExplaining(lrp.getIsExplaining());
            vo.setExplainStartTime(lrp.getExplainStartTime());

            Product product = productService.getById(lrp.getProductId());
            if (product != null) {
                vo.setProductName(product.getName());
                vo.setProductCoverUrl(product.getCoverUrl());
                vo.setProductPrice(product.getPrice());
            }

            result.add(vo);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProductToLiveRoom(Long liveRoomId, Long productId, Integer sortOrder) {
        if (liveRoomId == null || productId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<LiveRoomProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .eq(LiveRoomProduct::getProductId, productId);

        if (this.count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该商品已添加到直播间");
        }

        LiveRoomProduct liveRoomProduct = new LiveRoomProduct();
        liveRoomProduct.setLiveRoomId(liveRoomId);
        liveRoomProduct.setProductId(productId);
        liveRoomProduct.setSortOrder(sortOrder != null ? sortOrder : 0);
        liveRoomProduct.setIsExplaining((byte) 0);

        return this.save(liveRoomProduct);
    }

    @Override
    public boolean removeProductFromLiveRoom(Long liveRoomId, Long productId) {
        if (liveRoomId == null || productId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<LiveRoomProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .eq(LiveRoomProduct::getProductId, productId);

        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setExplainingProduct(Long liveRoomId, Long productId) {
        if (liveRoomId == null || productId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaUpdateWrapper<LiveRoomProduct> clearWrapper = new LambdaUpdateWrapper<>();
        clearWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .set(LiveRoomProduct::getIsExplaining, (byte) 0)
                .set(LiveRoomProduct::getExplainStartTime, null);
        this.update(clearWrapper);

        LambdaUpdateWrapper<LiveRoomProduct> setWrapper = new LambdaUpdateWrapper<>();
        setWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .eq(LiveRoomProduct::getProductId, productId)
                .set(LiveRoomProduct::getIsExplaining, (byte) 1)
                .set(LiveRoomProduct::getExplainStartTime, LocalDateTime.now());

        return this.update(setWrapper);
    }

    @Override
    public LiveRoomProductVO getCurrentExplainingProduct(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<LiveRoomProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId)
                .eq(LiveRoomProduct::getIsExplaining, (byte) 1);

        LiveRoomProduct liveRoomProduct = this.getOne(queryWrapper);
        if (liveRoomProduct == null) {
            return null;
        }

        LiveRoomProductVO vo = BeanUtil.copyProperties(liveRoomProduct, LiveRoomProductVO.class);
        Product product = productService.getById(liveRoomProduct.getProductId());
        if (product != null) {
            vo.setProductName(product.getName());
            vo.setProductCoverUrl(product.getCoverUrl());
            vo.setProductPrice(product.getPrice());
        }

        return vo;
    }
}
