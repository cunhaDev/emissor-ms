package com.emissor;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class EmissorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmissorApplication.class, args);
	}
}
