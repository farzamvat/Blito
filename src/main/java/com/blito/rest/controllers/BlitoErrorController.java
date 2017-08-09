package com.blito.rest.controllers;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

//@RestController
//public class BlitoErrorController implements ErrorController {
//
//	private static final String ERROR_MAPPING = "/error";
//	
//	@Override
//	public String getErrorPath() {
//		return ERROR_MAPPING;
//	}
//	
//	@RequestMapping(path=ERROR_MAPPING)
//	public RedirectView error()
//	{
//		return new RedirectView("/not-found");
//	}
//
//}
