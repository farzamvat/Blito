package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.models.EventHost;
import com.blito.search.SearchViewModel;
import com.blito.services.EventHostService;
import com.blito.view.PdfView;

@RestController
@RequestMapping("${api.base.url}" + "/pdftest")
public class TestPdfController {
	
	@Autowired
	EventHostService eventHostService;
	
	
	
	@PostMapping("/blits.pdf")
	public ModelAndView getPdf(@RequestBody SearchViewModel<EventHost> search) {
		return new ModelAndView(new PdfView(), eventHostService.searchEventHostsForExcel(search));
	}

}
