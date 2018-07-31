package com.amx.jax;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@ServletComponentScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.amx.jax")
@EnableAsync
@EnableCaching
public class WebApplication extends SpringBootServletInitializer {

	/*
	 * public static void main(String[] args) {
	 * SpringApplication.run(SampleApplication.class, args); }
	 */

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebApplication.class);
	}
}
