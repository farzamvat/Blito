package com.blito;


import com.blito.services.Initiallizer;
import com.blito.services.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
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
	private Initiallizer initializer;
	@Autowired
	private MigrationService migrationService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void init() {
		// TODO: 4/12/2018 must be deleted after deployment 
//		migrationService.updateEventDates_And_BlitTypes_Uids();
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

	@GetMapping("/534250.txt")
	public String samandehi() {
		return "";
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