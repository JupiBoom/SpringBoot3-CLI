package com.rosy.web.controller.main.utils;

/**
 * 用户上下文持有器
 * 用于获取当前登录用户的信息
 */
public class UserHolder {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     */
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        Long userId = userIdHolder.get();
        // 临时返回一个默认用户ID，实际项目中应该从JWT或Session中获取
        return userId != null ? userId : 1L;
    }

    /**
     * 清除当前用户ID
     */
    public static void clear() {
        userIdHolder.remove();
    }
}
