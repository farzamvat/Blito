package com.blito.rest.controllers;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.validation.ControllerEnumValidation;
import com.blito.exceptions.ExceptionUtil;
import com.blito.models.User;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}" + "/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Permission(value = ApiBusinessName.USER)
    @PostMapping("/set-discount-code")
    public CompletionStage<ResponseEntity<?>> setDiscountCode(@Valid @RequestBody DiscountViewModel vmodel,
                                                              BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res) {
        if (bindingResult.hasFieldErrors())
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(ExceptionUtil
                    .generate(HttpStatus.BAD_REQUEST, req, bindingResult, ControllerEnumValidation.class)));
        User user = SecurityContextHolder.currentUser();
        return CompletableFuture.supplyAsync(() -> discountService.setDiscountCode(vmodel,user))
                .handle((either, throwable) -> HandleUtility.generateEitherResponse(either, throwable, req, res));
    }


}
