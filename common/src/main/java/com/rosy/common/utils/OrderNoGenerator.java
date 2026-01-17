package com.rosy.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工单编号生成器
 */
public class OrderNoGenerator {

    private static final AtomicInteger SEQ = new AtomicInteger(0);
    private static final int MAX_SEQ = 9999;

    public static String generate() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int seq = SEQ.incrementAndGet();
        if (seq > MAX_SEQ) {
            SEQ.set(0);
            seq = SEQ.incrementAndGet();
        }
        return "R" + date + String.format("%04d", seq);
    }
}
