package com.blito;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.blito.services.Initiallizer;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	Initiallizer initializer;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void init() {
		initializer.importPermissionsToDataBase();
		initializer.insertAdminUserAndRoleAndOldBlitoUsers();
	}
	
	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}	

	@Bean
    public HandlerExceptionResolver customExceptionResolver () {
        SimpleMappingExceptionResolver s = new SimpleMappingExceptionResolver();
        Properties p = new Properties();
        p.setProperty(NoHandlerFoundException.class.getName(), "index");
        s.setExceptionMappings(p);
        s.addStatusCode("index", 404);
        //This resolver will be processed before default ones
        s.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return s;
    }
	
	@GetMapping("/*")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@GetMapping("/event-page/{eventLink}")
	public ModelAndView event(@PathVariable String eventLink) {
		return new ModelAndView("index");
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
		crlf.setIncludeClientInfo(true);
		crlf.setIncludeQueryString(true);
		crlf.setIncludePayload(true);
		crlf.setMaxPayloadLength(50000);
		return crlf;
	}
}