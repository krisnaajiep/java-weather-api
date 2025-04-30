package com.krisnaajiep.handler;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 29/04/25 18.15
@Last Modified 29/04/25 18.15
Version 1.0
*/

import com.krisnaajiep.exception.HttpResponseException;
import com.krisnaajiep.service.WeatherService;
import com.krisnaajiep.util.QueryParamParser;
import com.krisnaajiep.util.RedisClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.net.http.HttpResponse;

public class WeatherHandler extends MainHandler implements HttpHandler {
    private static final String[] ALLOWED_METHODS = {"GET"};
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private final WeatherService service;
    private final QueryParamParser queryParamParser;

    public WeatherHandler() throws IllegalStateException {
        this.service = new WeatherService();
        this.queryParamParser = new QueryParamParser();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (RedisClient cache = new RedisClient()) {
            if (!exchange.getRequestURI().getPath().equals("/")) {
                throw new HttpResponseException(404, "Not found", CONTENT_TYPE_TEXT);
            }

            if (!isAllowedMethod(exchange.getRequestMethod())) {
                throw new HttpResponseException(405, "Method not allowed", CONTENT_TYPE_TEXT);
            }

            String query = exchange.getRequestURI().getQuery();
            String location = queryParamParser.parse(query).get("location");
            if (location == null) {
                throw new HttpResponseException(400, "Missing location parameter", CONTENT_TYPE_TEXT);
            }

            String cacheData = cache.get(location);
            if (cacheData != null) {
                throw new HttpResponseException(200, cacheData, CONTENT_TYPE_JSON);
            }

            HttpResponse<String> response = service.getWeather(location);
            cache.set(location, response.body());
            sendResponse(response.statusCode(), response.body(), CONTENT_TYPE_JSON, exchange);
        } catch (IOException e) {
            sendResponse(500, e.getMessage(), CONTENT_TYPE_TEXT, exchange);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            sendResponse(503, e.getMessage(), CONTENT_TYPE_TEXT, exchange);
        } catch (JedisException e) {
            sendResponse(503, e.getMessage(), CONTENT_TYPE_TEXT, exchange);
        } catch (HttpResponseException e){
            sendResponse(e.getStatusCode(), e.getBody(),e.getContentType(), exchange);
        } finally {
            exchange.close();
        }
    }

    private boolean isAllowedMethod(String method) {
        for (String allowedMethod : ALLOWED_METHODS) {
            if (allowedMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }
}
