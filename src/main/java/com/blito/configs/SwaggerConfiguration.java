package com.blito.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
	@SuppressWarnings("deprecation")
	@Bean
	public Docket api() 
	{
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfo("Blito", "Tickets", "0.0.1", "", "f.vatanzadeh@gmail.com", "", ""))
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.blito.rest.controllers"))
				.paths(PathSelectors.any())
				.build();
	}
}
