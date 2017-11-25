package com.blito.rest.controllers.salon;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.View;
import com.blito.services.SalonService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Farzam Vatanzadeh
 * 10/21/17
 * Mailto : farzam.vat@gmail.com
 **/

@RestController
@RequestMapping("${api.base.url}" + "/public/salons")
public class PublicSalonController {
    private SalonService salonService;

    @Autowired
    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    @JsonView(View.SalonSchema.class)
    @GetMapping("/populated-schema/{eventDateId}")
    public CompletionStage<ResponseEntity<?>> getPopulatedSalonSchemaByEventDateId(@PathVariable Long eventDateId,
                                                                                   HttpServletRequest request,
                                                                                   HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> salonService.populateSeatInformationInSalonSchemaByEventDateId(eventDateId))
                .handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
    }
}
