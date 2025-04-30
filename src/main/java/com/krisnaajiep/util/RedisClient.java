package com.krisnaajiep.util;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 11.50
@Last Modified 30/04/25 11.50
Version 1.0
*/

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.params.SetParams;

public class RedisClient implements AutoCloseable {
    private final UnifiedJedis jedis;

    public RedisClient() {
        try {
            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                    .user(Env.get("REDIS_USERNAME"))
                    .password(Env.getOptional("REDIS_PASSWORD").orElse(""))
                    .database(Integer.parseInt(Env.getOptional("REDIS_DATABASE").orElse("0")))
                    .build();

            jedis = new UnifiedJedis(
                    new HostAndPort(
                            Env.get("REDIS_HOST"),
                            Integer.parseInt(Env.get("REDIS_PORT"))
                    ), clientConfig
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to connect to Redis, " + e.getMessage());
        }
    }

    public void set(String key, String value) {
        jedis.set(key, value, SetParams.setParams().ex(60 * 60 * 12));
    }

    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
