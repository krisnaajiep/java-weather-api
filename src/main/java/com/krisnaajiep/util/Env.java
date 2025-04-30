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

/**
 * The {@code Env} class provides utility methods for retrieving environment variables.
 * This class allows you to retrieve mandatory or optional environment variables
 * from the system's environment configuration.
 */
public class Env {
    /**
     * Retrieves the value of the specified environment variable.
     * If the environment variable is not set, an {@code IllegalStateException} is thrown.
     *
     * @param key the name of the environment variable to retrieve
     * @return the value of the specified environment variable
     * @throws IllegalStateException if the environment variable is not found
     */
    public static String get (String key) {
        String value = System.getenv(key);

        if (value == null) {
            throw new IllegalStateException("Missing environment variable: " + key);
        }

        return value;
    }

    /**
     * Retrieves the value of the specified environment variable as an {@code Optional}.
     * If the environment variable is not set, the returned {@code Optional} will be empty.
     *
     * @param key the name of the environment variable to retrieve
     * @return an {@code Optional} containing the value of the specified environment variable,
     *         or an empty {@code Optional} if the variable is not set
     */
    public static Optional<String> getOptional (String key) {
        return Optional.ofNullable(System.getenv(key));
    }
}
