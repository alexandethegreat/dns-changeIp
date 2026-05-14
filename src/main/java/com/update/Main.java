package com.update;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    static  String EMAIL = System.getenv("APP_EMAIL");
    static  String PASSWORD = System.getenv("APP_PASSWORD");
    static  String APP_API_URL =  System.getenv("APP_API_URL");
    static  String YOUR_HOST = System.getenv("APP_YOUR_HOST");
    static  byte APP_TIME_CHECK = Byte.parseByte(System.getenv("APP_TIME_CHECK"));


    void main(String[] args) throws InterruptedException, IOException {

        if (
                        EMAIL == null || EMAIL.isBlank() ||
                        PASSWORD == null || PASSWORD.isBlank() ||
                        APP_API_URL == null || APP_API_URL.isBlank() ||
                        YOUR_HOST == null || YOUR_HOST.isBlank() ||
                        APP_TIME_CHECK <= 0
        ) {
            System.err.println("Erro: Variáveis inválidas.");
            System.exit(1);
        }


        System.out.println("Script iniciado para o usuário com o email : " + EMAIL);

        while (true) {
            try {
                UpdateIp.UpdateIp(EMAIL, PASSWORD, APP_API_URL, YOUR_HOST);

            } catch (Exception e) {
                System.err.println("Erro na execução: " + e.getCause().getMessage());
            }
            TimeUnit.MINUTES.sleep(APP_TIME_CHECK);
        }




    }

}