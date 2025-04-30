package com.krisnaajiep.exception;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 01/05/25 01.58
@Last Modified 01/05/25 01.58
Version 1.0
*/

/**
 * The {@code HttpResponseException} class represents a custom runtime exception that
 * encapsulates detailed information about an HTTP response, such as the status code,
 * response body, and content type. This exception can be used to propagate HTTP error
 * responses throughout the application.
 */
public class HttpResponseException extends RuntimeException {
    /**
     * Represents the HTTP status code associated with the response.
     * This value typically indicates the outcome of an HTTP request,
     * such as 200 for success or 404 for not found.
     */
    private final int statusCode;

    /**
     * Represents the body of the HTTP response associated with this exception.
     * This typically contains the response content as provided by the server,
     * such as error details, JSON, HTML, or other textual data.
     */
    private final String body;

    /**
     * Represents the content type of the HTTP response associated with this exception.
     * This value typically indicates the media type of the response content, such as
     * "application/json", "text/html", or "text/plain".
     */
    private final String contentType;

    /**
     * Constructs a new {@code HttpResponseException} with the specified HTTP status code,
     * response body, and content type.
     *
     * @param statusCode the HTTP status code associated with the response; typically indicates
     *                   the outcome of an HTTP request, such as 200 for success or 404 for not found.
     * @param body       the content of the HTTP response body; may include error details, JSON,
     *                   HTML, or other textual data as provided by the server.
     * @param contentType the MIME type of the HTTP response content; indicates the media type
     *                    of the response, such as "application/json", "text/html", or "text/plain".
     */
    public HttpResponseException(int statusCode, String body, String contentType) {
        this.statusCode = statusCode;
        this.body = body;
        this.contentType = contentType;
    }

    /**
     * Retrieves the HTTP status code associated with this exception.
     *
     * @return the HTTP status code indicating the result of an HTTP request, such as 200 for success
     * or 404 for not found.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Retrieves the body of the HTTP response associated with this exception.
     *
     * @return the response body content as a string, which may include error details, JSON,
     * HTML, or other textual data provided by the server.
     */
    public String getBody() {
        return body;
    }

    /**
     * Retrieves the content type of the HTTP response associated with this exception.
     *
     * @return the MIME type of the HTTP response content, indicating the media type
     * of the response, such as "application/json", "text/html", or "text/plain".
     */
    public String getContentType() {
        return contentType;
    }
}
