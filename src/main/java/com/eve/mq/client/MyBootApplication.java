package com.eve.mq.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.lingzhi.mq.client"})
public class MyBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBootApplication.class);
    }

}