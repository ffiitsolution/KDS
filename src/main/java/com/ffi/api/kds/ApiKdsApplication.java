package com.ffi.api.kds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ApiKdsApplication extends SpringBootServletInitializer {
    	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiKdsApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiKdsApplication.class, args);
	}

}
