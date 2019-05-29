package com.beneil.redis.service;

import com.beneil.redis.request.Request;

/**
 * 请求异步执行service
 */
public interface RequestAsyncService {
    void handler(Request request);
}
