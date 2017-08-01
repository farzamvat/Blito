package com.blito.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.services.ExcelService;
import com.blito.view.BlitRecieptPdfView;

@RestController
@RequestMapping("${api.base.url}"+ "/test-pdf")
public class PdfTestController {

	@Autowired
	ExcelService excelService;
	
	@GetMapping("/xx.pdf")
	public ModelAndView testPdf() {
		return new ModelAndView(new BlitRecieptPdfView(), excelService.testPdfData());
	}
	
}
