package com.rosy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 页面控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Controller
@RequestMapping
public class PageController {

    /**
     * 直播间管理页面
     */
    @GetMapping("/live-room-management")
    public String liveRoomManagement() {
        return "forward:/live-room-management.html";
    }

    /**
     * 首页重定向到直播间管理页面
     */
    @GetMapping("/")
    public String index() {
        return "forward:/live-room-management.html";
    }
}