package com.rosy.main.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报修统计服务
 *
 * @author Rosy
 * @since 2025-01-31
 */
public interface IRepairStatisticsService {

    /**
     * 获取报修响应时间统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 响应时间统计数据
     */
    Map<String, Object> getResponseTimeStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取设备故障类型分析
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 故障类型分析数据
     */
    List<Map<String, Object>> getFaultTypeAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取工单数量和类型统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 工单统计数据
     */
    Map<String, Object> getOrderStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取维修人员工作量统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 维修人员工作量数据
     */
    List<Map<String, Object>> getTechnicianWorkloadStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取评价统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 评价统计数据
     */
    Map<String, Object> getEvaluationStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取月度工单趋势
     * @param months 月份数量
     * @return 月度工单趋势数据
     */
    List<Map<String, Object>> getMonthlyOrderTrend(Integer months);

    /**
     * 获取设备类型故障率统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 设备类型故障率数据
     */
    List<Map<String, Object>> getDeviceTypeFailureRate(LocalDateTime startDate, LocalDateTime endDate);
}