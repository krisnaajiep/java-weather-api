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
import java.util.Map;

/**
 * The {@code WeatherHandler} class is responsible for handling HTTP requests related to
 * weather information. It leverages caching for efficiency and interacts with external
 * APIs to fetch live weather data.
 * <p>
 * This class extends {@code MainHandler} to use response handling capabilities and
 * implements {@code HttpHandler} to handle HTTP requests in an HTTP server environment.
 */
public class WeatherHandler extends MainHandler implements HttpHandler {
    /**
     * A list of HTTP methods allowed by the {@code WeatherHandler} class to process incoming requests.
     * This variable is used to validate and restrict HTTP requests to specific methods, ensuring proper
     * handling and security of the API.
     */
    private static final String[] ALLOWED_METHODS = {"GET"};

    /**
     * A constant representing the MIME type for JSON content, "application/json".
     * This value is commonly used in HTTP headers to indicate that the body of
     * a request or response contains JSON-formatted data.
     */
    private static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * A constant defining the MIME type for plain text responses, which is "text/plain".
     * This value is primarily used in HTTP responses to specify that the content being
     * sent to the client is in plain text format.
     */
    private static final String CONTENT_TYPE_TEXT = "text/plain";

    /**
     * An instance represents an instance of {@link WeatherService}, which provides functionalities
     * to interact with a weather API for retrieving weather data.
     */
    private final WeatherService service;

    /**
     * An instance that is responsible for parsing query parameters from an HTTP request URL.
     * This parser extracts key-value pairs from query strings and returns them as a {@link Map}.
     * If the query string is empty or null, it returns an empty map.
     */
    private final QueryParamParser queryParamParser;

    /**
     * Constructs a {@code WeatherHandler} instance used for handling weather-related
     * HTTP requests. This handler integrates with {@link WeatherService} to fetch
     * weather data and utilizes {@link QueryParamParser} to parse query parameters
     * from incoming HTTP requests.
     *
     * @throws IllegalStateException if the handler fails to initialize due to internal configuration issues.
     */
    public WeatherHandler() throws IllegalStateException {
        this.service = new WeatherService();
        this.queryParamParser = new QueryParamParser();
    }

    /**
     * Handles HTTP requests for weather-related data, performing validation and processing
     * to provide appropriate responses or errors.
     * <p>
     * The method supports GET requests to fetch weather information for a specified location.
     * It checks for method validity, parses query parameters, retrieves cached responses,
     * fetches weather data from an external service if required, and sends responses to clients.
     * Uses Redis as a temporary caching mechanism.
     *
     * @param exchange the {@link HttpExchange} object representing the incoming HTTP request and response
     * @throws IOException if an IO-related error occurs during request handling or response writing
     */
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

    /**
     * Determines whether the provided HTTP method is allowed by checking it against
     * a predefined list of allowable methods.
     *
     * @param method the HTTP method to validate; should not be null or empty
     * @return true if the provided method matches one of the allowed methods, case-insensitively; false otherwise
     */
    private boolean isAllowedMethod(String method) {
        for (String allowedMethod : ALLOWED_METHODS) {
            if (allowedMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }
}
