package com.update.infra.network;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Optional;

public class GetPublicIp {

    public static Optional<String> getPublicIp() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.ipify.org"))
                    .GET()
                    .build();

            var response = client.send(
                    request,
                    java.net.http.HttpResponse.BodyHandlers.ofString()
            );

            return Optional.of(response.body());

        } catch (HttpTimeoutException e) {
            IO.println("Excedeu o tempo de espera");
            return Optional.empty();

        } catch (Exception e) {
            IO.println("Erro genérico: " + e.getMessage());
            return Optional.empty();
        }
    }
}
