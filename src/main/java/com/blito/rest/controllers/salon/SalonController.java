package com.blito.rest.controllers.salon;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.salon.SalonViewModel;
import com.blito.services.SalonService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/*
    @author Farzam Vatanzadeh
*/
@RestController
@RequestMapping("${api.base.url}" + "/salons")
public class SalonController {
    @Autowired
    private SalonService salonService;

    @JsonView(View.SimpleSalon.class)
    @Permission(value = ApiBusinessName.USER)
    @GetMapping
    public CompletionStage<ResponseEntity<?>> getSalons(Pageable pageable,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> salonService.getSalons(pageable))
                .handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
    }

    @JsonView(View.SalonSchema.class)
    @Permission(value = ApiBusinessName.USER)
    @GetMapping("/{uid}")
    public CompletionStage<ResponseEntity<?>> getSalonsByUid(@PathVariable String uid,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> salonService.getSalonByUid(uid))
                .handle((either,throwable) -> HandleUtility.generateEitherResponse(either,throwable,request,response));
    }

    @JsonView(View.IncludingCustomerNameSalonSchema.class)
    @Permission(value = ApiBusinessName.USER)
    @GetMapping("/populated-schema/{eventDateId}")
    public CompletionStage<ResponseEntity<?>> getPopulatedSalonSchemaByEventDateId(@PathVariable Long eventDateId,
                                                                                   HttpServletRequest request,
                                                                                   HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> salonService.populateSeatInformationInSalonSchemaByEventDateId(eventDateId))
                .handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
    }

    @JsonView(View.SalonSchema.class)
    @Permission(value = ApiBusinessName.ADMIN)
    @PutMapping("/upload-svg")
    public CompletionStage<ResponseEntity<?>> updateSalonAndSectionsSvg(@RequestBody @Validated SalonViewModel salonViewModel,
                                                                        HttpServletRequest request,
                                                                        HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> salonService.updateSalonAndItsSectionsSvg(salonViewModel))
                .handle((either,throwable) -> HandleUtility.generateEitherResponse(either,throwable,request,response));
    }
}
