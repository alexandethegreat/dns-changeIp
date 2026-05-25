package com.update;


import com.update.config.AppConfig;
import com.update.job.DnsMonitorJob;

public class Main {

    public static void main(String[] args) {
        try {
            AppConfig appConfig = new AppConfig(
                    System.getenv("APP_EMAIL"),
                    System.getenv("APP_PASSWORD"),
                    System.getenv("APP_API_URL"),
                    System.getenv("APP_YOUR_HOST"),
                    Byte.parseByte(System.getenv("APP_TIME_CHECK"))
            );

            DnsMonitorJob dnsMonitorJob = new DnsMonitorJob(appConfig);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nPreparando para desligar...");
                dnsMonitorJob.stopMonitor();
            }));

            System.out.println("Serviço iniciado com sucesso.");
            dnsMonitorJob.startMonitor();

        } catch (Exception e) {
            System.err.println("Erro crítico ao iniciar a aplicação: " + e.getMessage());
            System.exit(1);
        }
    }
}