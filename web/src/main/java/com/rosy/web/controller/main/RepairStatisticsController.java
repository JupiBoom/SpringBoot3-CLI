package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.RepairStatisticsVO;
import com.rosy.main.service.IRepairStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/statistics")
public class RepairStatisticsController {

    @Resource
    private IRepairStatisticsService repairStatisticsService;

    @GetMapping("/overall")
    public ApiResponse getOverallStatistics() {
        RepairStatisticsVO statistics = repairStatisticsService.getOverallStatistics();
        return ApiResponse.success(statistics);
    }
}
