package com.example.zshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZshopApplication.class, args);
    }

}
