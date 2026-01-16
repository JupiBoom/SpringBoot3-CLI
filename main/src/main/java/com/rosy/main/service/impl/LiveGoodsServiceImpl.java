package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveGoods;
import com.rosy.main.domain.vo.LiveGoodsVO;
import com.rosy.main.domain.vo.LiveGoodsAddVO;
import com.rosy.main.domain.vo.LiveGoodsBatchAddVO;
import com.rosy.main.mapper.LiveGoodsMapper;
import com.rosy.main.service.ILiveGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
public class LiveGoodsServiceImpl extends ServiceImpl<LiveGoodsMapper, LiveGoods> implements ILiveGoodsService {

    @Override
    @Transactional
    public LiveGoodsVO addGoodsToLive(LiveGoodsAddVO liveGoodsAddVO) {
        LiveGoods liveGoods = BeanUtil.copyProperties(liveGoodsAddVO, LiveGoods.class);
        liveGoods.setCreateTime(LocalDateTime.now());
        liveGoods.setUpdateTime(LocalDateTime.now());
        this.save(liveGoods);
        return this.toVO(liveGoods);
    }

    @Override
    @Transactional
    public List<LiveGoodsVO> batchAddGoodsToLive(LiveGoodsBatchAddVO liveGoodsBatchAddVO) {
        List<LiveGoods> goodsList = liveGoodsBatchAddVO.getGoodsList().stream()
                .map(vo -> {
                    LiveGoods liveGoods = BeanUtil.copyProperties(vo, LiveGoods.class);
                    liveGoods.setLiveRoomId(liveGoodsBatchAddVO.getLiveRoomId());
                    liveGoods.setCreateTime(LocalDateTime.now());
                    liveGoods.setUpdateTime(LocalDateTime.now());
                    return liveGoods;
                })
                .collect(Collectors.toList());
        this.saveBatch(goodsList);
        return this.toVOList(goodsList);
    }

    @Override
    public List<LiveGoodsVO> getLiveGoodsList(Long liveRoomId) {
        LambdaQueryWrapper<LiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveGoods::getLiveRoomId, liveRoomId)
                .eq(LiveGoods::getStatus, 1)
                .orderByAsc(LiveGoods::getSortOrder);
        List<LiveGoods> goodsList = this.list(queryWrapper);
        return this.toVOList(goodsList);
    }

    @Override
    @Transactional
    public LiveGoodsVO updateGoodsSlogan(Long goodsId, String slogan) {
        LiveGoods liveGoods = this.getById(goodsId);
        if (liveGoods == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品不存在");
        }
        liveGoods.setSlogan(slogan);
        liveGoods.setUpdateTime(LocalDateTime.now());
        this.updateById(liveGoods);
        return this.toVO(liveGoods);
    }

    @Override
    @Transactional
    public LiveGoodsVO updateGoodsSort(Long goodsId, Integer sortOrder) {
        LiveGoods liveGoods = this.getById(goodsId);
        if (liveGoods == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品不存在");
        }
        liveGoods.setSortOrder(sortOrder);
        liveGoods.setUpdateTime(LocalDateTime.now());
        this.updateById(liveGoods);
        return this.toVO(liveGoods);
    }

    @Override
    public LiveGoodsVO toVO(LiveGoods liveGoods) {
        return BeanUtil.copyProperties(liveGoods, LiveGoodsVO.class);
    }

    @Override
    public List<LiveGoodsVO> toVOList(List<LiveGoods> liveGoodsList) {
        return liveGoodsList.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }
}
