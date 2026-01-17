package com.rosy.web.controller.main;

import com.rosy.common.core.domain.R;
import com.rosy.main.domain.vo.RepairmanVO;
import com.rosy.main.service.IRepairmanService;
import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 维修人员控制器
 */
@RestController
@RequestMapping("/api/repair/repairman")
@Tag(name = "维修人员管理")
public class RepairmanController {

    @Autowired
    private IRepairmanService repairmanService;

    /**
     * 查询可用的维修人员列表
     */
    @GetMapping("/available")
    @Operation(summary = "查询可用的维修人员列表")
    public R<List<RepairmanVO>> getAvailableRepairmen(@RequestParam(required = false) String equipmentType) {
        List<RepairmanVO> repairmen = repairmanService.getAvailableRepairmen(equipmentType);
        return R.ok(repairmen);
    }

    /**
     * 根据用户ID查询维修人员信息
     */
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "根据用户ID查询维修人员信息")
    public R<RepairmanVO> getRepairmanByUserId(@PathVariable Long userId) {
        RepairmanVO repairman = repairmanService.getRepairmanByUserId(userId);
        if (repairman == null) {
            return R.fail("维修人员不存在");
        }
        return R.ok(repairman);
    }

    /**
     * 根据ID查询维修人员信息
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "根据ID查询维修人员信息")
    public R<RepairmanVO> getRepairmanById(@PathVariable Long id) {
        RepairmanVO repairman = repairmanService.getRepairmanById(id);
        if (repairman == null) {
            return R.fail("维修人员不存在");
        }
        return R.ok(repairman);
    }

    /**
     * 查询所有维修人员列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有维修人员列表")
    public R<List<RepairmanVO>> getAllRepairmen(@RequestParam(required = false) String status) {
        List<RepairmanVO> repairmen = repairmanService.getAllRepairmen(status);
        return R.ok(repairmen);
    }

    /**
     * 查询维修人员工作统计
     */
    @GetMapping("/statistics/{id}")
    @Operation(summary = "查询维修人员工作统计")
    public R<RepairmanStatisticsVO> getRepairmanStatistics(@PathVariable Long id) {
        RepairmanVO repairman = repairmanService.getRepairmanById(id);
        if (repairman == null) {
            return R.fail("维修人员不存在");
        }
        
        RepairmanStatisticsVO statistics = new RepairmanStatisticsVO();
        statistics.setRepairmanId(id);
        statistics.setRepairmanName(repairman.getName());
        statistics.setCompletedOrders(repairman.getCompletedOrders());
        statistics.setAverageScore(repairman.getAverageScore());
        statistics.setSpecialty(repairman.getSpecialty());
        statistics.setSkillLevel(repairman.getSkillLevel());
        statistics.setStatus(repairman.getStatus());
        
        return R.ok(statistics);
    }
}

/**
 * 维修人员统计VO
 */
class RepairmanStatisticsVO {
    private Long repairmanId;
    private String repairmanName;
    private Integer completedOrders;
    private Double averageScore;
    private String specialty;
    private String skillLevel;
    private String status;

    public Long getRepairmanId() {
        return repairmanId;
    }

    public void setRepairmanId(Long repairmanId) {
        this.repairmanId = repairmanId;
    }

    public String getRepairmanName() {
        return repairmanName;
    }

    public void setRepairmanName(String repairmanName) {
        this.repairmanName = repairmanName;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}