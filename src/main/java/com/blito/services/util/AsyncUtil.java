package com.blito.services.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/*
    @author Farzam Vatanzadeh
*/
@Component
public class AsyncUtil {

    private static final Logger logger = LoggerFactory.getLogger(AsyncUtil.class);

    public static CompletableFuture<Optional<Throwable>> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable).handle((aVoid, throwable) -> {
            if(throwable != null) {
                logger.error("Exception in AsyncUtil ",throwable.getCause());
                return Optional.of(throwable.getCause());
            } else {
                return Optional.empty();
            }
        });
    }
}
