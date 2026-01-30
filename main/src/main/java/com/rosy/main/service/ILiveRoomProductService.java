package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.liveRoom.LiveRoomProductAddRequest;
import com.rosy.main.domain.dto.liveRoom.SwitchCurrentProductRequest;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.domain.vo.ProductSalesRankingVO;

import java.util.List;

/**
 * <p>
 * 直播间商品关联表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ILiveRoomProductService extends IService<LiveRoomProduct> {

    /**
     * 添加商品到直播间
     * @param liveRoomProductAddRequest 直播间商品添加请求
     * @return 是否成功
     */
    boolean addProductToLiveRoom(LiveRoomProductAddRequest liveRoomProductAddRequest);

    /**
     * 从直播间移除商品
     * @param id 记录ID
     * @return 是否成功
     */
    boolean removeProductFromLiveRoom(Long id);

    /**
     * 切换当前讲解商品
     * @param switchCurrentProductRequest 切换当前讲解商品请求
     * @return 是否成功
     */
    boolean switchCurrentProduct(SwitchCurrentProductRequest switchCurrentProductRequest);

    /**
     * 获取直播间商品列表
     * @param liveRoomId 直播间ID
     * @return 商品列表
     */
    List<LiveRoomProductVO> getProductsByLiveRoomId(Long liveRoomId);

    /**
     * 获取直播间商品视图对象
     * @param liveRoomProduct 直播间商品实体
     * @return 直播间商品视图对象
     */
    LiveRoomProductVO getLiveRoomProductVO(LiveRoomProduct liveRoomProduct);

    /**
     * 获取商品销售排行榜
     * @param liveRoomId 直播间ID
     * @param limit 限制数量
     * @return 商品销售排行榜
     */
    List<ProductSalesRankingVO> getProductSalesRanking(Long liveRoomId, Integer limit);
}