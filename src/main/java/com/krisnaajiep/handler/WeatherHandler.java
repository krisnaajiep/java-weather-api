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

import com.krisnaajiep.service.WeatherService;
import com.krisnaajiep.util.QueryParamParser;
import com.krisnaajiep.util.RedisClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;

public class WeatherHandler implements HttpHandler {
    private static final String[] ALLOWED_METHODS = {"GET"};
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private final WeatherService service;
    private final QueryParamParser queryParamParser;
    private final RedisClient cache;

    public WeatherHandler() throws IllegalStateException {
        this.service = new WeatherService();
        this.queryParamParser = new QueryParamParser();
        this.cache = new RedisClient();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestURI().getPath().equals("/")) {
                sendResponse(404, "Not found", CONTENT_TYPE_TEXT, exchange);
                return;
            }

            if (!isAllowedMethod(exchange.getRequestMethod())) {
                sendResponse(405, "Method not allowed", CONTENT_TYPE_TEXT, exchange);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String location = queryParamParser.parse(query).get("location");

            if (location == null) {
                sendResponse(400, "Missing location parameter", CONTENT_TYPE_TEXT, exchange);
                return;
            }

            if (cache.get(location) != null) {
                sendResponse(200, cache.get(location), CONTENT_TYPE_JSON, exchange);
                return;
            }

            HttpResponse<String> response = service.getWeather(location);
            cache.set(location, response.body());
            sendResponse(response.statusCode(), response.body(), CONTENT_TYPE_JSON, exchange);
        } catch (IOException e) {
            sendResponse(500, e.getMessage(), CONTENT_TYPE_TEXT, exchange);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            sendResponse(503, e.getMessage(), CONTENT_TYPE_TEXT, exchange);
        } finally {
            exchange.close();
            cache.close();
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

    private void sendResponse(
            int statusCode,
            String body,
            String contentType,
            HttpExchange exchange
    ) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", contentType);

        if (body == null || body.isBlank()) {
            exchange.sendResponseHeaders(statusCode, -1);
        } else {
            writeResponseBody(exchange, statusCode, body);
        }
    }

    private void writeResponseBody(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.sendResponseHeaders(statusCode, body.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
