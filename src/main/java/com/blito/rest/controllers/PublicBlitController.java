package com.blito.rest.controllers;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.services.BlitService;
import com.blito.services.PaymentRequestService;
import com.blito.view.BlitReceiptPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}" + "/public/blits")
public class PublicBlitController {
	
	@Autowired
	private BlitService blitService;
	@Autowired
	private PaymentRequestService paymentRequestServicec;
	
	@GetMapping("/{trackCode}")
	public ResponseEntity<?> getBlit(@PathVariable String trackCode)
	{
		return ResponseEntity.ok(blitService.getBlitByTrackCode(trackCode));
	}
	
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyRequestForFreeBlit(@Validated @RequestBody CommonBlitViewModel vmodel,HttpServletRequest req,HttpServletResponse res)
	{
		return CompletableFuture.supplyAsync(() -> paymentRequestServicec.createCommonBlitUnauthorizedAndNoneFreeBlits(vmodel))
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
	
	@GetMapping("/{trackCode}/blit.pdf")
	public ModelAndView getBlitPdfReciept(@PathVariable String trackCode) {
		return new ModelAndView(new BlitReceiptPdfView(), blitService.getBlitPdf(trackCode));
	}
}
