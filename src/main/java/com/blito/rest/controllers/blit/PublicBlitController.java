package com.blito.rest.controllers.blit;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.services.ExcelService;
import com.blito.services.blit.CommonBlitService;
import com.blito.services.blit.SeatBlitService;
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
	private CommonBlitService commonBlitService;
	@Autowired
	private SeatBlitService seatBlitService;
	@Autowired
	private ExcelService excelService;
	
	@GetMapping("/{trackCode}")
	public ResponseEntity<?> getBlit(@PathVariable String trackCode)
	{
		return ResponseEntity.ok(commonBlitService.getBlitByTrackCode(trackCode));
	}
	
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyRequestForFreeBlit(@Validated @RequestBody CommonBlitViewModel vmodel,HttpServletRequest req,HttpServletResponse res)
	{
		return CompletableFuture.supplyAsync(() -> commonBlitService.createUnauthorizedAndNoneFreeBlits(vmodel))
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}

	@PostMapping("/buy-request-with-seat")
	public CompletionStage<ResponseEntity<?>> buyBlitWithSeat(@Validated @RequestBody SeatBlitViewModel viewModel,
															  HttpServletRequest request,HttpServletResponse response) {
		return CompletableFuture.supplyAsync(() -> seatBlitService.createUnauthorizedAndNoneFreeBlits(viewModel))
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
	}
	
	@GetMapping("/{trackCode}/blit.pdf")
	public ModelAndView getBlitPdfReciept(@PathVariable String trackCode) {
		return new ModelAndView(new BlitReceiptPdfView(), commonBlitService.getBlitPdf(trackCode));
	}

	@GetMapping("testpdf.pdf")
	public ModelAndView testPdf(){
		return new ModelAndView(new BlitReceiptPdfView(), excelService.testPdfData());
	}
}
