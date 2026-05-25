package com.update.infra.client;

import com.update.config.AppConfig;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Base64;

public class ChangeIpClient {

    public static Boolean start(@NotNull AppConfig appConfig){
        IO.println("Iniciando processo de verificação e atualização");
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            String credentials = appConfig.email() + ":" + appConfig.password();
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
            HttpRequest updateRequest = HttpRequest.newBuilder()
                    .uri(URI.create(appConfig.apiUrl()+appConfig.host()))
                    .header("Authorization", basicAuth)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            var updateResponse = client.send(
                    updateRequest,
                    java.net.http.HttpResponse.BodyHandlers.ofString()
            );
            return updateResponse.statusCode() == 200;
        }catch (Exception e){
            IO.println(e.getMessage());
            return  false;
        }
    }

    }




