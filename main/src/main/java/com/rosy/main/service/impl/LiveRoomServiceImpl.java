package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.LiveRoomStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveAudienceRetention;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.entity.LiveProductRank;
import com.rosy.main.domain.entity.LiveProductSellPoint;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveStatistics;
import com.rosy.main.domain.vo.LiveAudienceRetentionVO;
import com.rosy.main.domain.vo.LiveProductRankVO;
import com.rosy.main.domain.vo.LiveProductVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatisticsVO;
import com.rosy.main.mapper.LiveAudienceRetentionMapper;
import com.rosy.main.mapper.LiveProductMapper;
import com.rosy.main.mapper.LiveProductRankMapper;
import com.rosy.main.mapper.LiveProductSellPointMapper;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.mapper.LiveStatisticsMapper;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Resource
    private LiveProductMapper liveProductMapper;

    @Resource
    private LiveProductSellPointMapper liveProductSellPointMapper;

    @Resource
    private LiveStatisticsMapper liveStatisticsMapper;

    @Resource
    private LiveAudienceRetentionMapper liveAudienceRetentionMapper;

    @Resource
    private LiveProductRankMapper liveProductRankMapper;

    @Lazy
    @Resource
    private ILiveRoomService liveRoomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRoom(LiveRoomAddRequest request) {
        LiveRoom liveRoom = BeanUtil.copyProperties(request, LiveRoom.class);
        liveRoom.setRoomStatus(LiveRoomStatusEnum.NOT_STARTED.getValue());
        boolean result = this.save(liveRoom);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建直播间失败");
        }
        LiveStatistics statistics = new LiveStatistics();
        statistics.setRoomId(liveRoom.getId());
        statistics.setTotalOrderNum(0);
        statistics.setTotalSales(BigDecimal.ZERO);
        statistics.setTotalViewer(0);
        statistics.setPeakViewer(0);
        statistics.setAvgOnlineTime(0);
        liveStatisticsMapper.insert(statistics);
        return liveRoom.getId();
    }

    @Override
    public Boolean updateRoom(LiveRoomUpdateRequest request) {
        LiveRoom existLiveRoom = this.getById(request.getId());
        if (existLiveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (!LiveRoomStatusEnum.NOT_STARTED.getValue().equals(existLiveRoom.getRoomStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能修改未开始的直播间");
        }
        LiveRoom liveRoom = BeanUtil.copyProperties(request, LiveRoom.class);
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (!LiveRoomStatusEnum.NOT_STARTED.getValue().equals(liveRoom.getRoomStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播状态不正确");
        }
        liveRoom.setRoomStatus(LiveRoomStatusEnum.LIVING.getValue());
        liveRoom.setStartTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean endLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (!LiveRoomStatusEnum.LIVING.getValue().equals(liveRoom.getRoomStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播状态不正确");
        }
        liveRoom.setRoomStatus(LiveRoomStatusEnum.ENDED.getValue());
        liveRoom.setEndTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean switchExplainingProduct(Long roomId, Long liveProductId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (!LiveRoomStatusEnum.LIVING.getValue().equals(liveRoom.getRoomStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播未开始或已结束");
        }
        LiveProduct targetProduct = liveProductMapper.selectById(liveProductId);
        if (targetProduct == null || !roomId.equals(targetProduct.getRoomId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不在当前直播间中");
        }
        LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProduct::getRoomId, roomId);
        List<LiveProduct> products = liveProductMapper.selectList(wrapper);
        for (LiveProduct product : products) {
            product.setIsExplaining((byte) 0);
            liveProductMapper.updateById(product);
        }
        targetProduct.setIsExplaining((byte) 1);
        liveProductMapper.updateById(targetProduct);
        liveRoom.setCurrentProductId(liveProductId);
        this.updateById(liveRoom);
        return true;
    }

    @Override
    public LiveRoomVO getRoomDetail(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        LiveRoomVO roomVO = objToVo(liveRoom);
        LambdaQueryWrapper<LiveProduct> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(LiveProduct::getRoomId, roomId);
        productWrapper.orderByAsc(LiveProduct::getSortOrder);
        List<LiveProduct> products = liveProductMapper.selectList(productWrapper);
        if (CollUtil.isNotEmpty(products)) {
            List<Long> liveProductIds = products.stream().map(LiveProduct::getId).collect(Collectors.toList());
            LambdaQueryWrapper<LiveProductSellPoint> pointWrapper = new LambdaQueryWrapper<>();
            pointWrapper.in(LiveProductSellPoint::getLiveProductId, liveProductIds);
            pointWrapper.orderByAsc(LiveProductSellPoint::getSortOrder);
            List<LiveProductSellPoint> sellPoints = liveProductSellPointMapper.selectList(pointWrapper);
            Map<Long, List<LiveProductSellPoint>> pointMap = sellPoints.stream().collect(Collectors.groupingBy(LiveProductSellPoint::getLiveProductId));
            List<LiveProductVO> productVOs = products.stream().map(p -> {
                LiveProductVO vo = BeanUtil.copyProperties(p, LiveProductVO.class);
                List<LiveProductSellPoint> points = pointMap.get(p.getId());
                if (CollUtil.isNotEmpty(points)) {
                    vo.setSellPoints(points.stream().map(LiveProductSellPoint::getPointContent).collect(Collectors.toList()));
                }
                return vo;
            }).collect(Collectors.toList());
            roomVO.setProducts(productVOs);
            if (roomVO.getCurrentProductId() != null) {
                roomVO.setCurrentProduct(productVOs.stream().filter(p -> p.getId().equals(roomVO.getCurrentProductId())).findFirst().orElse(null));
            }
        }
        roomVO.setStatistics(getRoomStatistics(roomId));
        return roomVO;
    }

    @Override
    public Page<LiveRoomVO> getRoomPage(LiveRoomQueryRequest request) {
        Page<LiveRoom> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<LiveRoom> queryWrapper = getQueryWrapper(request);
        Page<LiveRoom> result = this.page(page, queryWrapper);
        Page<LiveRoomVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(liveRoomService.objToVo(result.getRecords()));
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getRoomName())) {
            queryWrapper.like(LiveRoom::getRoomName, request.getRoomName());
        }
        QueryWrapperUtil.addCondition(queryWrapper, request.getRoomStatus(), LiveRoom::getRoomStatus);
        QueryWrapperUtil.addCondition(queryWrapper, request.getCreatorId(), LiveRoom::getCreatorId);
        QueryWrapperUtil.addSortCondition(queryWrapper, request.getSortField(), request.getSortOrder(), LiveRoom::getId);
        return queryWrapper;
    }

    @Override
    public LiveRoomVO objToVo(LiveRoom liveRoom) {
        return Optional.ofNullable(liveRoom).map(r -> {
            LiveRoomVO vo = BeanUtil.copyProperties(r, LiveRoomVO.class);
            LiveRoomStatusEnum statusEnum = LiveRoomStatusEnum.getByValue(r.getRoomStatus());
            if (statusEnum != null) {
                vo.setRoomStatusText(statusEnum.getText());
            }
            return vo;
        }).orElse(null);
    }

    @Override
    public List<LiveRoomVO> objToVo(List<LiveRoom> liveRoomList) {
        return Optional.ofNullable(liveRoomList).map(list -> list.stream().map(this::objToVo).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addProductToRoom(LiveProductAddRequest request) {
        LiveRoom liveRoom = this.getById(request.getRoomId());
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (LiveRoomStatusEnum.ENDED.getValue().equals(liveRoom.getRoomStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播已结束，无法添加商品");
        }
        LiveProduct liveProduct = BeanUtil.copyProperties(request, LiveProduct.class);
        liveProduct.setSoldCount(0);
        liveProduct.setIsExplaining((byte) 0);
        int result = liveProductMapper.insert(liveProduct);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加商品失败");
        }
        List<String> sellPoints = request.getSellPoints();
        if (CollUtil.isNotEmpty(sellPoints)) {
            for (int i = 0; i < sellPoints.size(); i++) {
                LiveProductSellPoint sellPoint = new LiveProductSellPoint();
                sellPoint.setLiveProductId(liveProduct.getId());
                sellPoint.setPointContent(sellPoints.get(i));
                sellPoint.setSortOrder(i);
                liveProductSellPointMapper.insert(sellPoint);
            }
        }
        return true;
    }

    @Override
    public Boolean removeProductFromRoom(Long liveProductId) {
        LiveProduct liveProduct = liveProductMapper.selectById(liveProductId);
        if (liveProduct == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        int result = liveProductMapper.deleteById(liveProductId);
        return result > 0;
    }

    @Override
    public LiveStatisticsVO getRoomStatistics(Long roomId) {
        LambdaQueryWrapper<LiveStatistics> statWrapper = new LambdaQueryWrapper<>();
        statWrapper.eq(LiveStatistics::getRoomId, roomId);
        LiveStatistics statistics = liveStatisticsMapper.selectOne(statWrapper);
        LiveStatisticsVO vo = new LiveStatisticsVO();
        if (statistics != null) {
            vo = BeanUtil.copyProperties(statistics, LiveStatisticsVO.class);
            if (statistics.getTotalViewer() != null && statistics.getTotalViewer() > 0) {
                BigDecimal viewer = new BigDecimal(statistics.getTotalViewer());
                BigDecimal orderNum = new BigDecimal(statistics.getTotalOrderNum());
                vo.setConversionRate(orderNum.divide(viewer, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
            } else {
                vo.setConversionRate(BigDecimal.ZERO);
            }
        } else {
            vo.setRoomId(roomId);
            vo.setTotalOrderNum(0);
            vo.setTotalSales(BigDecimal.ZERO);
            vo.setTotalViewer(0);
            vo.setPeakViewer(0);
            vo.setAvgOnlineTime(0);
            vo.setConversionRate(BigDecimal.ZERO);
        }
        LambdaQueryWrapper<LiveProductRank> rankWrapper = new LambdaQueryWrapper<>();
        rankWrapper.eq(LiveProductRank::getRoomId, roomId);
        rankWrapper.orderByDesc(LiveProductRank::getSoldCount);
        List<LiveProductRank> ranks = liveProductRankMapper.selectList(rankWrapper);
        if (CollUtil.isNotEmpty(ranks)) {
            List<LiveProductRankVO> rankVOs = ranks.stream().map(r -> {
                LiveProductRankVO rankVO = BeanUtil.copyProperties(r, LiveProductRankVO.class);
                if (r.getClickCount() != null && r.getClickCount() > 0) {
                    BigDecimal clickCount = new BigDecimal(r.getClickCount());
                    BigDecimal soldCount = new BigDecimal(r.getSoldCount());
                    rankVO.setConversionRate(soldCount.divide(clickCount, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
                } else {
                    rankVO.setConversionRate(BigDecimal.ZERO);
                }
                return rankVO;
            }).collect(Collectors.toList());
            vo.setProductRanks(rankVOs);
        }
        LambdaQueryWrapper<LiveAudienceRetention> retentionWrapper = new LambdaQueryWrapper<>();
        retentionWrapper.eq(LiveAudienceRetention::getRoomId, roomId);
        retentionWrapper.orderByAsc(LiveAudienceRetention::getMinuteMark);
        List<LiveAudienceRetention> retentionList = liveAudienceRetentionMapper.selectList(retentionWrapper);
        if (CollUtil.isNotEmpty(retentionList)) {
            List<LiveAudienceRetentionVO> retentionVOs = retentionList.stream().map(r -> BeanUtil.copyProperties(r, LiveAudienceRetentionVO.class)).collect(Collectors.toList());
            vo.setRetentionData(retentionVOs);
        }
        return vo;
    }

    @Override
    public List<LiveProductRankVO> getProductRank(Long roomId) {
        LambdaQueryWrapper<LiveProductRank> rankWrapper = new LambdaQueryWrapper<>();
        rankWrapper.eq(LiveProductRank::getRoomId, roomId);
        rankWrapper.orderByDesc(LiveProductRank::getSoldCount);
        List<LiveProductRank> ranks = liveProductRankMapper.selectList(rankWrapper);
        if (CollUtil.isEmpty(ranks)) {
            return new ArrayList<>();
        }
        return ranks.stream().map(r -> {
            LiveProductRankVO rankVO = BeanUtil.copyProperties(r, LiveProductRankVO.class);
            if (r.getClickCount() != null && r.getClickCount() > 0) {
                BigDecimal clickCount = new BigDecimal(r.getClickCount());
                BigDecimal soldCount = new BigDecimal(r.getSoldCount());
                rankVO.setConversionRate(soldCount.divide(clickCount, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
            } else {
                rankVO.setConversionRate(BigDecimal.ZERO);
            }
            return rankVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LiveAudienceRetentionVO> getAudienceRetention(Long roomId) {
        LambdaQueryWrapper<LiveAudienceRetention> retentionWrapper = new LambdaQueryWrapper<>();
        retentionWrapper.eq(LiveAudienceRetention::getRoomId, roomId);
        retentionWrapper.orderByAsc(LiveAudienceRetention::getMinuteMark);
        List<LiveAudienceRetention> retentionList = liveAudienceRetentionMapper.selectList(retentionWrapper);
        if (CollUtil.isEmpty(retentionList)) {
            return new ArrayList<>();
        }
        return retentionList.stream().map(r -> BeanUtil.copyProperties(r, LiveAudienceRetentionVO.class)).collect(Collectors.toList());
    }
}
