package com.bank.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.bank.common.entity"})
public class BankingAppFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAppFrontApplication.class, args);
    }

}
