package com.krisnaajiep.handler;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 23.04
@Last Modified 30/04/25 23.04
Version 1.0
*/

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

class MainHandler {
    void sendResponse(
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
