




package com.amx.jax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class WebApplication extends SpringBootServletInitializer
{

	/*
	 * public static void main(String[] args)
	 * {
	 * SpringApplication.run(SampleApplication.class, args);
	 * }
	 */

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(WebApplication.class);
	}
}