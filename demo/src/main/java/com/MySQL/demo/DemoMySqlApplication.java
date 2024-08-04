package com.MySQL.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.MySQL.demo.repository")
public class DemoMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMySqlApplication.class, args);
	}
}
