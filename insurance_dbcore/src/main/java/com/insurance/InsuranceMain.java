




package com.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com")
@EnableAutoConfiguration
@EntityScan(basePackages = "com")
public class InsuranceMain
{
	public static void main(String[] args)
	{

		System.out.println("InsuranceMain :: main ");
		SpringApplication.run(InsuranceMain.class, args);
		
	}

}
