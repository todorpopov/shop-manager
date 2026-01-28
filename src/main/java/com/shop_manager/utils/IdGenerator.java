package com.shop_manager.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private final AtomicLong seq = new AtomicLong(0);

    public Long nextId() {
        return seq.incrementAndGet();
    }
}
