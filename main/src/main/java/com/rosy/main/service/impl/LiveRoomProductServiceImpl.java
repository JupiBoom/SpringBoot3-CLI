package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.mapper.LiveRoomProductMapper;
import com.rosy.main.service.ILiveRoomProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomProductServiceImpl extends ServiceImpl<LiveRoomProductMapper, LiveRoomProduct> implements ILiveRoomProductService {

    @Override
    public LiveRoomProductVO addProduct(LiveRoomProduct liveRoomProduct) {
        liveRoomProduct.setStatus((byte) 1);
        liveRoomProduct.setCreateTime(LocalDateTime.now());
        liveRoomProduct.setUpdateTime(LocalDateTime.now());
        boolean saved = save(liveRoomProduct);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加商品失败");
        }
        return getLiveRoomProductVO(liveRoomProduct);
    }

    @Override
    public boolean removeProduct(Long id) {
        LiveRoomProduct product = getById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        return removeById(id);
    }

    @Override
    public LiveRoomProductVO updateProduct(LiveRoomProduct liveRoomProduct) {
        LiveRoomProduct existProduct = getById(liveRoomProduct.getId());
        if (existProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        liveRoomProduct.setUpdateTime(LocalDateTime.now());
        boolean updated = updateById(liveRoomProduct);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新商品失败");
        }
        return getLiveRoomProductVO(liveRoomProduct);
    }

    @Override
    public List<LiveRoomProductVO> getProductsByLiveRoomId(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId);
        queryWrapper.orderByAsc(LiveRoomProduct::getSortOrder);
        List<LiveRoomProduct> products = list(queryWrapper);
        return products.stream().map(this::getLiveRoomProductVO).collect(Collectors.toList());
    }

    @Override
    public boolean switchCurrentProduct(Long liveRoomId, Long productId) {
        LambdaUpdateWrapper<LiveRoomProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LiveRoomProduct::getLiveRoomId, liveRoomId);
        updateWrapper.set(LiveRoomProduct::getStatus, (byte) 1);
        update(updateWrapper);

        LambdaUpdateWrapper<LiveRoomProduct> currentWrapper = new LambdaUpdateWrapper<>();
        currentWrapper.eq(LiveRoomProduct::getId, productId);
        currentWrapper.set(LiveRoomProduct::getStatus, (byte) 2);
        return update(currentWrapper);
    }

    @Override
    public LiveRoomProductVO getLiveRoomProductVO(LiveRoomProduct liveRoomProduct) {
        if (liveRoomProduct == null) {
            return null;
        }
        LiveRoomProductVO vo = BeanUtil.copyProperties(liveRoomProduct, LiveRoomProductVO.class);
        vo.setStatusText(getStatusText(liveRoomProduct.getStatus()));
        return vo;
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "未上架";
            case 1:
                return "已上架";
            case 2:
                return "讲解中";
            default:
                return "未知";
        }
    }

    @Override
    public boolean updateProductSales(Long productId, Integer orderCount, BigDecimal salesAmount) {
        LiveRoomProduct product = getById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        LambdaUpdateWrapper<LiveRoomProduct> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LiveRoomProduct::getId, productId);
        updateWrapper.set(LiveRoomProduct::getOrderCount, orderCount);
        updateWrapper.set(LiveRoomProduct::getSalesAmount, salesAmount);
        updateWrapper.set(LiveRoomProduct::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }
}