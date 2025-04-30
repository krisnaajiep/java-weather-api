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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    private final String apiKey;
    private final HttpClient httpClient;

    public WeatherService() {
        this.apiKey = getApiKey();
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getWeather(String location) throws IOException, InterruptedException {
        String fullUrl = getFullUrl(location);
        URI uri = URI.create(fullUrl);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getApiKey() {
        String value = System.getenv("API_KEY");

        if (value == null) {
            throw new IllegalStateException("Missing environment variable: " + "API_KEY");
        }

        return value;
    }

    private String getFullUrl(String location) {
        return "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                + location + "?key=" + apiKey + "&include=days";
    }
}
