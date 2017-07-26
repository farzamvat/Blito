package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.services.ExcelService;
import com.blito.view.PdfView;

@RestController
@RequestMapping("${api.base.url}" + "/pdftest")
public class TestPdfController {
	
	@Autowired
	ExcelService excelService;
	
	@GetMapping
	public ModelAndView getPdf() {
		return new ModelAndView(new PdfView(), excelService.test());
	}

}
