package com.liuyang19900520.bugs.l13.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


public class CommonMistakesApplication {

    public static void main(String[] args) {
        System.setProperty("logging.config", "classpath:org/geekbang/time/commonmistakes/logging/async/asyncwrong.xml");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

