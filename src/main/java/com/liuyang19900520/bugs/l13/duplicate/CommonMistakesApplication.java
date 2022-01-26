package com.liuyang19900520.bugs.l13.duplicate;

import org.springframework.boot.SpringApplication;


public class CommonMistakesApplication {

  public static void main(String[] args) {
//        System.out.println(System.getProperty("classpath"));
//        System.setProperty("logging.config", "classpath:multiplelevelsfilter.xml");
    SpringApplication.run(CommonMistakesApplication.class, args);
  }
}

