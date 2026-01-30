package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.entity.LiveRoomData;
import com.rosy.main.domain.entity.LiveRoomSales;
import com.rosy.main.service.ILiveRoomDataService;
import com.rosy.main.service.ILiveRoomSalesService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/live/room/data")
public class LiveRoomDataController {

    @Resource
    ILiveRoomDataService liveRoomDataService;

    @Resource
    ILiveRoomSalesService liveRoomSalesService;

    @GetMapping("/basic/{liveRoomId}")
    public ApiResponse getLiveRoomBasicData(@PathVariable Long liveRoomId) {
        LiveRoomData liveRoomData = liveRoomDataService.getLiveRoomData(liveRoomId);
        return ApiResponse.success(liveRoomData);
    }

    @PostMapping("/update-audience")
    public ApiResponse updateAudienceCount(@RequestParam Long liveRoomId, @RequestParam Integer audienceCount) {
        boolean result = liveRoomDataService.updateAudienceCount(liveRoomId, audienceCount);
        return ApiResponse.success(result);
    }

    @PostMapping("/update-sales")
    public ApiResponse updateSalesData(@RequestParam Long liveRoomId, @RequestParam Integer orderCount, @RequestParam Double salesAmount) {
        boolean result = liveRoomDataService.updateSalesData(liveRoomId, orderCount, salesAmount);
        return ApiResponse.success(result);
    }

    @GetMapping("/sales/{liveRoomId}")
    public ApiResponse getLiveRoomSalesData(@PathVariable Long liveRoomId) {
        List<LiveRoomSales> salesList = liveRoomSalesService.getLiveRoomSales(liveRoomId);
        return ApiResponse.success(salesList);
    }

    @PostMapping("/add-sales")
    public ApiResponse addSalesData(@RequestParam Long liveRoomId, @RequestParam Long itemId, @RequestParam Integer orderCount, @RequestParam Double salesAmount) {
        boolean result = liveRoomSalesService.addSalesData(liveRoomId, itemId, orderCount, salesAmount);
        return ApiResponse.success(result);
    }
}