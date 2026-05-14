package com.update;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Base64;

public class UpdateIp {

    public static void UpdateIp(String email, String password, String API_URL, String YOUR_HOST) throws IOException, InterruptedException {

        System.out.println("Entering UpdateIp");

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String dnsIp = InetAddress
                .getByName(YOUR_HOST)
                .getHostAddress();

        HttpRequest publicIpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.ipify.org"))
                .GET()
                .build();

        var ipPublicResponse = client.send(
                publicIpRequest,
                java.net.http.HttpResponse.BodyHandlers.ofString()
        );
        System.out.println("IP Public Response: " + ipPublicResponse.body());

        String publicIp = ipPublicResponse.body().trim();

        if(dnsIp.equals(publicIp)) {
            System.out.println("O ip não mudou\n");
            return;
        }

        String credentials = email + ":" + password;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL+YOUR_HOST))
                .header("Authorization", basicAuth)
                .header("Accept", "application/json")
                .GET()
                .build();
        var updateResponse = client.send(
                updateRequest,
                java.net.http.HttpResponse.BodyHandlers.ofString()
        );
        System.out.println("BODY: " + updateResponse.body());
    }
}
