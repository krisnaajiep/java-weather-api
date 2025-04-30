package com.krisnaajiep;

import com.krisnaajiep.handler.RateLimiterHandler;
import com.krisnaajiep.handler.WeatherHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The {@code App} class provides the entry point and main application logic to start an HTTP server.
 */
public class App {
    /**
     * The port number on which the HTTP server will listen for incoming connections.<br>
     * (Default value: 8080)
     */
    private static int port = 8080;

    /**
     * The entry point of the application. Initializes the HTTP server and processes command-line arguments
     * to configure the server settings such as the port number.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                parseArgs(args);
            }

            startServer();
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Parses the command-line arguments and processes configuration options.
     * This method checks for specific arguments like "-p" or "--port" to set the server port.
     *
     * @param args the array of command-line arguments passed to the application.
     */
    private static void parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.equals("-p") || arg.equals("--port")) {
                if (args.length < 2) {
                    throw new IllegalArgumentException("Missing port argument");
                }

                setPort(args[1]);
                return;
            }
        }

        throw new IllegalArgumentException("Invalid argument: " + args[0]);
    }

    /**
     * Sets the port number for the server after validating the input string.
     * The port number must be a valid integer between 1 and 65535.
     *
     * @param newPortStr the string representing the new port number to be set.
     *                   It must be a valid numeric string within the acceptable range.
     * @throws IllegalArgumentException if the provided string is not a valid integer
     *                                  or if the resulting port number is outside the valid range.
     */
    private static void setPort(String newPortStr) {
        int newPort;

        try {
            newPort = Integer.parseInt(newPortStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port number: " + newPortStr);
        }

        if (newPort < 1 || newPort > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        port = newPort;
    }

    /**
     * Starts and configures the HTTP server instance.
     *
     * @throws IOException if an I/O error occurs during server initialization.
     */
    private static void startServer() throws IOException {
        System.out.println("Starting server on port " + port + "...");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RateLimiterHandler(new WeatherHandler()));
        server.setExecutor(null);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\nServer shutting down...")));

        System.out.println("Server running at http://localhost:" + port);
    }
}
