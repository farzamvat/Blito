package com.blito.rest.controllers;

import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.models.CommonBlit;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.search.SearchViewModel;
import com.blito.services.BlitService;
import com.blito.services.ExcelService;
import com.blito.services.PaymentRequestServiceAsync;
import com.blito.view.BlitRecieptPdfView;
import com.blito.view.ExcelView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {

	@Autowired
	BlitService blitService;
	@Autowired
	PaymentRequestServiceAsync paymentRequestService;

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyBlit(@Validated @RequestBody CommonBlitViewModel vmodel,
			HttpServletRequest req, HttpServletResponse res) {
		return paymentRequestService.createCommonBlitAuthorized(vmodel)
				.handle((result, throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/search")
	public ResponseEntity<Page<CommonBlitViewModel>> search(@RequestBody SearchViewModel<CommonBlit> searchViewModel,
			Pageable pageable) {
		return ResponseEntity.ok(blitService.searchCommonBlits(searchViewModel, pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get blits with excel")
	@ApiResponses({
			@ApiResponse(code = 200, message = "get blits with excel ok", response = ModelAndView.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/blits.xlsx")
	public ModelAndView searchBlitsForExcel(@RequestBody SearchViewModel<CommonBlit> search) {
		return new ModelAndView(new ExcelView(), blitService.searchCommonBlitsForExcel(search));
	}
	
	@Permission(value = ApiBusinessName.USER)
	@GetMapping("{trackCode}/blit.pdf")
	public ModelAndView getBlitPdfReciept(@PathVariable String trackCode) {
		return new ModelAndView(new BlitRecieptPdfView(), blitService.getBlitPdf(trackCode));
	}

}
