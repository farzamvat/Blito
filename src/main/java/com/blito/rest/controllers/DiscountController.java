package com.blito.rest.controllers;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.exceptions.ExceptionUtil;
import com.blito.models.Discount;
import com.blito.models.User;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.discount.DiscountEnableViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Permission(value = ApiBusinessName.USER)
    @PostMapping("/discount/set-discount-code")
    public CompletionStage<ResponseEntity<?>> setDiscountCodeByUser(@Valid @RequestBody DiscountViewModel vmodel,
                                                                    BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
                    .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(() -> discountService.setDiscountCodeByUser(vmodel,user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }

    @PostMapping("/validate-discount-code")
    public CompletionStage<ResponseEntity<?>> validateDiscountCode(@Valid @RequestBody DiscountValidationViewModel vmodel,
                                                                   BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res){
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
                .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        return CompletableFuture.supplyAsync(() -> discountService.validateDiscountCode(vmodel))
                .handle((response, throwable) -> HandleUtility.generateResponseResult(() -> response, throwable, req, res));
    }

    @Permission(value = ApiBusinessName.ADMIN)
    @PostMapping("/discount/admin-set-discount-code")
    public CompletionStage<ResponseEntity<?>> setDiscountCodeByOperator(@Valid @RequestBody DiscountViewModel vmodel,
                                                                        BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
                    .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(() -> discountService.setDiscountCodeByOperator(vmodel,user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }

    @Permission(value = ApiBusinessName.USER)
    @PutMapping("/discount/update-discount-code")
    public CompletionStage<ResponseEntity<?>> updateDiscountCodeByUser(@Valid @RequestBody DiscountViewModel vmodel,
                                                                       BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res){
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
            .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(()->discountService.updateDiscountCodeByUser(vmodel, user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }

    @Permission(value = ApiBusinessName.ADMIN)
    @PutMapping("/discount/admin-update-discount-code")
    public CompletionStage<ResponseEntity<?>> updateDiscountCodeByOperator(@Valid @RequestBody DiscountViewModel vmodel,
                                                                           BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res){
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
            .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(()->discountService.updateDiscountCodeByOperator(vmodel, user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }

    @Permission(ApiBusinessName.USER)
    @PutMapping("/discount/set-enable")
    public CompletionStage<ResponseEntity<?>> enableDiscountCode(@Valid @RequestBody DiscountEnableViewModel viewModel,
                                                                 BindingResult bindingResult,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response) {
        if(bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil.generate(HttpStatus.BAD_REQUEST, ResourceUtil.getMessage(Response.VALIDATION),bindingResult,request)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(() -> discountService.enableDiscountCode(viewModel,user))
                .handle((either, throwable) ->
                        HandleUtility.generateEitherResponse(either,throwable,request,response));
    }

    @Permission(ApiBusinessName.USER)
    @PostMapping("/discount/search")
    public CompletionStage<ResponseEntity<?>> searchDiscountCodes(@RequestBody SearchViewModel<Discount> searchViewModel,
                                                                  Pageable pageable,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> discountService.searchDiscountCodes(searchViewModel,pageable))
                .handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
    }

    @Permission(ApiBusinessName.ADMIN)
    @PostMapping("discount/set-discount-all-events")
    public CompletionStage<ResponseEntity<?>> setDiscountForAllEvents(@Valid @RequestBody DiscountViewModel vmodel,
                                                                      BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
                    .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(() -> discountService.setDiscountForAllEvents(vmodel,user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }

}
