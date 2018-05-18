package com.aukey.report.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Web cas 登陆Session灯状态切面拦截
 *
 *
 * @version 1.0.0
 */
@Aspect
@Order(2)
@Component
public class TaskAspect {

	 @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
	    public void doIt(ProceedingJoinPoint pjp) throws Throwable {
	        System.out.println("cancel"+pjp.toString());
	        pjp.proceed();
	        return;
	       
	       
	    }


}

