package com.viladafolha;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class VilaDaFolhaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VilaDaFolhaApplication.class, args);
    }

}
