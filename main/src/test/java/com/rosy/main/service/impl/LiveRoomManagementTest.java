package com.rosy.main.service.impl;

import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.entity.LiveRoomStats;
import com.rosy.main.domain.vo.LiveConversionRateVO;
import com.rosy.main.domain.vo.ViewerRetentionCurveVO;
import com.rosy.main.service.ILiveRoomAnalyticsService;
import com.rosy.main.service.ILiveRoomProductService;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveRoomStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * 直播间管理功能测试
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@SpringBootTest
@ActiveProfiles("test")
public class LiveRoomManagementTest {

    @Autowired
    private ILiveRoomService liveRoomService;

    @Autowired
    private ILiveRoomProductService liveRoomProductService;

    @Autowired
    private ILiveRoomStatsService liveRoomStatsService;

    @Autowired
    private ILiveRoomAnalyticsService liveRoomAnalyticsService;

    @Test
    public void testCreateLiveRoom() {
        // 创建直播间
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle("测试直播间");
        liveRoom.setDescription("这是一个测试直播间");
        liveRoom.setCoverImage("https://example.com/cover.jpg");
        liveRoom.setStreamUrl("rtmp://example.com/live/test");
        liveRoom.setHostId(1L);
        liveRoom.setStatus(0); // 未开始
        liveRoom.setViewerCount(0);
        liveRoom.setStartTime(LocalDateTime.now().plusDays(1));
        
        boolean result = liveRoomService.save(liveRoom);
        assertTrue(result);
        assertNotNull(liveRoom.getId());
        
        // 验证直播间是否正确保存
        LiveRoom savedRoom = liveRoomService.getById(liveRoom.getId());
        assertNotNull(savedRoom);
        assertEquals("测试直播间", savedRoom.getTitle());
        assertEquals(1L, savedRoom.getHostId());
    }

    @Test
    public void testAddProductToLiveRoom() {
        // 先创建直播间
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle("商品测试直播间");
        liveRoom.setHostId(1L);
        liveRoom.setStatus(1); // 直播中
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoomService.save(liveRoom);
        
        // 添加商品到直播间
        LiveRoomProduct product = new LiveRoomProduct();
        product.setLiveRoomId(liveRoom.getId());
        product.setProductId(1001L);
        product.setProductName("测试商品");
        product.setProductImage("https://example.com/product.jpg");
        product.setProductPrice(new BigDecimal("99.99"));
        product.setSellingPoints("高品质，超值优惠");
        product.setDisplayOrder(1);
        product.setIsCurrent(true); // 当前讲解商品
        
        boolean result = liveRoomProductService.save(product);
        assertTrue(result);
        assertNotNull(product.getId());
        
        // 验证商品是否正确添加
        List<LiveRoomProduct> products = liveRoomProductService.listByLiveRoomId(liveRoom.getId());
        assertEquals(1, products.size());
        assertEquals("测试商品", products.get(0).getProductName());
        assertTrue(products.get(0).getIsCurrent());
    }

    @Test
    public void testUpdateLiveRoomStats() {
        // 先创建直播间
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle("统计测试直播间");
        liveRoom.setHostId(1L);
        liveRoom.setStatus(1); // 直播中
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoomService.save(liveRoom);
        
        // 更新观众统计数据
        boolean viewerResult = liveRoomStatsService.updateViewerStats(liveRoom.getId(), 100, 150);
        assertTrue(viewerResult);
        
        // 更新销售统计数据
        boolean salesResult = liveRoomStatsService.updateSalesStats(liveRoom.getId(), 10, new BigDecimal("999.90"));
        assertTrue(salesResult);
        
        // 验证统计数据是否正确更新
        List<LiveRoomStats> stats = liveRoomStatsService.listByLiveRoomId(liveRoom.getId());
        assertFalse(stats.isEmpty());
        
        LiveRoomStats latestStat = stats.get(stats.size() - 1);
        assertEquals(100, latestStat.getTotalViewers());
        assertEquals(150, latestStat.getPeakViewers());
        assertEquals(10, latestStat.getTotalOrders());
        assertEquals(new BigDecimal("999.90"), latestStat.getTotalSales());
    }

