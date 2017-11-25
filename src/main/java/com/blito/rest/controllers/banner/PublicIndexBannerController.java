package com.blito.rest.controllers.banner;

import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.View;
import com.blito.services.IndexBannerService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("${api.base.url}" + "/public/index-banners")
public class PublicIndexBannerController {
	@Autowired
	private IndexBannerService indexBannerService;

	@JsonView(View.IndexBanner.class)
	@GetMapping
	public CompletionStage<ResponseEntity<?>> getIndexBanners(Pageable pageable, HttpServletRequest req,
			HttpServletResponse res) {
		return CompletableFuture.supplyAsync(() -> indexBannerService.getIndexBanners(pageable))
				.handle((result, throwable) -> HandleUtility.generateResponseResult(() -> result, throwable, req, res));
	}
}
