package com.blito.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/*
    @author Farzam Vatanzadeh
*/

public class AsyncUtil {

    private static final Logger logger = LoggerFactory.getLogger(AsyncUtil.class);

    public static CompletableFuture<Optional<Throwable>> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable).handle((aVoid, throwable) -> logIfExeption(throwable));
    }

    public static <T> CompletableFuture<Optional<Throwable>> run(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier).handle((result,throwable) -> logIfExeption(throwable));
    }

    private static Optional<Throwable> logIfExeption(Throwable throwable) {
        if(throwable != null) {
            logger.error("Exception in AsyncUtil ",throwable.getCause());
            return Optional.of(throwable.getCause());
        } else {
            return Optional.empty();
        }
    }
}
