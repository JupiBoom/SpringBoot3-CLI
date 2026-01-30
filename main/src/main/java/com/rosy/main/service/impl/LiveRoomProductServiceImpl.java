package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveRoom.LiveRoomProductAddRequest;
import com.rosy.main.domain.dto.liveRoom.SwitchCurrentProductRequest;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.domain.vo.ProductSalesRankingVO;
import com.rosy.main.mapper.LiveRoomProductMapper;
import com.rosy.main.service.ILiveRoomProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间商品关联表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class LiveRoomProductServiceImpl extends ServiceImpl<LiveRoomProductMapper, LiveRoomProduct> implements ILiveRoomProductService {

    @Override
    public boolean addProductToLiveRoom(LiveRoomProductAddRequest liveRoomProductAddRequest) {
        LiveRoomProduct liveRoomProduct = new LiveRoomProduct();
        BeanUtil.copyProperties(liveRoomProductAddRequest, liveRoomProduct);
        liveRoomProduct.setIsCurrent((byte) 0); // 默认不是当前讲解商品
        liveRoomProduct.setSalesCount(0); // 默认销售数量为0
        return this.save(liveRoomProduct);
    }

    @Override
    public boolean removeProductFromLiveRoom(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean switchCurrentProduct(SwitchCurrentProductRequest switchCurrentProductRequest) {
        Long liveRoomId = switchCurrentProductRequest.getLiveRoomId();
        Long productId = switchCurrentProductRequest.getProductId();
        
        // 先将所有商品设置为非当前讲解商品
        UpdateWrapper<LiveRoomProduct> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("live_room_id", liveRoomId).set("is_current", 0);
        this.update(updateWrapper);
        
        // 设置指定商品为当前讲解商品
        UpdateWrapper<LiveRoomProduct> currentUpdateWrapper = new UpdateWrapper<>();
        currentUpdateWrapper.eq("live_room_id", liveRoomId)
                .eq("product_id", productId)
                .set("is_current", 1);
        boolean result = this.update(currentUpdateWrapper);
        
        // 更新直播间的当前商品ID
        if (result) {
            // TODO: 更新LiveRoom表的current_product_id字段
        }
        
        return result;
    }

    @Override
    public List<LiveRoomProductVO> getProductsByLiveRoomId(Long liveRoomId) {
        QueryWrapper<LiveRoomProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .orderByAsc("display_order");
        List<LiveRoomProduct> liveRoomProducts = this.list(queryWrapper);
        
        return liveRoomProducts.stream()
                .map(this::getLiveRoomProductVO)
                .collect(Collectors.toList());
    }

    @Override
    public LiveRoomProductVO getLiveRoomProductVO(LiveRoomProduct liveRoomProduct) {
        if (liveRoomProduct == null) {
            return null;
        }
        LiveRoomProductVO liveRoomProductVO = new LiveRoomProductVO();
        BeanUtil.copyProperties(liveRoomProduct, liveRoomProductVO);
        return liveRoomProductVO;
    }

    @Override
    public List<ProductSalesRankingVO> getProductSalesRanking(Long liveRoomId, Integer limit) {
        QueryWrapper<LiveRoomProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .gt("sales_count", 0)
                .orderByDesc("sales_count");
        if (limit != null && limit > 0) {
            queryWrapper.last("LIMIT " + limit);
        }
        
        List<LiveRoomProduct> liveRoomProducts = this.list(queryWrapper);
        
        return liveRoomProducts.stream()
                .map(product -> {
                    ProductSalesRankingVO rankingVO = new ProductSalesRankingVO();
                    rankingVO.setProductId(product.getProductId());
                    rankingVO.setProductName(product.getProductName());
                    rankingVO.setProductImage(product.getProductImage());
                    rankingVO.setProductPrice(product.getProductPrice());
                    rankingVO.setSalesCount(product.getSalesCount());
                    rankingVO.setTotalSalesAmount(product.getProductPrice()
                            .multiply(new java.math.BigDecimal(product.getSalesCount())));
                    rankingVO.setLiveRoomId(product.getLiveRoomId());
                    // TODO: 设置直播间标题
                    rankingVO.setStatsTime(java.time.LocalDateTime.now());
                    return rankingVO;
                })
                .collect(Collectors.toList());
    }
}