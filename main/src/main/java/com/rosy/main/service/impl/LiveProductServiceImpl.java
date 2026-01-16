package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.vo.LiveProductVO;
import com.rosy.main.mapper.LiveProductMapper;
import com.rosy.main.service.ILiveProductService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间商品表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveProductServiceImpl extends ServiceImpl<LiveProductMapper, LiveProduct> implements ILiveProductService {

    private final Gson gson = new Gson();

    @Override
    public LiveProductVO getLiveProductVO(LiveProduct liveProduct) {
        if (liveProduct == null) {
            return null;
        }
        LiveProductVO vo = BeanUtil.copyProperties(liveProduct, LiveProductVO.class);
        
        if (liveProduct.getSellingPoints() != null && !liveProduct.getSellingPoints().isEmpty()) {
            List<String> points = gson.fromJson(liveProduct.getSellingPoints(), new com.google.gson.reflect.TypeToken<List<String>>() {}.getType());
            vo.setSellingPoints(points);
        }
        
        String statusText = switch (liveProduct.getStatus()) {
            case 0 -> "未上架";
            case 1 -> "已上架";
            case 2 -> "已下架";
            default -> "未知";
        };
        vo.setStatusText(statusText);
        
        return vo;
    }

    @Override
    public LambdaQueryWrapper<LiveProduct> getQueryWrapper(Long liveRoomId) {
        LambdaQueryWrapper<LiveProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveProduct::getLiveRoomId, liveRoomId);
        queryWrapper.orderByAsc(LiveProduct::getSortOrder);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveProductVO addProductToLive(LiveProductAddRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        
        LiveProduct liveProduct = BeanUtil.copyProperties(request, LiveProduct.class);
        
        if (request.getSellingPoints() != null && !request.getSellingPoints().isEmpty()) {
            liveProduct.setSellingPoints(gson.toJson(request.getSellingPoints()));
        }
        
        if (liveProduct.getSortOrder() == null) {
            liveProduct.setSortOrder(0);
        }
        liveProduct.setStatus((byte) 1);
        liveProduct.setSoldCount(0);
        
        if (!this.save(liveProduct)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加商品失败");
        }
        
        return getLiveProductVO(liveProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProductFromLive(Long liveProductId) {
        if (liveProductId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品ID不能为空");
        }
        
        LiveProduct liveProduct = this.getById(liveProductId);
        if (liveProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        
        return this.removeById(liveProductId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStatus(Long liveProductId, Byte status) {
        if (liveProductId == null || status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        
        LiveProduct liveProduct = this.getById(liveProductId);
        if (liveProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        
        liveProduct.setStatus(status);
        
        return this.updateById(liveProduct);
    }

    @Override
    public List<LiveProductVO> listLiveProducts(Long liveRoomId) {
        LambdaQueryWrapper<LiveProduct> queryWrapper = getQueryWrapper(liveRoomId);
        List<LiveProduct> products = this.list(queryWrapper);
        
        return products.stream()
                .map(this::getLiveProductVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductSortOrder(Long liveProductId, Integer sortOrder) {
        if (liveProductId == null || sortOrder == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        
        LiveProduct liveProduct = this.getById(liveProductId);
        if (liveProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        
        liveProduct.setSortOrder(sortOrder);
        
        return this.updateById(liveProduct);
    }
}