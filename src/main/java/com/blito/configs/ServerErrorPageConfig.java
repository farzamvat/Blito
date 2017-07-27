package com.blito.configs;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

//@Configuration

//public class ServerErrorPageConfig {
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//	 
//	   return (container -> {
//	        ErrorPage error404Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/404page.html");
//	        container.addErrorPages(error404Page);
//	   });
//	}
//}
