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
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.SetParams;

/**
 * The {@code RedisClient} class provides a simple Redis client implementation for interacting
 * with a Redis server. This class supports basic operations such as setting and getting
 * key-value pairs, and ensures proper connection handling through the {@code AutoCloseable} interface.
 */
public class RedisClient implements AutoCloseable {
    /**
     * An instance used to interact with a Redis server. This variable is initialized with
     * host, port, user, password, and database configuration derived from environment variables.
     */
    private final UnifiedJedis jedis;

    /**
     * Initializes a new instance of the {@code RedisClient} class with the connection
     * settings derived from environment variables. This constructor establishes a
     * connection to a Redis server by configuring a {@code UnifiedJedis} client using
     * parameters such as host, port, user, password, and database index.
     * <p>
     * If any required environment variable is missing or the connection to Redis fails, an
     * {@code IllegalStateException} is thrown with an appropriate error message.
     *
     * @throws IllegalStateException if a required environment variable is missing
     *                               or the connection to Redis cannot be established
     */
    public RedisClient() {
        try {
            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                    .user(Env.getOptional("REDIS_USERNAME").orElse("default"))
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
            throw new JedisException("Failed to connect to Redis, " + e.getMessage());
        }
    }

    /**
     * Sets a key-value pair in the Redis database with an expiration time of 12 hours.
     *
     * @param key   the key to be stored in Redis; must not be null or empty
     * @param value the value associated with the specified key; must not be null
     */
    public void set(String key, String value) {
        jedis.set(key, value, SetParams.setParams().ex(60 * 60 * 12));
    }

    /**
     * Retrieves the value associated with the specified key from the Redis database.
     *
     * @param key the key whose associated value is to be returned; must not be null
     * @return the value associated with the specified key, or null if the key does not exist
     */
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * Closes the underlying {@code UnifiedJedis} connection.
     */
    @Override
    public void close() {
        jedis.close();
    }
}
