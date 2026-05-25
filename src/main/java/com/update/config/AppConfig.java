package com.update.config;

public record AppConfig(
        String email,
        String password,
        String apiUrl,
        String host,
        byte timeCheckMinutes
) {
    public AppConfig {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("A variável APP_EMAIL é obrigatória.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("A variável APP_PASSWORD é obrigatória.");
        }
        if (apiUrl == null || !apiUrl.startsWith("http")) {
            throw new IllegalArgumentException("A variável APP_API_URL deve conter um protocolo HTTP/HTTPS válido.");
        }
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("A variável APP_YOUR_HOST é obrigatória.");
        }
        if (timeCheckMinutes <= 0) {
            throw new IllegalArgumentException("A variável APP_TIME_CHECK deve ser maior que zero.");
        }
    }
}