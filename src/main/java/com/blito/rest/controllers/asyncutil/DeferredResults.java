package com.blito.rest.controllers.asyncutil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.web.context.request.async.DeferredResult;

public class DeferredResults {
	private DeferredResults() {
	}
 
	public static <T> DeferredResult<T> from(final CompletableFuture<T> future) {
		final DeferredResult<T> deferred = new DeferredResult<>();
		future.thenAccept(deferred::setResult);
		future.exceptionally(ex -> {
			if (ex instanceof CompletionException) {
				deferred.setErrorResult(ex.getCause());
			} else {
				deferred.setErrorResult(ex);
			}
			return null;
		});
		return deferred;
	}
}