    @Test
    public void testConversionRateStats() {
        // 先创建直播间
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle("转化率测试直播间");
        liveRoom.setHostId(1L);
        liveRoom.setStatus(3); // 已结束
        liveRoom.setStartTime(LocalDateTime.now().minusDays(2));
        liveRoom.setEndTime(LocalDateTime.now().minusDays(1));
        liveRoomService.save(liveRoom);
        
        // 添加统计数据
        LiveRoomStats stats = new LiveRoomStats();
        stats.setLiveRoomId(liveRoom.getId());
        stats.setStatsDate(LocalDate.now().minusDays(1));
        stats.setTotalViewers(200);
        stats.setPeakViewers(250);
        stats.setTotalOrders(20);
        stats.setTotalSales(new BigDecimal("1999.80"));
        liveRoomStatsService.save(stats);
        
        // 获取转化率统计
        LiveConversionRateVO conversionRate = liveRoomAnalyticsService.getConversionRateStats(
                liveRoom.getId(), 
                LocalDate.now().minusDays(2), 
                LocalDate.now());
        
        assertNotNull(conversionRate);
        assertEquals(liveRoom.getId(), conversionRate.getLiveRoomId());
        assertEquals(200, conversionRate.getTotalViewers());
        assertEquals(20, conversionRate.getTotalOrders());
        assertEquals(new BigDecimal("1999.80"), conversionRate.getTotalSales());
        
        // 验证转化率计算
        BigDecimal expectedRate = new BigDecimal("20").divide(new BigDecimal("200"), 4, java.math.RoundingMode.HALF_UP);
        assertEquals(expectedRate, conversionRate.getConversionRate());
        assertTrue(conversionRate.getConversionRatePercent().contains("10.00%"));
        
        // 验证平均客单价计算
        BigDecimal expectedAvgOrderValue = new BigDecimal("1999.80").divide(new BigDecimal("20"), 2, java.math.RoundingMode.HALF_UP);
        assertEquals(expectedAvgOrderValue, conversionRate.getAvgOrderValue());
    }

    @Test
    public void testViewerRetentionCurve() {
        // 先创建直播间
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle("留存曲线测试直播间");
        liveRoom.setHostId(1L);
        liveRoom.setStatus(3); // 已结束
        liveRoom.setStartTime(LocalDateTime.now().minusDays(2));
        liveRoom.setEndTime(LocalDateTime.now().minusDays(1));
        liveRoomService.save(liveRoom);
        
        // 获取观众留存曲线
        ViewerRetentionCurveVO retentionCurve = liveRoomAnalyticsService.getViewerRetentionCurve(liveRoom.getId());
        
        assertNotNull(retentionCurve);
        assertEquals(liveRoom.getId(), retentionCurve.getLiveRoomId());
        assertNotNull(retentionCurve.getRetentionPoints());
        
        // 验证留存曲线数据点
        assertFalse(retentionCurve.getRetentionPoints().isEmpty());
        
        // 检查第一个数据点（0分钟）
        ViewerRetentionPointVO firstPoint = retentionCurve.getRetentionPoints().get(0);
        assertEquals(0, firstPoint.getTimePoint().intValue());
        assertNotNull(firstPoint.getTimestamp());
        
        // 检查最后一个数据点（55分钟）
        ViewerRetentionPointVO lastPoint = retentionCurve.getRetentionPoints().get(retentionCurve.getRetentionPoints().size() - 1);
        assertEquals(55, lastPoint.getTimePoint().intValue());
    }

    @Test
    public void testConversionRateComparison() {
        // 创建多个直播间
        LiveRoom room1 = new LiveRoom();
        room1.setTitle("对比测试直播间1");
        room1.setHostId(1L);
        room1.setStatus(3);
        room1.setStartTime(LocalDateTime.now().minusDays(3));
        room1.setEndTime(LocalDateTime.now().minusDays(2));
        liveRoomService.save(room1);
        
        LiveRoom room2 = new LiveRoom();
        room2.setTitle("对比测试直播间2");
        room2.setHostId(1L);
        room2.setStatus(3);
        room2.setStartTime(LocalDateTime.now().minusDays(2));
        room2.setEndTime(LocalDateTime.now().minusDays(1));
        liveRoomService.save(room2);
        
        // 添加统计数据
        LiveRoomStats stats1 = new LiveRoomStats();
        stats1.setLiveRoomId(room1.getId());
        stats1.setStatsDate(LocalDate.now().minusDays(2));
        stats1.setTotalViewers(100);
        stats1.setTotalOrders(5);
        stats1.setTotalSales(new BigDecimal("499.95"));
        liveRoomStatsService.save(stats1);
        
        LiveRoomStats stats2 = new LiveRoomStats();
        stats2.setLiveRoomId(room2.getId());
        stats2.setStatsDate(LocalDate.now().minusDays(1));
        stats2.setTotalViewers(200);
        stats2.setTotalOrders(30);
        stats2.setTotalSales(new BigDecimal("2999.70"));
        liveRoomStatsService.save(stats2);
        
        // 获取转化率对比
        List<LiveConversionRateVO> comparison = liveRoomAnalyticsService.getConversionRateComparison(
                Arrays.asList(room1.getId(), room2.getId()),
                LocalDate.now().minusDays(3),
                LocalDate.now());
        
        assertNotNull(comparison);
        assertEquals(2, comparison.size());
        
        // 验证第一个直播间的数据
        LiveConversionRateVO room1Stats = comparison.stream()
                .filter(stats -> stats.getLiveRoomId().equals(room1.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(room1Stats);
        assertEquals(100, room1Stats.getTotalViewers());
        assertEquals(5, room1Stats.getTotalOrders());
        
        // 验证第二个直播间的数据
        LiveConversionRateVO room2Stats = comparison.stream()
                .filter(stats -> stats.getLiveRoomId().equals(room2.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(room2Stats);
        assertEquals(200, room2Stats.getTotalViewers());
        assertEquals(30, room2Stats.getTotalOrders());
    }
}