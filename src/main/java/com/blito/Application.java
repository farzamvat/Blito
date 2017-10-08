package com.blito;


import com.blito.services.Initiallizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;


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
		initializer.insertSalonSchemasAndDataIntoDB();
	}
	@GetMapping("/*")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	
	@GetMapping("/not-found")
	public ModelAndView notFound()
	{
		return new ModelAndView("index");
	}

	@GetMapping("/event-page/{eventLink}")
	public ModelAndView event(@PathVariable String eventLink) {
		return new ModelAndView("index");
	}
	
	@GetMapping("/exchange-page/{exchangeLink}")
	public ModelAndView exchangeBlit(@PathVariable String exchangeLink)
	{
		return new ModelAndView("index");
	}
	
	@GetMapping("/event-host-page/{eventHostLink}")
	public ModelAndView eventHost(@PathVariable String eventHostLink)
	{
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