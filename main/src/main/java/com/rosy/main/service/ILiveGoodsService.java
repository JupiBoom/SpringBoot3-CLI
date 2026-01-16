package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveGoods;
import com.rosy.main.domain.vo.LiveGoodsAddVO;
import com.rosy.main.domain.vo.LiveGoodsBatchAddVO;
import com.rosy.main.domain.vo.LiveGoodsVO;

import java.util.List;

/**
 * <p>
 * 直播间商品表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveGoodsService extends IService<LiveGoods> {

    /**
     * 添加商品到直播间
     *
     * @param liveGoods 商品信息
     * @return 商品VO
     */
    LiveGoodsVO addGoodsToLive(LiveGoodsAddVO liveGoodsAddVO);

    /**
     * 批量添加商品到直播间
     *
     * @param liveRoomId 直播间ID
     * @param goodsList  商品列表
     * @return 商品VO列表
     */
    List<LiveGoodsVO> batchAddGoodsToLive(LiveGoodsBatchAddVO liveGoodsBatchAddVO);

    /**
     * 获取直播间商品列表
     *
     * @param liveRoomId 直播间ID
     * @return 商品VO列表
     */
    List<LiveGoodsVO> getLiveGoodsList(Long liveRoomId);

    /**
     * 更新商品卖点
     *
     * @param goodsId 商品ID
     * @param slogan  卖点
     * @return 商品VO
     */
    LiveGoodsVO updateGoodsSlogan(Long goodsId, String slogan);

    /**
     * 更新商品排序
     *
     * @param goodsId   商品ID
     * @param sortOrder 排序值
     * @return 商品VO
     */
    LiveGoodsVO updateGoodsSort(Long goodsId, Integer sortOrder);

    /**
     * 转换为VO
     *
     * @param liveGoods 商品实体
     * @return 商品VO
     */
    LiveGoodsVO toVO(LiveGoods liveGoods);

    /**
     * 批量转换为VO
     *
     * @param liveGoodsList 商品实体列表
     * @return 商品VO列表
     */
    List<LiveGoodsVO> toVOList(List<LiveGoods> liveGoodsList);
}
