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

public class HttpResponseException extends RuntimeException {
    private final int statusCode;
    private final String body;
    private final String contentType;

    public HttpResponseException(int statusCode, String body, String contentType) {
        this.statusCode = statusCode;
        this.body = body;
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
