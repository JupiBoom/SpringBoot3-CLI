package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveGoods;
import com.rosy.main.domain.entity.LiveGoodsRank;
import com.rosy.main.domain.vo.LiveGoodsRankVO;
import com.rosy.main.mapper.LiveGoodsRankMapper;
import com.rosy.main.service.ILiveGoodsRankService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间商品排行榜 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveGoodsRankServiceImpl extends ServiceImpl<LiveGoodsRankMapper, LiveGoodsRank> implements ILiveGoodsRankService {

    private final com.rosy.main.service.ILiveGoodsService liveGoodsService;

    public LiveGoodsRankServiceImpl(com.rosy.main.service.ILiveGoodsService liveGoodsService) {
        this.liveGoodsService = liveGoodsService;
    }

    @Override
    @Transactional
    public void updateRank(Long roomId) {
        // 先删除旧的排行榜
        LambdaQueryWrapper<LiveGoodsRank> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(LiveGoodsRank::getLiveRoomId, roomId);
        this.remove(deleteWrapper);

        // 获取商品列表并排序
        LambdaQueryWrapper<LiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveGoods::getLiveRoomId, roomId)
                .orderByDesc(LiveGoods::getSellCount);
        List<LiveGoods> goodsList = liveGoodsService.list(queryWrapper);

        // 生成新的排行榜
        List<LiveGoodsRank> rankList = goodsList.stream()
                .map(goods -> {
                    LiveGoodsRank rank = new LiveGoodsRank();
                    rank.setLiveRoomId(roomId);
                    rank.setGoodsId(goods.getGoodsId());
                    rank.setGoodsName(goods.getGoodsName());
                    rank.setSellCount(goods.getSellCount());
                    rank.setSalesAmount(goods.getPrice().multiply(new java.math.BigDecimal(goods.getSellCount())));
                    rank.setCreateTime(LocalDateTime.now());
                    rank.setUpdateTime(LocalDateTime.now());
                    return rank;
                })
                .collect(Collectors.toList());

        // 设置排名
        for (int i = 0; i < rankList.size(); i++) {
            rankList.get(i).setRank(i + 1);
        }

        this.saveBatch(rankList);
    }

    @Override
    public List<LiveGoodsRankVO> getRankList(Long roomId) {
        LambdaQueryWrapper<LiveGoodsRank> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveGoodsRank::getLiveRoomId, roomId)
                .orderByAsc(LiveGoodsRank::getRank);
        List<LiveGoodsRank> rankList = this.list(queryWrapper);
        return this.toVOList(rankList);
    }

    @Override
    public LiveGoodsRankVO toVO(LiveGoodsRank liveGoodsRank) {
        return BeanUtil.copyProperties(liveGoodsRank, LiveGoodsRankVO.class);
    }

    @Override
    public List<LiveGoodsRankVO> toVOList(List<LiveGoodsRank> rankList) {
        return rankList.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }
}
