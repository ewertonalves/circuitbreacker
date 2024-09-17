package com.pedidosepagamentos.circuitbreacker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CircuitbrakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitbrakerApplication.class, args);
	}

}
