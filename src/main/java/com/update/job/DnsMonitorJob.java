package com.update.job;

import com.update.config.AppConfig;
import com.update.infra.network.GetPublicIp;
import com.update.infra.client.ChangeIpClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class DnsMonitorJob {

    private final AppConfig appConfig;
    private Optional<String> lastKnowIP = Optional.empty();
    private volatile boolean isRunning = true;

    public DnsMonitorJob(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void startMonitor(){
        while (isRunning) {
            IO.println("Tentando Atualizar Host: " + appConfig.host());
            try {
               Optional<String> currentPublicIp = GetPublicIp.getPublicIp();
                if (currentPublicIp.equals(lastKnowIP)) {
                    IO.println("O ip não mudou");
                } else {
                    boolean updeted = ChangeIpClient.start(appConfig);
                    if (updeted) {
                     lastKnowIP = currentPublicIp;
                        IO.println("O ip foi atualizado com sucesso");
                    } else {
                        IO.println("Erro ao atualizar o ip");
                    }
                }
            } finally {
                if (isRunning) {
                    pauseMonitor();
                }
            }
        }
    }

    public void pauseMonitor(){
        try {
            TimeUnit.MINUTES.sleep(appConfig.timeCheckMinutes());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopMonitor() {
        this.isRunning = false;
    }
}
