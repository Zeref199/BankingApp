package com.bank.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.bank.common.entity"})
public class BankAdminDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAdminDashboardApplication.class, args);
	}

}
