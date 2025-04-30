package com.krisnaajiep.service;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 06.32
@Last Modified 30/04/25 06.32
Version 1.0
*/

import com.krisnaajiep.util.Env;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The {@code WeatherService} class is responsible for interacting with an
 * external weather API to fetch weather data for a specified location.
 * <p>
 * This service uses an API key stored in the environment variables and makes HTTP
 * requests to the Visual Crossing Weather API to retrieve the required data.
 */
public class WeatherService {
    /**
     * The {@code apiKey} variable holds the API key used for authentication
     * when making requests to the external weather API.
     */
    private final String apiKey;

    /**
     * Represents an instance of {@link HttpClient} used for sending HTTP requests
     * and receiving HTTP responses from the external weather API.
     */
    private final HttpClient httpClient;

    /**
     * Constructs a new instance of the {@code WeatherService} class.
     * <p>
     * This constructor initializes the weather service with an API key retrieved from
     * the environment variables and sets up an HTTP client for sending requests to
     * the external weather API.
     *
     * @throws IllegalStateException if the API key is missing from the environment variables
     */
    public WeatherService() {
        this.apiKey = Env.get("API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Fetches weather information for the specified location by sending an HTTP request
     * to an external weather API and returns the HTTP response.
     *
     * @param location the name of the location for which weather data is to be retrieved; must not be null or empty
     * @return an {@link HttpResponse} object containing the weather data as a string in the response body
     * @throws IOException if an I/O error occurs when sending or receiving the HTTP request
     * @throws InterruptedException if the operation is interrupted while waiting for the response
     */
    public HttpResponse<String> getWeather(String location) throws IOException, InterruptedException {
        String fullUrl = getFullUrl(location);
        URI uri = URI.create(fullUrl);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Constructs a full URL string to access the Visual Crossing Weather API using the specified location.
     * The URL includes the API key and configuration parameters necessary for the request.
     *
     * @param location the name of the location for which the full API URL is to be constructed; must not be null or empty
     * @return the full URL string for accessing the weather data for the specified location
     */
    private String getFullUrl(String location) {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                + location + "/today?key=" + apiKey + "&include=days";
    }
}
