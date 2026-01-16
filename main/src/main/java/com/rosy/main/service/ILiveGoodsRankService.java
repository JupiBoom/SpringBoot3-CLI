package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveGoodsRank;
import com.rosy.main.domain.vo.LiveGoodsRankVO;

import java.util.List;

/**
 * <p>
 * 直播间商品排行榜 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveGoodsRankService extends IService<LiveGoodsRank> {

    /**
     * 更新排行榜
     *
     * @param roomId 直播间ID
     */
    void updateRank(Long roomId);

    /**
     * 获取排行榜
     *
     * @param roomId 直播间ID
     * @return 排行榜列表
     */
    List<LiveGoodsRankVO> getRankList(Long roomId);

    /**
     * 转换为VO
     *
     * @param liveGoodsRank 排行榜实体
     * @return 排行榜VO
     */
    LiveGoodsRankVO toVO(LiveGoodsRank liveGoodsRank);

    /**
     * 批量转换为VO
     *
     * @param rankList 排行榜实体列表
     * @return 排行榜VO列表
     */
    List<LiveGoodsRankVO> toVOList(List<LiveGoodsRank> rankList);
}
