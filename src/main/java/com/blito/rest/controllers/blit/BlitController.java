package com.blito.rest.controllers.blit;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.models.CommonBlit;
import com.blito.models.SeatBlit;
import com.blito.models.User;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blit.ReservedBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.ExcelService;
import com.blito.services.blit.CommonBlitService;
import com.blito.services.blit.SeatBlitService;
import com.blito.view.BlitReceiptPdfView;
import com.blito.view.ExcelView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}" + "/blits")
public class BlitController {

	@Autowired
	private CommonBlitService commonBlitService;
	@Autowired
	private SeatBlitService seatBlitService;
	@Autowired
    private ExcelService excelService;

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/buy-request")
	public CompletionStage<ResponseEntity<?>> buyBlit(@Validated @RequestBody CommonBlitViewModel vmodel,
			HttpServletRequest req, HttpServletResponse res) {
		User user = SecurityContextHolder.currentUser();
		return CompletableFuture.supplyAsync(() -> commonBlitService.createBlitAuthorized(vmodel,user))
				.handle((result, throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/buy-request-with-seat")
	public CompletionStage<ResponseEntity<?>> buyBlitWithSeat(@Validated @RequestBody SeatBlitViewModel viewModel,
															  HttpServletRequest request,HttpServletResponse response) {
		User user = SecurityContextHolder.currentUser();
		return CompletableFuture.supplyAsync(() -> seatBlitService.createBlitAuthorized(viewModel,user))
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
	}

	@JsonView(View.Blit.class)
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/search")
	public ResponseEntity<Page<CommonBlitViewModel>> commonBlitSearch(@RequestBody SearchViewModel<CommonBlit> searchViewModel,
			Pageable pageable) {
		return ResponseEntity.ok(commonBlitService.searchBlits(searchViewModel, pageable));
	}

	@JsonView(View.Blit.class)
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/seats/search")
	public ResponseEntity<Page<SeatBlitViewModel>> seatBlitSearch(@RequestBody SearchViewModel<SeatBlit> searchViewModel,
															Pageable pageable) {
		return ResponseEntity.ok(seatBlitService.searchBlits(searchViewModel, pageable));
	}

	// ***************** SWAGGER DOCS ***************** //
	@ApiOperation(value = "get blits with excel")
	@ApiResponses({
			@ApiResponse(code = 200, message = "get blits with excel ok", response = ModelAndView.class) })
	// ***************** SWAGGER DOCS ***************** //
	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/blits.xlsx")
	public ModelAndView searchBlitsForExcel(@RequestBody SearchViewModel<CommonBlit> search) {
		return new ModelAndView(new ExcelView(), commonBlitService.searchBlitsForExcel(search));
	}

	@Permission(value = ApiBusinessName.USER)
	@PostMapping("/seats/blits.xlsx")
	public ModelAndView searchSeatBlitsForExcel(@RequestBody SearchViewModel<SeatBlit> search) {
		return new ModelAndView(new ExcelView(), seatBlitService.searchBlitsForExcel(search));
	}

    @Permission(value = ApiBusinessName.USER)
    @PostMapping("/generate-reserved-blit.pdf")
    public ModelAndView generateReservedBlit(@Validated @RequestBody ReservedBlitViewModel reservedBlitViewModel) {
	    User user = SecurityContextHolder.currentUser();
	    return new ModelAndView(new BlitReceiptPdfView(), seatBlitService.generateReservedBlit(reservedBlitViewModel, user));

    }


}
