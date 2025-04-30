package com.krisnaajiep.handler;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 22.57
@Last Modified 30/04/25 22.57
Version 1.0
*/

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.bucket4j.Bucket;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterHandler extends MainHandler implements HttpHandler {
    private static final int CAPACITY = 20;
    private static final int REFILL_TOKENS = 10;
    private static final Duration REFILL_INTERVAL = Duration.ofMinutes(1);
    private final HttpHandler nextHandler;
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    public RateLimiterHandler(HttpHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
        Bucket bucket = ipBuckets.computeIfAbsent(ip, k -> createNewBucket());

        long remainingTokens = bucket.getAvailableTokens();
        exchange.getResponseHeaders().add("X-RateLimit-Remaining", String.valueOf(remainingTokens));
        exchange.getResponseHeaders().add("X-RateLimit-Limit", String.valueOf(CAPACITY));

        if (!bucket.tryConsume(1)) {
            System.out.printf("Rate limit exceeded for IP: %s%n", ip);
            sendResponse(429, "Too many requests", "text/plain", exchange);
            return;
        }

        nextHandler.handle(exchange);
    }

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(CAPACITY)
                        .refillGreedy(REFILL_TOKENS, REFILL_INTERVAL)
                ).build();
    }
}
