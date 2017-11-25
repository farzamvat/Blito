package com.blito.rest.controllers.banner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.blito.annotations.Permission;
import com.blito.enums.ApiBusinessName;
import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.BannerViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.services.IndexBannerService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("${api.base.url}" + "/admin/index-banner")
public class AdminIndexBannerController {

	@Autowired
	IndexBannerService indexBannerService;

	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.AdminIndexBanner.class)
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated BannerViewModel vmodel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(indexBannerService.create(vmodel));
	}

	@Permission(value = ApiBusinessName.ADMIN)
	@JsonView(View.AdminIndexBanner.class)
	@PutMapping
	public CompletionStage<ResponseEntity<?>> update(@RequestBody @Validated BannerViewModel vmodel,
			HttpServletRequest req,HttpServletResponse res) {
		return CompletableFuture.supplyAsync(() -> indexBannerService.update(vmodel))
				.handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}

	@Permission(value = ApiBusinessName.ADMIN)
	@DeleteMapping("/{indexBannerId}")
	public CompletionStage<ResponseEntity<?>> delete(@PathVariable long indexBannerId, HttpServletRequest req,
			HttpServletResponse res) {
		return CompletableFuture.runAsync(() -> indexBannerService.delete(indexBannerId))
				.handle((result, throwable) -> HandleUtility.generateResponseResult(
						() -> new ResultVm(ResourceUtil.getMessage(Response.SUCCESS)), throwable, req, res));
	}
}
