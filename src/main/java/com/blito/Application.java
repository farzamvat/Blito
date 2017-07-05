package com.blito;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.services.Initiallizer;

@SpringBootApplication
@RestController
public class Application {
	
	@Autowired Initiallizer initializer;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	public void init()
	{
		initializer.importPermissionsToDataBase();
		initializer.insertAdminUserAndRoleAndOldBlitoUsers();
	}
	
	@GetMapping("/")
	public ModelAndView index()
	{
		return new ModelAndView("index");
	}
	
	@GetMapping("/event-page/{eventLink}")
	public ModelAndView event(@PathVariable String eventLink)
	{
		return new ModelAndView("index");
	}
}