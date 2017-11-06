package com.blito.rest.controllers.analytics;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.rest.utility.HandleUtility;
import com.blito.services.AdminAnalyticsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Farzam Vatanzadeh
 * 11/6/17
 * Mailto : farzam.vat@gmail.com
 **/

@RestController
@RequestMapping("${api.base.url}" + "/admin/analytics")
public class AdminAnalyticsController {
    @Autowired
    private AdminAnalyticsReportService adminAnalyticsReportService;

    @Permission(ApiBusinessName.ADMIN)
    @GetMapping
    public CompletionStage<ResponseEntity<?>> generalAdminAnalytics(HttpServletRequest request,
                                                                    HttpServletResponse response) {
        return CompletableFuture.supplyAsync(() -> adminAnalyticsReportService.getAdminAnalyticsReport())
                .handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,request,response));
    }
}
