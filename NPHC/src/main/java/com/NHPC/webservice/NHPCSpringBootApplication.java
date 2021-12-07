package com.NHPC.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com")
public class NHPCSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(NHPCSpringBootApplication.class, args);
	}

}
