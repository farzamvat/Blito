package com.blito.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blito.repositories.UserRepository;

@Configuration
@EnableAspectJAutoProxy
public class SecurityConfig {
	
	@Value("${token.secret}")
	String tokenSecret;
	@Value("${api.base.url}")
	String baseUrl;
	@Autowired
	@Bean
	public FilterRegistrationBean jwtFilter(UserRepository userRepository)
	{
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new JwtFilter(userRepository,tokenSecret));
		filterRegistration.setOrder(1);
		filterRegistration.setUrlPatterns(
				PathBuilder.build()
					.setBaseUrl(baseUrl)
					.addMatcher("/event-host/*")
					.addMatcher("/account/*")
					.addMatcher("/operator/*")
					.addMatcher("/enums/*")
					.addMatcher("/exchange-blits*")
					.getUrlPatterns());
		return filterRegistration;
	}
	
	@Bean
	public FilterRegistrationBean corsFilter()
	{
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CorsFilter());
		filterRegistrationBean.setOrder(0);
		return filterRegistrationBean;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}