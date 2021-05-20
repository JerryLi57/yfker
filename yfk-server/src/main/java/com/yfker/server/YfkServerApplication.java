package com.yfker.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.yfker"})
public class YfkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YfkServerApplication.class, args);
    }

}
