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

/**
 * The {@code MainHandler} class provides utility methods to handle HTTP responses in
 * an HTTP server environment.
 */
class MainHandler {
    /**
     * Sends an HTTP response to the client with the specified status code, response body,
     * and content type using the given {@link HttpExchange} instance.
     * <p>
     * If the response body is null or blank, this method sends a response without a body.
     * Otherwise, the response body is written to the output stream of the exchange object.
     *
     * @param statusCode the HTTP status code to be sent in the response
     * @param body       the response body to be sent; nullable, and if blank or null, a
     *                   response without a body is sent
     * @param contentType the MIME type of the response content
     * @param exchange   the {@link HttpExchange} instance used to send the response
     * @throws IOException if an error occurs while writing the response body or headers
     */
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

    /**
     * Writes the specified HTTP response body and status code to the provided {@link HttpExchange} instance.
     *
     * @param exchange   the {@link HttpExchange} instance used to send the response
     * @param statusCode the HTTP status code to be sent in the response
     * @param body       the response body to be sent; must not be null
     * @throws IOException if an error occurs while writing the response body or headers
     */
    private void writeResponseBody(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.sendResponseHeaders(statusCode, body.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
