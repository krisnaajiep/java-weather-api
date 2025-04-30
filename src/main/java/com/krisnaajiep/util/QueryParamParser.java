package com.krisnaajiep.util;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 07.43
@Last Modified 30/04/25 07.43
Version 1.0
*/

import com.sun.net.httpserver.HttpExchange;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryParamParser {
    public Map<String , String> parse(String query) {
        if (query == null || query.isBlank()) {
            return Map.of();
        }
        return parseQueryParams(query.split("&"));
    }

    private Map<String, String> parseQueryParams(String[] params) {
        Map<String, String> result = new HashMap<>();

        for (String param : params) {
            String[] parts = param.split("=");
            if (parts.length == 2) {
                result.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }

        return result;
    }
}
