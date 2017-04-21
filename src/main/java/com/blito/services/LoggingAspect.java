package com.blito.services;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class LoggingAspect {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Before("execution(* com.blito.rest.controllers.AccountController.*(..))")
	public void beforelog(JoinPoint joinPoint){
		log.info(joinPoint.getSignature().getName() + " is going to call");
	}
	
	@After("execution(* com.blito.rest.controllers.AccountController.*(..))")
	public void afterlog(JoinPoint joinPoint){
		log.info(joinPoint.getSignature().getName() + " is called");
	}
}
