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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code QueryParamParser} class provides functionality for parsing URL query strings into a map of
 * key-value pairs. It decodes the URL-encoded query parameters using the UTF-8 character encoding.
 */
public class QueryParamParser {
    /**
     * Parses a URL query string into a map of key-value pairs.
     * If the given query string is null or blank, it returns an empty map.
     *
     * @param query the query string containing URL-encoded parameters,
     *              typically in the format "key1=value1&key2=value2"
     * @return a map containing decoded key-value pairs extracted from the query string,
     *         or an empty map if the input is null or blank
     */
    public Map<String , String> parse(String query) {
        if (query == null || query.isBlank()) {
            return Map.of();
        }
        return parseQueryParams(query.split("&"));
    }

    /**
     * Parses an array of strings representing URL-encoded query parameters into a map of decoded key-value pairs.
     *
     * @param params an array of strings, where each string is expected to be in the format "key=value",
     *               representing URL-encoded query parameters
     * @return a map containing decoded key-value pairs extracted from the input parameter array,
     *         or an empty map if no valid key-value pairs are found
     */
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
