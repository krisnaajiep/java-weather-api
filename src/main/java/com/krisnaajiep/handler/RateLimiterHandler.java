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

/**
 * The {@code RateLimiterHandler} class implements an HTTP request handler that applies
 * rate limiting based on client IP addresses. It uses token bucket algorithms to control
 * the number of requests allowed per client within a specific time interval.
 * <p>
 * This handler uses the provided {@link HttpHandler} as the next handler in the chain
 * and restricts excessive requests by sending a 429 status code ("Too Many Requests").
 * It also includes rate-limiting headers in the response to inform clients about their
 * rate limits and remaining requests allowed.
 * <p>
 * The rate limiter operates with a fixed capacity and refill rate, using a {@link Bucket}
 * to implement the token bucket algorithm for each client IP.
 * <p>
 * This class is thread-safe and suitable for concurrent HTTP request handling.
 */
public class RateLimiterHandler extends MainHandler implements HttpHandler {
    /**
     * Defines the maximum number of tokens that a rate-limiting bucket can hold.
     * This value represents the upper bound of requests allowed for a client
     * within a given time period before the bucket needs to be refilled.
     */
    private static final int CAPACITY = 20;

    /**
     * Specifies the number of tokens added to a rate-limiting bucket during each
     * refill interval. This constantly works in conjunction with the refill interval
     * and capacity to determine how often and how many new tokens are made available
     * to clients for their requests.
     */
    private static final int REFILL_TOKENS = 10;

    /**
     * Specifies the time interval used for refilling tokens in the rate-limiting buckets.
     * This duration determines how frequently new tokens are added to the token bucket
     * for each client IP. The value is set to 1 minute, aligning with the rate-limiting
     * policy to regulate HTTP request traffic.
     */
    private static final Duration REFILL_INTERVAL = Duration.ofMinutes(1);

    /**
     * Represents the next {@link HttpHandler} in the processing chain. It is used to delegate the
     * request handling to another handler after the current handler completes its specific logic.
     */
    private final HttpHandler nextHandler;

    /**
     * A concurrent mapping of IP addresses to their respective rate-limiting {@code Bucket} instances.
     * Each key in the map represents an IP address as a {@code String}, and its associated value
     * represents the rate-limiting bucket assigned to that IP.
     */
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code RateLimiterHandler} with the specified next handler in the handler chain.
     * This handler enforces rate-limiting policies for incoming HTTP requests, and forwards requests
     * to the specified {@code nextHandler} after applying the rate-limiting rules.
     *
     * @param nextHandler the next {@link HttpHandler} in the processing chain. It handles requests
     *                    after the rate limiter has applied its policies.
     */
    public RateLimiterHandler(HttpHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Handles incoming HTTP requests by enforcing rate-limiting policies based on the client's IP address.
     * If the rate limit is exceeded, a "429 Too Many Requests" response is sent. Otherwise, the request
     * is forwarded to the next handler in the chain.
     *
     * @param exchange the {@link HttpExchange} instance representing the HTTP request and response
     * @throws IOException if an error occurs while processing the HTTP exchange
     */
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

    /**
     * Creates a new rate-limiting bucket with predefined capacity and refill strategy.
     * This bucket is configured to limit the number of requests a client can make
     * within a defined time interval.
     *
     * @return a {@code Bucket} instance configured with the specified capacity and refill policy
     */
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(CAPACITY)
                        .refillGreedy(REFILL_TOKENS, REFILL_INTERVAL)
                ).build();
    }
}
