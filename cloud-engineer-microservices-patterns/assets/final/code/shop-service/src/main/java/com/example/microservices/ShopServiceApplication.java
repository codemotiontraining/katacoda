package com.example.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableEurekaClient
public class ShopServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopServiceApplication.class, args);
	}
}
