package com.krisnaajiep.util;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 30/04/25 12.03
@Last Modified 30/04/25 12.03
Version 1.0
*/

import java.util.Optional;

public class Env {
    public static String get (String key) {
        String value = System.getenv(key);

        if (value == null) {
            throw new IllegalStateException("Missing environment variable: " + key);
        }

        return value;
    }

    public static Optional<String> getOptional (String key) {
        return Optional.ofNullable(System.getenv(key));
    }
}
