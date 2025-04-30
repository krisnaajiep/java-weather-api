package com.krisnaajiep;

import com.krisnaajiep.handler.WeatherHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static int port = 8080;

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

    private static void startServer() throws IOException {
        System.out.println("Starting server on port " + port + "...");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new WeatherHandler());
        server.setExecutor(null);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\nServer shutting down...")));

        System.out.println("Server running at http://localhost:" + port);
    }
}
