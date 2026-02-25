package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveOrderAddRequest;
import com.rosy.main.domain.entity.LiveOrder;
import com.rosy.main.domain.vo.LiveOrderVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间订单表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface ILiveOrderService extends IService<LiveOrder> {

    /**
     * 创建订单
     */
    Long createOrder(LiveOrderAddRequest request);

    /**
     * 支付订单
     */
    boolean payOrder(Long orderId);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 根据ID获取订单
     */
    LiveOrderVO getOrderById(Long orderId);

    /**
     * 分页查询直播间订单
     */
    Page<LiveOrderVO> listOrdersByRoomId(Long roomId, int current, int size);

    /**
     * 查询直播间订单总金额
     */
    BigDecimal getTotalSalesAmount(Long roomId);

    /**
     * 查询直播间订单总数
     */
    Long getTotalOrderCount(Long roomId);

    /**
     * 查询直播间已支付订单数
     */
    Long getPaidOrderCount(Long roomId);

    /**
     * 查询直播间各商品销售统计
     */
    List<Map<String, Object>> getItemSalesStats(Long roomId);

    /**
     * 查询直播间每分钟订单数据
     */
    List<Map<String, Object>> getMinuteOrderStats(Long roomId);
}
