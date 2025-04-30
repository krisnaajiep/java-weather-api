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
    private static final String API_BASE_URL = System.getenv("API_BASE_URL");
    private static final String API_KEY = System.getenv("API_KEY");
    private final HttpClient httpClient;

    public WeatherService() {
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

    private String getFullUrl(String location) {
        return API_BASE_URL + "/" + location + "?key=" + API_KEY + "&include=days";
    }
}
