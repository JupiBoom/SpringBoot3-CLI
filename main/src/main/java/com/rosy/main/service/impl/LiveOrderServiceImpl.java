package com.rosy.main.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveOrderAddRequest;
import com.rosy.main.domain.entity.LiveOrder;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveOrderVO;
import com.rosy.main.mapper.LiveOrderMapper;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveOrderService;
import com.rosy.main.service.ILiveRoomItemService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间订单表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Service
public class LiveOrderServiceImpl extends ServiceImpl<LiveOrderMapper, LiveOrder> implements ILiveOrderService {

    @Resource
    private LiveRoomMapper liveRoomMapper;

    @Lazy
    @Resource
    private ILiveRoomItemService liveRoomItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(LiveOrderAddRequest request) {
        // 验证直播间是否存在且正在直播
        LiveRoom liveRoom = liveRoomMapper.selectById(request.getRoomId());
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR, "直播间不存在");

        LiveOrder liveOrder = new LiveOrder();
        BeanUtils.copyProperties(request, liveOrder);

        // 生成订单号
        liveOrder.setOrderNo(IdUtil.getSnowflakeNextIdStr());

        // 计算订单总金额
        BigDecimal totalAmount = request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        liveOrder.setTotalAmount(totalAmount);

        // 设置初始状态为待支付
        liveOrder.setStatus((byte) 0);

        boolean result = this.save(liveOrder);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 更新直播间订单数
        liveRoomMapper.updateSalesData(request.getRoomId(), 1, BigDecimal.ZERO, request.getUserId());

        return liveOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long orderId) {
        LiveOrder liveOrder = this.getById(orderId);
        ThrowUtils.throwIf(liveOrder == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(liveOrder.getStatus() != 0, ErrorCode.OPERATION_ERROR, "订单状态不正确");

        liveOrder.setStatus((byte) 1);
        liveOrder.setPayTime(LocalDateTime.now());
        boolean result = this.updateById(liveOrder);

        if (result) {
            // 更新直播间销售数据
            liveRoomMapper.updateSalesData(
                    liveOrder.getRoomId(),
                    0,
                    liveOrder.getTotalAmount(),
                    liveOrder.getUserId()
            );
            
            // 更新商品销售数据
            liveRoomItemService.updateItemSalesData(
                    liveOrder.getRoomId(),
                    liveOrder.getItemId(),
                    liveOrder.getQuantity(),
                    liveOrder.getTotalAmount(),
                    liveOrder.getUserId()
            );
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId) {
        LiveOrder liveOrder = this.getById(orderId);
        ThrowUtils.throwIf(liveOrder == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(liveOrder.getStatus() != 0, ErrorCode.OPERATION_ERROR, "只能取消待支付订单");
        
        liveOrder.setStatus((byte) 4);
        return this.updateById(liveOrder);
    }

    @Override
    public LiveOrderVO getOrderById(Long orderId) {
        LiveOrder liveOrder = this.getById(orderId);
        if (liveOrder == null) {
            return null;
        }
        return convertToVO(liveOrder);
    }

    @Override
    public Page<LiveOrderVO> listOrdersByRoomId(Long roomId, int current, int size) {
        Page<LiveOrder> page = new Page<>(current, size);
        LambdaQueryWrapper<LiveOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveOrder::getRoomId, roomId)
                .orderByDesc(LiveOrder::getCreateTime);
        Page<LiveOrder> orderPage = this.page(page, queryWrapper);
        
        List<LiveOrderVO> voList = orderPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        Page<LiveOrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public BigDecimal getTotalSalesAmount(Long roomId) {
        return baseMapper.sumAmountByRoomId(roomId);
    }

    @Override
    public Long getTotalOrderCount(Long roomId) {
        return baseMapper.countByRoomId(roomId);
    }

    @Override
    public Long getPaidOrderCount(Long roomId) {
        return baseMapper.countPaidOrdersByRoomId(roomId);
    }

    @Override
    public List<Map<String, Object>> getItemSalesStats(Long roomId) {
        return baseMapper.selectItemSalesStats(roomId);
    }

    @Override
    public List<Map<String, Object>> getMinuteOrderStats(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if (liveRoom == null || liveRoom.getStartTime() == null) {
            return List.of();
        }

        LocalDateTime startTime = liveRoom.getStartTime();
        LocalDateTime endTime = liveRoom.getEndTime() != null ? liveRoom.getEndTime() : LocalDateTime.now();

        return baseMapper.selectMinuteOrderStats(roomId, startTime, endTime);
    }

    private LiveOrderVO convertToVO(LiveOrder liveOrder) {
        LiveOrderVO vo = new LiveOrderVO();
        BeanUtils.copyProperties(liveOrder, vo);

        // 设置状态文本
        switch (liveOrder.getStatus()) {
            case 0:
                vo.setStatusText("待支付");
                break;
            case 1:
                vo.setStatusText("已支付");
                break;
            case 2:
                vo.setStatusText("已发货");
                break;
            case 3:
                vo.setStatusText("已完成");
                break;
            case 4:
                vo.setStatusText("已取消");
                break;
            default:
                vo.setStatusText("未知");
        }

        // 加载直播间标题
        LiveRoom liveRoom = liveRoomMapper.selectById(liveOrder.getRoomId());
        if (liveRoom != null) {
            vo.setRoomTitle(liveRoom.getTitle());
        }

        return vo;
    }
}
