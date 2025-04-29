package com.krisnaajiep;

import com.krisnaajiep.handler.WeatherHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        try {
            int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/", new WeatherHandler());
            server.setExecutor(null);
            System.out.println("Server running on port " + port);
            server.start();
        } catch (NumberFormatException e) {
            System.out.println("Port must be a number");
        } catch (BindException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
