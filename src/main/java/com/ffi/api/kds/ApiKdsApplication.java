package com.ffi.api.kds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = "file:${app.external.properties}")
public class ApiKdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiKdsApplication.class, args);
	}
}
